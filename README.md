# Leave Management System - MVP

A comprehensive Leave Management System built for startups with 50+ employees, providing APIs for employee management, leave applications, approvals, and balance tracking.

# Features

-Employee Management: Add employees with validation
- Leave Application: Apply for leaves with comprehensive validation
- Leave Approval/Rejection: HR can approve or reject leave requests
- Leave Balance Tracking: Real-time leave balance calculation
- Comprehensive Validation: Handles all edge cases mentioned in requirements

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │    Database     │
│                 │    │                 │    │                 │
│  - Web App      │────│  Spring Boot    │────│   PostgreSQL    │
│  - Mobile App   │    │  - REST APIs    │    │   - employees   │
│  - Admin Panel  │    │  - Services     │    │   - leave_req   │
│                 │    │  - Controllers  │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## API Endpoints

### Employee Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/employees` | Add new employee |
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| GET | `/api/employees/{id}/leave-balance` | Get leave balance |

### Leave Management

| Method | Endpoint | Description |
|--------|----------|------------|
| POST | '/api/leaves' | Apply for leave |
| PUT | '/api/leaves/{id}/approve` | Approve leave |
| PUT | `/api/leaves/{id}/reject` | Reject leave |
| GET | `/api/leaves` | Get all leave requests |
| GET | `/api/leaves/employee/{id}` | Get leaves by employee |

## 🛠️ Setup Instructions

### Prerequisites
- Java 17+
- Maven 
- SQL 

### Local Development Setup

1. Clone the repository
bash-
git clone <repository-url>
cd leave-management-system
`

2. Database Setup
```sql
-- Create database
CREATE DATABASE leave_management_db;

-- Update application.properties with your DB credentials
spring.datasource.url=jdbc:postgresql://localhost:5432/leave_management_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build and Run
```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run

4. **Access the API
- Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html` (if configured)

##  API Testing Examples

### 1. Add Employee
```bash
curl -X POST http://localhost:8080/api/employees \
-H "Content-Type: application/json" \
-d '{
  "name": "Ankit",
  "email": "xyz@gmail.com",
  "department": "ENGINEERING",
  "joiningDate": "2024-01-15"
}'
```

Response:
```json
{
  "id": 1,
  "name": " Divya",
  "email": "divya@xyz.com",
  "department": "ENGINEERING",
  "joiningDate": "2024-01-15",
  "annualLeaveBalance": 30
}
```

### 2. Apply for Leave
```bash
curl -X POST http://localhost:8080/api/leaves \
-H "Content-Type: application/json" \
-d '{
  "employeeId": 1,
  "startDate": "2024-12-25",
  "endDate": "2024-12-27",
  "reason": "Christmas vacation"
}'
```

Response:
```json
{
  "id": 1,
  "employeeId": 1,
  "startDate": "2024-12-25",
  "endDate": "2024-12-27",
  "days": 3,
  "reason": "Christmas vacation",
  "status": "PENDING",
  "createdAt": "2024-08-12T10:30:00"
}
```

### 3. Approve Leave
```bash
curl -X PUT http://localhost:8080/api/leaves/1/approve
```

### 4. Get Leave Balance
```bash
curl http://localhost:8080/api/employees/1/leave-balance
```

Response:
```json
{
  "employeeId": 1,
  "employeeName": "John Doe",
  "totalLeaveBalance": 30,
  "usedLeaves": 3,
  "remainingBalance": 27
}
```

## Edge Cases Handled

###  Implemented Validations

1. Employee Management
   - Duplicate email prevention
   - Future joining date validation
   - Email format validation
   - Required field validation

2. Leave Application
   - Start date after end date
   - Leave before joining date
   - Past date leave application
   - Insufficient leave balance
   - Overlapping approved leaves

3. System Validations
   - Employee not found
   - Leave request not found
   - Already processed leave requests
   - Positive days validation

### 🔄 Additional Edge Cases Considered

1. **Business Logic**
   - Weekend/Holiday calculation (can be enhanced)
   - Probation period restrictions
   - Department-specific leave policies
   - Carry-forward leave balance
   - Emergency leave applications
     
2. Data Integrity
   - Concurrent leave applications
   - Leave cancellation after approval
   - Employee resignation with pending leaves
   - Bulk leave operations

3. Security & Performance
   - API rate limiting
   - Role-based access control
   - Database connection pooling

