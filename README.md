# 💰 Personal Expense Tracker

A Spring Boot web application for tracking personal expenses with JWT authentication and category management.

## 🚀 Features

- **User Authentication**: JWT-based secure login and registration
- **Expense Management**: Add, edit, delete, and view expenses
- **Category Management**: Organize expenses by custom categories
- **Dashboard**: Overview of spending patterns and statistics
- **Security**: Spring Security with JWT token authentication
- **Database**: MySQL for production, H2 for testing

## 🛠️ Tech Stack

- **Backend**: Spring Boot 4.0.6
- **Security**: Spring Security + JWT
- **Database**: MySQL (Production), H2 (Testing)
- **ORM**: Spring Data JPA + Hibernate
- **Build Tool**: Maven
- **Java Version**: 17
- **Testing**: JUnit 5, Spring Boot Test

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (for production)
- Git

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/personal-expense-tracker.git
cd personal-expense-tracker
```

### 2. Database Setup
```sql
-- Create MySQL database
CREATE DATABASE expense_tracker;
```

### 3. Configure Database
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. Build and Run
```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Or run the JAR directly
java -jar target/personal-expense-tracker-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## 🧪 Testing

### Run All Tests
```bash
./mvnw test
```

### Test Configuration
- Tests use **H2 in-memory database** (no MySQL required)
- Test configuration: `src/test/resources/application.properties`
- All tests run automatically in CI/CD pipeline

## 📊 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### Expenses
- `GET /api/expenses` - Get user expenses
- `POST /api/expenses` - Create new expense
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense

### Categories
- `GET /api/categories` - Get user categories
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### Dashboard
- `GET /api/dashboard` - Get dashboard statistics

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/jwt/project/
│   │   ├── config/          # Security & App configuration
│   │   ├── controller/      # REST API controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Exception handlers
│   │   ├── mapper/         # Entity-DTO mappers
│   │   ├── repository/     # Data repositories
│   │   ├── security/       # JWT & Security components
│   │   └── service/        # Business logic
│   └── resources/
│       └── application.properties
└── test/
    ├── java/               # Test classes
    └── resources/
        └── application.properties  # Test configuration (H2)
```

## 🚀 CI/CD Pipeline

This project includes a GitHub Actions CI/CD pipeline that:

- ✅ Runs on every push and pull request
- ✅ Sets up Java 17 environment
- ✅ Caches Maven dependencies for faster builds
- ✅ Runs all tests with H2 database
- ✅ Builds JAR artifact
- ✅ Uploads JAR for team download

### Pipeline Status
![CI/CD Pipeline](https://github.com/YOUR_USERNAME/personal-expense-tracker/workflows/Build%20and%20Test/badge.svg)

### Download Built JAR
1. Go to [Actions](https://github.com/YOUR_USERNAME/personal-expense-tracker/actions)
2. Click on latest successful workflow
3. Download `expense-tracker-jar` artifact