# FlipFit Gym Management System - REST API

A comprehensive REST API for gym management, converted from a console application to a modern Dropwizard-based web service.

## 🏋️ Features

- **User Authentication** - Multi-role login (Admin, Customer, Owner)
- **Gym Management** - Complete CRUD operations for gyms
- **Slot Management** - Time slot scheduling and management
- **Booking System** - Gym slot booking with payment tracking
- **Notifications** - Customer notification system
- **Database Integration** - MySQL with Hibernate ORM

## 🚀 Quick Start

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Installation

1. **Clone the repository**
   ```bash
   cd /Users/nitheezkant.r/train/JEDI-CHI-DEVELOPMENT-FLIPKART/JEDI_CHI_Developmeent_JAVA_POS
   ```

2. **Set Java path**
   ```bash
   export PATH="/opt/homebrew/opt/openjdk@11/bin:$PATH"
   ```

3. **Build the application**
   ```bash
   mvn clean package -DskipTests
   ```

4. **Start the server**
   ```bash
   java -jar target/flipfit-gym-management-1.0.0.jar server config.yml
   ```

The server will start on:
- **API Server**: http://localhost:8080
- **Admin Server**: http://localhost:8081

## 📋 API Endpoints

### Health Check
- `GET /healthcheck` - Server health status

### Authentication
- `POST /api/users/login` - User login
- `POST /api/users/register/customer` - Customer registration
- `POST /api/users/register/owner` - Owner registration
- `POST /api/users/register/admin` - Admin registration

### Gym Management
- `GET /api/gyms` - Get all gyms
- `GET /api/gyms/{gymId}` - Get gym by ID
- `POST /api/gyms` - Create new gym
- `PUT /api/gyms/{gymId}` - Update gym
- `DELETE /api/gyms/{gymId}` - Delete gym

### Slot Management
- `GET /api/slots` - Get all slots
- `GET /api/slots/{slotId}` - Get slot by ID
- `GET /api/slots/gym/{gymId}` - Get slots by gym
- `POST /api/slots` - Create new slot
- `PUT /api/slots/{slotId}` - Update slot
- `DELETE /api/slots/{slotId}` - Delete slot

### Booking Management
- `GET /api/bookings` - Get all bookings
- `GET /api/bookings/{bookingId}` - Get booking by ID
- `GET /api/bookings/customer/{customerId}` - Get bookings by customer
- `GET /api/bookings/slot/{slotId}` - Get bookings by slot
- `GET /api/bookings/gym/{gymId}` - Get bookings by gym
- `POST /api/bookings` - Create new booking
- `PUT /api/bookings/{bookingId}` - Update booking
- `DELETE /api/bookings/{bookingId}` - Delete booking

### Notification Management
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/{notificationId}` - Get notification by ID
- `GET /api/notifications/customer/{customerId}` - Get notifications by customer
- `GET /api/notifications/customer/{customerId}/unread` - Get unread notifications
- `POST /api/notifications` - Create new notification
- `PUT /api/notifications/{notificationId}/read` - Mark notification as read
- `DELETE /api/notifications/{notificationId}` - Delete notification

## 🧪 Testing Guide

### 1. Health Check
```bash
curl http://localhost:8081/healthcheck
```

### 2. Authentication Tests

**Admin Login:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"identifier":"admin","password":"admin123","role":"ADMIN"}'
```

**Customer Login:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"identifier":"customer@example.com","password":"customer123","role":"CUSTOMER"}'
```

**Owner Login:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"identifier":"owner@example.com","password":"owner123","role":"OWNER"}'
```

### 3. Gym Management Tests

**Get All Gyms:**
```bash
curl http://localhost:8080/api/gyms
```

**Get Specific Gym:**
```bash
curl http://localhost:8080/api/gyms/GYM001
```

**Create New Gym:**
```bash
curl -X POST http://localhost:8080/api/gyms \
  -H "Content-Type: application/json" \
  -d '{"gymId":"GYM999","name":"Test Gym","location":"Test Location","contactNo":"1234567890","ownerId":"OWNER001","validated":false}'
```

### 4. Slot Management Tests

**Get All Slots:**
```bash
curl http://localhost:8080/api/slots
```

**Get Slots by Gym:**
```bash
curl http://localhost:8080/api/slots/gym/GYM001
```