##  Scaling Strategy (50 → 500 employees)

### Current Architecture Limitations
- Single database instance
- Monolithic application
- In-memory session management
- No caching layer

### Scaling Recommendations

#### 1. Database Scaling
```
Current: Single PostgreSQL
→ Master-Slave Replication
→ Connection Pooling (HikariCP)
→ Database Partitioning by year/department
```

#### 2. Application Scaling
```
Current: Single Spring Boot instance
→ Load Balancer (Nginx/AWS ALB)
→ Multiple app instances
→ Microservices (Employee Service, Leave Service)
```

#### 3. Caching Strategy
```
→ Redis for session management
→ Cache leave balances
→ Cache employee data
→ Database query result caching
```

#### 4. Infrastructure
```
→ Docker containers
→ Kubernetes orchestration
→ Auto-scaling policies
→ Health checks and monitoring
```

### Estimated Scaling Timeline
- 100 employees: Add connection pooling, Redis cache
- 250 employees: Implement read replicas, load balancer
- 500 employees: Microservices, container orchestration

## 🏗️ High-Level System Design

### Component Interaction Flow

```
┌─────────────────┐
│   Frontend      │
│   (React/Vue)   │
└─────────┬───────┘
          │ HTTP/HTTPS
          ▼
┌─────────────────┐
│  Load Balancer  │
│   (Nginx/ALB)   │
└─────────┬───────┘
          │
          ▼
┌─────────────────┐    ┌─────────────────┐
│  Spring Boot    │    │     Redis       │
│  Application    │────│   (Caching)     │
│  (Multiple      │    │                 │
│   Instances)    │    └─────────────────┘
└─────────┬───────┘
          │
          ▼
┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │   File Storage  │
│   (Master)      │    │   (AWS S3)      │
└─────────┬───────┘    └─────────────────┘
          │
          ▼
┌─────────────────┐
│   PostgreSQL    │
│   (Read Replica)│
└─────────────────┘
```

### Database Schema Design

```sql
-- Employees Table
employees (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  department VARCHAR(100) NOT NULL,
  joining_date DATE NOT NULL,
  annual_leave_balance INTEGER DEFAULT 30,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

-- Leave Requests Table
leave_requests (
  id BIGINT PRIMARY KEY,
  employee_id BIGINT REFERENCES employees(id),
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  days INTEGER NOT NULL,
  reason TEXT,
  status VARCHAR(20) DEFAULT 'PENDING',
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

-- Indexes for Performance
CREATE INDEX idx_employee_email ON employees(email);
CREATE INDEX idx_leave_employee_id ON leave_requests(employee_id);
CREATE INDEX idx_leave_dates ON leave_requests(start_date, end_date);
CREATE INDEX idx_leave_status ON leave_requests(status);
```

##  Deployment Options

### Option 1: Heroku


### Option 2: Render


## 🔮 Potential Improvements

### Phase 1 (Immediate)
-  Role-based access control (HR, Manager, Employee)
-  Email notifications for leave status
-Leave calendar view
-  Bulk leave operations

### Phase 2 (3-6 months)
 Mobile application
-  Advanced reporting and analytics
  Integration with payroll systems
  Multi-tenant support

### Phase 3 (6-12 months)
-  AI-powered leave predictio
-  Advanced approval workflows
-  Performance monitoring dashboard

## 🤝 Assumptions Made

1. Leave Policy
   - 30 days annual leave for all employees
   - Calendar days calculation (not working days)
   - No different leave types (sick, casual, etc.)

2. Business Rules
   - All employees have same leave entitlement
   - No probation period restrictions
   - No carry-forward policy implemented

3. Technical
   - Single time zone operation
   - No authentication/authorization for MVP
   - Simple leave day calculation
   - No integration with HR systems

## 🐛 Known Issues & TODOs# Leave Management System - MVP

A comprehensive Leave Management System built for startups with 50+ employees, providing APIs for employee management, leave applications, approvals, and balance tracking.

## 🚀 Features

- **Employee Management**: Add employees with validation
- **Leave Application**: Apply for leaves with comprehensive validation
- **Leave Approval/Rejection**: HR can approve or reject leave requests
- **Leave Balance Tracking**: Real-time leave balance calculation
- **Comprehensive Validation**: Handles all edge cases mentioned in requirements

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │    Database     │
│                 │    │                 │    │                 │
│  - Web App      │────│  Spring Boot    │────│   PostgreSQL    │
│  - Mobile App   │    │  - REST APIs    │    │   - employees   │
│  - Admin Panel  │    │  - Services     │    │   - leave_req   │
│                 │    │  - Controllers  │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📋 API Endpoints

