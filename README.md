# FlipFit Gym Management System

A comprehensive Java-based gym management system that handles gym bookings, customer management, owner operations, and administrative functions with role-based access control.

##  Project Overview

FlipFit is a complete gym management solution that allows customers to book gym slots, gym owners to manage their centers, and administrators to oversee the entire system. The system features real-time notifications, waitlist management, and multi-role authentication.

##  Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Installation & Setup](#-installation--setup)
- [User Roles & Access](#-user-roles--access)
- [Core Functionality](#-core-functionality)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Configuration](#-configuration)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

##  Features

### Customer Features
- **Registration & Authentication**: Secure user registration and login system
- **Gym Center Discovery**: Browse gyms by city/location
- **Slot Booking**: Book available gym slots with real-time availability
- **Booking Management**: View, modify, and cancel bookings
- **Waitlist System**: Automatic promotion from waitlist when slots become available
- **Notifications**: Real-time updates for booking confirmations and waitlist changes
- **Profile Management**: Update personal information and preferences
- **Nearest Slot Finder**: Find closest available slots based on location

### Gym Owner Features
- **Center Registration**: Register and manage multiple gym centers
- **Slot Management**: Create and manage time slots with capacity limits
- **Booking Oversight**: View all bookings for their centers
- **Revenue Tracking**: Monitor booking statistics and revenue
- **Validation System**: Admin validation for gym operations

### Administrative Features
- **User Management**: Oversee all customer and owner accounts
- **Center Validation**: Approve/reject gym center registrations
- **System Monitoring**: Track system usage and statistics
- **Data Management**: Access and manage all system data

##  Architecture

### System Architecture
The system follows a **3-tier architecture** pattern:

1. **Presentation Layer** (Client)
   - User interfaces for different roles
   - Input validation and user experience
   - Menu-driven console interface

2. **Business Layer** (Services)
   - Core business logic implementation
   - Service interfaces and implementations
   - Transaction management

3. **Data Layer** (Repository)
   - In-memory data storage using Java Collections
   - Data access abstraction
   - Entity management

### Design Patterns Used
- **Interface Segregation Principle**: Separate interfaces for each service
- **Dependency Injection**: Loose coupling through interfaces
- **Repository Pattern**: DataStore abstracts data access
- **Factory Pattern**: Service creation in client classes
- **Observer Pattern**: Notification system for booking updates

##  Project Structure

```
src/com/flipfit/
├── bean/                    # Data Models (POJOs)
│   ├── Booking.java         # Booking entity with status tracking
│   ├── BookingStatus.java   # Booking status enumeration
│   ├── GymAdmin.java        # Admin user entity
│   ├── GymCenter.java       # Gym center information
│   ├── GymCustomer.java     # Customer user entity
│   ├── GymOwner.java        # Gym owner entity
│   ├── Notification.java    # Notification entity
│   └── Slot.java            # Time slot entity
├── business/                # Service Layer
│   ├── BookingImpl.java     # Booking service implementation
│   ├── BookingInterface.java
│   ├── GymAdminImpl.java    # Admin service implementation
│   ├── GymAdminInterface.java
│   ├── GymCenterImpl.java   # Gym center service
│   ├── GymCenterInterface.java
│   ├── GymCustomerImpl.java # Customer service
│   ├── GymCustomerInterface.java
│   ├── GymOwnerImpl.java    # Gym owner service
│   ├── GymOwnerInterface.java
│   ├── NotificationImpl.java # Notification service
│   ├── NotificationInterface.java
│   ├── SlotImpl.java        # Slot management service
│   ├── SlotInterface.java
│   ├── UserServiceImpl.java  # Authentication service
│   └── UserServiceInterface.java
├── client/                  # Presentation Layer
│   ├── AdminClient.java     # Admin user interface
│   ├── CustomerClient.java  # Customer user interface
│   ├── GymApplicationClient.java # Main application entry point
│   └── GymOwnerClient.java  # Gym owner interface
├── constants/               # Application Constants
│   ├── DemoDataConstants.java # Demo data for testing
│   ├── FormatConstants.java  # Date/time formats
│   ├── IdPrefixConstants.java # ID generation prefixes
│   ├── MessageConstants.java  # System messages
│   ├── RoleConstants.java     # User role definitions
│   └── ValidationConstants.java # Validation rules
├── data/                    # Data Layer
│   └── DataStore.java       # In-memory data repository
├── exception/               # Custom Exceptions
│   └── InvalidCredentialsException.java
└── validation/              # Input Validation
    ├── InputValidation.java # Validation utilities
    └── ValidationResult.java # Validation result wrapper
```

##  Installation & Setup

### Prerequisites
- **Java Development Kit (JDK)**: Version 8 or higher
- **IDE**: IntelliJ IDEA, Eclipse, or any Java IDE
- **Build Tool**: Maven or Gradle (optional)

### Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd JEDI_CHI_Developmeent_JAVA_POS
   ```

2. **Compile the Project**
   ```bash
   javac -cp src src/com/flipfit/client/GymApplicationClient.java
   ```

3. **Run the Application**
   ```bash
   java -cp src com.flipfit.client.GymApplicationClient
   ```

### Default Demo Credentials

The system comes with pre-configured demo data:

#### Admin Account
- **Username**: `admin1`
- **Password**: `admin123`
- **Role**: GymAdmin

#### Gym Owner Accounts
- **Email**: `owner1@flipfit.com`
- **Password**: `owner123`
- **Role**: GymOwner

#### Customer Accounts
- **Email**: `customer1@flipfit.com`
- **Password**: `customer123`
- **Role**: GymCustomer

## 👥 User Roles & Access

### 1. Gym Administrator
**Access Level**: Full system access
**Responsibilities**:
- Validate gym owner registrations
- Monitor system-wide bookings and statistics
- Manage user accounts
- Oversee gym center operations

**Menu Options**:
- View all gym centers
- Validate pending gym registrations
- View system statistics
- Manage user accounts

### 2. Gym Owner
**Access Level**: Center-specific management
**Responsibilities**:
- Register and manage gym centers
- Create and manage time slots
- Monitor bookings for their centers
- Track revenue and occupancy

**Menu Options**:
- Register new gym center
- Add/edit slots for centers
- View booking statistics
- Manage center information

### 3. Gym Customer
**Access Level**: Personal booking management
**Responsibilities**:
- Book gym slots
- Manage personal bookings
- View notifications
- Update profile information

**Menu Options**:
- View available gyms by city
- Book slots
- View/cancel bookings
- View notifications
- Update profile
- Find nearest available slots

## 🔧 Core Functionality

### Authentication System
- **Role-based Authentication**: Users select their role before login
- **Credential Validation**: Secure username/password verification
- **Session Management**: User session tracking during active use

### Booking System
- **Real-time Availability**: Live slot availability updates
- **Waitlist Management**: Automatic promotion when slots become available
- **Booking Status Tracking**: CONFIRMED, CANCELLED, WAITLISTED states
- **Date-based Booking**: Book slots for specific dates

### Notification System
- **Real-time Alerts**: Instant notifications for booking changes
- **Waitlist Promotion**: Automatic notifications when waitlisted bookings are confirmed
- **Read/Unread Tracking**: Track notification status
- **Customer-specific**: Notifications targeted to specific customers

### Data Management
- **In-memory Storage**: Java Collections-based data persistence
- **Demo Data**: Pre-populated with sample gyms, slots, and users
- **ID Generation**: Automatic unique ID generation for all entities
- **Data Validation**: Comprehensive input validation and error handling

##  API Documentation

### Service Interfaces

#### UserServiceInterface
```java
String authenticate(String identifier, String password);
String getLoggedInUserId();
```

#### BookingInterface
```java
Booking bookSlot(String customerId, String slotId, LocalDate date);
void cancelBooking(String bookingId);
List<Booking> getBookingsByCustomer(String customerId);
List<Booking> getBookingsByCustomerAndDay(String customerId, LocalDate date);
```

#### GymCenterInterface
```java
List<GymCenter> getCentersByCity(String city);
List<Slot> getAvailableSlots(String gymId);
Map<Slot, Integer> getSlotAvailabilityForDate(String gymId, LocalDate date);
```

#### NotificationInterface
```java
void sendNotification(String userId, String message);
void markAsRead(String notificationId);
```

### Data Models

#### Booking
- `id`: Unique booking identifier
- `bookingDate`: Date of booking
- `status`: Current booking status
- `customerId`: Associated customer
- `slotId`: Booked slot
- `gymId`: Gym center ID

#### GymCenter
- `gymId`: Unique center identifier
- `name`: Center name
- `location`: Geographic location
- `ownerId`: Owner identifier
- `validated`: Admin validation status
- `slotList`: Available time slots

#### Notification
- `notificationId`: Unique notification ID
- `customerId`: Target customer
- `message`: Notification content
- `timestamp`: Creation time
- `read`: Read status flag

##  Database Schema

The system uses an **in-memory data store** with the following structure:

### Core Entities
- **ADMINS**: Map<String, GymAdmin>
- **OWNERS**: Map<String, GymOwner>
- **CUSTOMERS**: Map<String, GymCustomer>
- **CENTERS**: Map<String, GymCenter>
- **GYM_SLOTS**: Map<String, List<Slot>>
- **BOOKINGS**: List<Booking>
- **NOTIFICATIONS**: List<Notification>
- **SLOT_IDS**: Set<String>

### Relationships
- GymCenter 1 → * Slot
- GymCenter 1 → 1 GymOwner
- Booking 1 → 1 GymCustomer
- Booking 1 → 1 Slot
- Notification 1 → 1 GymCustomer

##  Configuration

### System Constants
Located in `/constants/` package:

#### FormatConstants
```java
DATE_PATTERN = "yyyy-MM-dd"
TIME_PATTERN = "HH:mm"
```

#### RoleConstants
```java
ADMIN = "ADMIN"
OWNER = "OWNER"
CUSTOMER = "CUSTOMER"
```

#### IdPrefixConstants
```java
BOOKING_PREFIX = "BK"
NOTIFICATION_PREFIX = "NT"
```

### Validation Rules
- Email format validation
- Phone number format validation
- Required field validation
- Role selection validation

##  Testing

### Demo Data
The system includes comprehensive demo data:
- 3 gym centers in Bangalore
- Multiple time slots per center
- Sample customer and owner accounts
- Pre-configured admin account

### Manual Testing Steps

1. **Test Authentication**
   - Login with different roles
   - Test invalid credentials
   - Verify role-based access

2. **Test Booking Flow**
   - Browse available centers
   - Book a slot
   - Verify booking confirmation
   - Test waitlist functionality

3. **Test Notifications**
   - Trigger booking notifications
   - Verify waitlist promotion notifications
   - Test notification read status

4. **Test Admin Functions**
   - Validate gym centers
   - View system statistics
   - Manage user accounts