**Create New Slot:**
```bash
curl -X POST http://localhost:8080/api/slots \
  -H "Content-Type: application/json" \
  -d '{"slotId":"SLOT888","gymId":"GYM001","totalCapacity":30,"startTime":"10:00:00","endTime":"11:00:00"}'
```

### 5. Booking Management Tests

**Get All Bookings:**
```bash
curl http://localhost:8080/api/bookings
```

**Get Bookings by Customer:**
```bash
curl http://localhost:8080/api/bookings/customer/CUST001
```

**Create New Booking:**
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"id":"BK777","bookingDate":"2026-02-01","statusId":1,"customerId":"CUST001","slotId":"SLOT001","gymId":"GYM001","slotStartTime":"06:00:00","slotEndTime":"08:00:00","paymentStatus":"PENDING"}'
```

### 6. Notification Management Tests

**Get All Notifications:**
```bash
curl http://localhost:8080/api/notifications
```

**Create New Notification:**
```bash
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -d '{"notificationId":"NOTIF888","customerId":"CUST001","message":"Welcome to FlipFit!","timestamp":"2026-02-01T06:00:00Z","read":false}'
```

**Mark Notification as Read:**
```bash
curl -X PUT http://localhost:8080/api/notifications/NOTIF888/read
```

## 🧪 Automated Testing

Run the comprehensive test script:
```bash
chmod +x test-api.sh
./test-api.sh
```

## 🛠️ Configuration

### Database Configuration

The application uses MySQL database. Configuration is in `config.yml`:

```yaml
database:
  driverClass: com.mysql.cj.jdbc.Driver
  url: jdbc:mysql://localhost:3306/flipfit_gym
  user: root
  password: Nitheez@iiit1
```

### Server Configuration

```yaml
server:
  applicationConnectors:
    - type: http
      port: 8080
  adminConnectors:
    - type: http
      port: 8081
```

## 🗄️ Database Setup

1. **Start MySQL service**
   ```bash
   brew services start mysql
   # or
   sudo systemctl start mysql
   ```

2. **Create database**
   ```bash
   mysql -u root -p'Nitheez@iiit1' -e "CREATE DATABASE IF NOT EXISTS flipfit_gym;"
   ```

3. **Verify connection**
   ```bash
   mysql -u root -p'Nitheez@iiit1' -e "USE flipfit_gym; SHOW TABLES;"
   ```

## 🛑 Stopping the Server

```bash
pkill -f "flipfit-gym-management"
```

Or press `Ctrl+C` in the server terminal window.

## 🔧 Troubleshooting

### Port Already in Use
```bash
lsof -i :8080
lsof -i :8081
pkill -f "flipfit-gym-management"
```

### Java Not Found
```bash
export PATH="/opt/homebrew/opt/openjdk@11/bin:$PATH"
java -version
```

### Database Connection Issues
```bash
mysql -u root -p'Nitheez@iiit1' -e "SHOW DATABASES;"
```

### Build Issues
```bash
mvn clean
mvn compile
mvn package
```

## 📊 Project Structure

```
src/
├── com/flipfit/
│   ├── FlipFitApplication.java      # Main application class
│   ├── configuration/              # Dropwizard configuration
│   ├── resources/                 # REST API resources
│   │   ├── UserResource.java
│   │   ├── GymResource.java
│   │   ├── BookingResource.java
│   │   ├── SlotResource.java
│   │   └── NotificationResource.java
│   ├── bean/                       # Entity classes
│   │   ├── GymCustomer.java
│   │   ├── GymOwner.java
│   │   ├── GymAdmin.java
│   │   ├── GymCenter.java
│   │   ├── Slot.java
│   │   ├── Booking.java
│   │   └── Notification.java
│   ├── dto/                        # Data Transfer Objects
│   ├── health/                     # Health checks
│   └── dao/                        # Data Access Objects
├── config.yml                      # Configuration file
├── pom.xml                         # Maven configuration
├── test-api.sh                     # Test script
└── README.md                       # This file
```

## 🛡️ Security

- Password-based authentication
- Role-based access control (Admin, Customer, Owner)
- Input validation
- SQL injection prevention through Hibernate

## 📈 Performance

- Connection pooling via HikariCP
- Efficient database queries with Hibernate
- JSON serialization with Jackson
- Asynchronous request handling

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For support and questions, please refer to the project documentation or create an issue.

---

**🎉 FlipFit Gym Management System REST API is ready to use!**