### Employee Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/employees` | Add new employee |
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| GET | `/api/employees/{id}/leave-balance` | Get leave balance |

### Leave Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/leaves` | Apply for leave |
| PUT | `/api/leaves/{id}/approve` | Approve leave |
| PUT | `/api/leaves/{id}/reject` | Reject leave |
| GET | `/api/leaves` | Get all leave requests |
| GET | `/api/leaves/employee/{id}` | Get leaves by employee |

## 🛠️ Setup Instructions

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Local Development Setup

1. Clone the repository
```bash
git clone <repository-url>
cd leave-management-system
```

2. Database Setup
```sql
-- Create database
CREATE DATABASE leave_management_db;

-- Update application.properties with your DB credentials
spring.datasource.url=jdbc:postgresql://localhost:5432/leave_management_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build and Run
```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

4. Access the API
- Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html` (if configured)

## 🧪 API Testing Examples

### 1. Add Employee
```bash
curl -X POST http://localhost:8080/api/employees \
-H "Content-Type: application/json" \
-d '{
  "name": "John Doe",
  "email": "john.doe@company.com",
  "department": "ENGINEERING",
  "joiningDate": "2024-01-15"
}'
```

Response:
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@company.com",
  "department": "ENGINEERING",
  "joiningDate": "2024-01-15",
  "annualLeaveBalance": 30
}
```

### 2. Apply for Leave
```bash
curl -X POST http://localhost:8080/api/leaves \
-H "Content-Type: application/json" \
-d '{
  "employeeId": 1,
  "startDate": "2024-12-25",
  "endDate": "2024-12-27",
  "reason": "Christmas vacation"
}'
```

Response:
```json
{
  "id": 1,
  "employeeId": 1,
  "startDate": "2024-12-25",
  "endDate": "2024-12-27",
  "days": 3,
  "reason": "Christmas vacation",
  "status": "PENDING",
  "createdAt": "2024-08-12T10:30:00"
}
```

### 3. Approve Leave
```bash
curl -X PUT http://localhost:8080/api/leaves/1/approve
```

### 4. Get Leave Balance
```bash
curl http://localhost:8080/api/employees/1/leave-balance
```

Response:
```json
{
  "employeeId": 1,
  "employeeName": "John Doe",
  "totalLeaveBalance": 30,
  "usedLeaves": 3,
  "remainingBalance": 27
}
```

## 🎯 Edge Cases Handled

### ✅ Implemented Validations
1. Employe Managemen
   - Duplicate email prevention
   - Future joining date validation
   - Email format validation
   - Required field validation

2. Leave Application
   - Start date after end date
   - Leave before joining date
   - Past date leave application
   - Future date limit (6 months)
   - Insufficient leave balance
   - Overlapping approved leaves

3. System Validations
   - Employee not found
   - Leave request not found
   - Already processed leave requests
   - Positive days validation

### 🔄 Additional Edge Cases Considered

1. Business Logic
   - Weekend/Holiday calculation (can be enhanced)
   - Probation period restrictions
   - Department-specific leave policies
   - Carry-forward leave balance
   - Emergency leave applications

2. Data Integrity
   - Concurrent leave applications
   - Leave cancellation after approval
   - Employee resignation with pending leaves
   - Bulk leave operations

3. Security & Performance
   - API rate limiting
   - Role-based access control
   - Audit trails
   - Database connection pooling

## 🚀 Scaling Strategy (50 → 500 employees)

### Current Architecture Limitations
- Single database instance
- Monolithic application
- In-memory session management
- No caching layer

### Scaling Recommendations

#### 1. Database Scaling
```
Current: Single PostgreSQL
→ Master-Slave Replication
→ Connection Pooling (HikariCP)
→ Database Partitioning by year/department
```

#### 2. Application Scaling
```
Current: Single Spring Boot instance
→ Load Balancer (Nginx/AWS ALB)
→ Multiple app instances
→ Microservices (Employee Service, Leave Service)
```

