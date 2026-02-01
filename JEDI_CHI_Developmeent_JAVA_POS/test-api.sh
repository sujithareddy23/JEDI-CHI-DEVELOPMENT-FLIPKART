#!/bin/bash

# FlipFit Gym Management System - API Test Script
# This script tests the REST API endpoints

echo "=========================================="
echo "FlipFit Gym Management System API Test"
echo "=========================================="

BASE_URL="http://localhost:8080/api"
ADMIN_URL="http://localhost:8081"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
    fi
}

# Function to test an endpoint
test_endpoint() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    
    echo -e "\n${YELLOW}Testing: $description${NC}"
    echo "Request: $method $url"
    
    if [ -n "$data" ]; then
        echo "Data: $data"
        response=$(curl -s -w "\n%{http_code}" -X $method \
            -H "Content-Type: application/json" \
            -d "$data" \
            "$url")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$url")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    echo "HTTP Status: $http_code"
    echo "Response: $body"
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        print_status 0 "SUCCESS"
        return 0
    else
        print_status 1 "FAILED"
        return 1
    fi
}

# Check if server is running
echo -e "\n${YELLOW}Checking if server is running...${NC}"
if curl -s "$ADMIN_URL/healthcheck" > /dev/null 2>&1; then
    print_status 0 "Server is running"
else
    print_status 1 "Server is not running. Please start the server first:"
    echo "java -jar target/flipfit-gym-management-1.0.0.jar server config.yml"
    exit 1
fi

# Test Health Check
test_endpoint "GET" "$ADMIN_URL/healthcheck" "" "Health Check"

# Test Login endpoints
test_endpoint "POST" "$BASE_URL/users/login" \
    '{"identifier":"admin","password":"admin123","role":"ADMIN"}' \
    "Admin Login"

test_endpoint "POST" "$BASE_URL/users/login" \
    '{"identifier":"customer@example.com","password":"customer123","role":"CUSTOMER"}' \
    "Customer Login"

test_endpoint "POST" "$BASE_URL/users/login" \
    '{"identifier":"owner@example.com","password":"owner123","role":"OWNER"}' \
    "Owner Login"

# Test invalid login
test_endpoint "POST" "$BASE_URL/users/login" \
    '{"identifier":"invalid","password":"invalid","role":"ADMIN"}' \
    "Invalid Login (should fail)"

# Test User Registration endpoints
test_endpoint "POST" "$BASE_URL/users/register/customer" \
    '{"id":"CUST999","name":"Test Customer","email":"test@example.com","password":"test123","mobileNo":"9876543210","address":"Test Address"}' \
    "Customer Registration"

test_endpoint "POST" "$BASE_URL/users/register/owner" \
    '{"id":"OWNER999","ownerName":"Test Owner","emailId":"owner@example.com","password":"test123","idProof":"ID123","panNo":"PAN123","gstNo":"GST123","validated":false}' \
    "Owner Registration"

# Test Gym endpoints
test_endpoint "GET" "$BASE_URL/gyms" "" "Get All Gyms"

test_endpoint "GET" "$BASE_URL/gyms/GYM001" "" "Get Gym by ID"

test_endpoint "POST" "$BASE_URL/gyms" \
    '{"gymId":"GYM999","name":"Test Gym","location":"Test Location","contactNo":"1234567890","ownerId":"OWNER001","validated":false}' \
    "Create New Gym"

test_endpoint "GET" "$BASE_URL/gyms/INVALID" "" "Get Invalid Gym (should fail)"

# Test Slot endpoints
test_endpoint "GET" "$BASE_URL/slots" "" "Get All Slots"

test_endpoint "GET" "$BASE_URL/slots/SLOT001" "" "Get Slot by ID"

test_endpoint "GET" "$BASE_URL/slots/gym/GYM001" "" "Get Slots by Gym"

test_endpoint "POST" "$BASE_URL/slots" \
    '{"slotId":"SLOT999","gymId":"GYM001","totalCapacity":25,"startTime":"09:00:00","endTime":"10:00:00"}' \
    "Create New Slot"

# Test Booking endpoints
test_endpoint "GET" "$BASE_URL/bookings" "" "Get All Bookings"

test_endpoint "POST" "$BASE_URL/bookings" \
    '{"id":"BK999","bookingDate":"2026-02-01","statusId":1,"customerId":"CUST002","slotId":"SLOT001","gymId":"GYM001","slotStartTime":"06:00:00","slotEndTime":"08:00:00","paymentStatus":"PENDING"}' \
    "Create New Booking"

test_endpoint "GET" "$BASE_URL/bookings/customer/CUST002" "" "Get Bookings by Customer"

test_endpoint "GET" "$BASE_URL/bookings/slot/SLOT001" "" "Get Bookings by Slot"

# Test Notification endpoints
test_endpoint "GET" "$BASE_URL/notifications" "" "Get All Notifications"

test_endpoint "POST" "$BASE_URL/notifications" \
    '{"notificationId":"NOTIF999","customerId":"CUST002","message":"Test notification","timestamp":"2026-02-01T05:00:00Z","read":false}' \
    "Create New Notification"

test_endpoint "GET" "$BASE_URL/notifications/customer/CUST002" "" "Get Notifications by Customer"

test_endpoint "PUT" "$BASE_URL/notifications/NOTIF999/read" "" "Mark Notification as Read"

echo -e "\n${YELLOW}=========================================="
echo "API Test Complete"
echo "==========================================${NC}"