#### 3. Caching Strategy
```
→ Redis for session management
→ Cache leave balances
→ Cache employee data
→ Database query result caching
```

#### 4. Infrastructure
```
→ Docker containers
→ Kubernetes orchestration
→ Auto-scaling policies
→ Health checks and monitoring
```

### Estimated Scaling Timeline
- 100 employees: Add connection pooling, Redis cache
- 250 employees: Implement read replicas, load balancer
- 500 employees: Microservices, container orchestration

## 🏗️ High-Level System Design

### Component Interaction Flow

```
┌─────────────────┐
│   Frontend      │
│   (React/Vue)   │
└─────────┬───────┘
          │ HTTP/HTTPS
          ▼
┌─────────────────┐
│  Load Balancer  │
│   (Nginx/ALB)   │
└─────────┬───────┘
          │
          ▼
┌─────────────────┐    ┌─────────────────┐
│  Spring Boot    │    │     Redis       │
│  Application    │────│   (Caching)     │
│  (Multiple      │    │                 │
│   Instances)    │    └─────────────────┘
└─────────┬───────┘
          │
          ▼
┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │   File Storage  │
│   (Master)      │    │   (AWS S3)      │
└─────────┬───────┘    └─────────────────┘
          │
          ▼
┌─────────────────┐
│   PostgreSQL    │
│   (Read Replica)│
└─────────────────┘
```

### Database Schema Design

```sql
-- Employees Table
employees (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  department VARCHAR(100) NOT NULL,
  joining_date DATE NOT NULL,
  annual_leave_balance INTEGER DEFAULT 30,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

-- Leave Requests Table
leave_requests (
  id BIGINT PRIMARY KEY,
  employee_id BIGINT REFERENCES employees(id),
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  days INTEGER NOT NULL,
  reason TEXT,
  status VARCHAR(20) DEFAULT 'PENDING',
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

-- Indexes for Performance
CREATE INDEX idx_employee_email ON employees(email);
CREATE INDEX idx_leave_employee_id ON leave_requests(employee_id);
CREATE INDEX idx_leave_dates ON leave_requests(start_date, end_date);
CREATE INDEX idx_leave_status ON leave_requests(status);
```

## 🚀 Deployment Options

### Option 1: Heroku (Free Tier)
```bash
# Install Heroku CLI
heroku create leave-management-app
heroku addons:create heroku-postgresql:hobby-dev
git push heroku main
```

### Option 2: Render
- Connect GitHub repository
- Auto-deploy on push
- Free PostgreSQL database

### Option 3: Railway
- One-click deploy
- Built-in PostgreSQL
- Automatic HTTPS

## 🔮 Potential Improvements

### Phase 1 (Immediate)
- Role-based access control (HR, Manager, Employee)
-  Email notifications for leave status
-  Leave calendar view
Bulk leave operations

### Phase 2 (3-6 months)
-  Mobile application
-  Advanced reporting and analytics
-  Integration with payroll systems
-  Mlti-tenant support

### Phase 3 (6-12 months)
-  AI-powered leave prediction
-Slack/Teams integration
-  Advanced approval workflows
-  Performance monitoring dashboard

## 🤝 Assumptions Made

1. Leave Policy
   - 30 days annual leave for all employees
   - Calendar days calculation (not working days)
   - No different leave types (sick, casual, etc.)

2. Business Rules
   - All employees have same leave entitlement
   - No probation period restrictions
   - No carry-forward policy implemented
3. Technical
   - Single time zone operation
   - No authentication/authorization for MVP
   - Simple leave day calculation
   - No integration with HR systems

## 🐛 Known Issues & TODOs

-  Weekend exclusion in leave day calculation
-  Time zone handling for global teams
-  Advanced leave approval workflows
-  Integration tests for edge cases
-  API documentation with OpenAPI

## 📞 Support

For any issues or questions, please create an issue in the repository or contact the development team.

---

Note: This is an MVP version. For production use, additional security, monitoring, and compliance features should be implemented.

- [ ] Weekend exclusion in leave day calculation
- [ ] Time zone handling for global teams
- [ ] Advanced leave approval workflows
- [ ] Integration tests for edge cases
- [ ] API documentation with OpenAPI

## 📞 Support

For any issues or questions, please create an issue in the repository or contact the development team.

---

**Note**: This is an MVP version. For production use, additional security, monitoring, and compliance features should be implemented.

