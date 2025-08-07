# Patient Management System

A comprehensive microservices-based patient management system built with Spring Boot, featuring JWT authentication, gRPC communication, event-driven architecture with Apache Kafka, and cloud-native deployment options using AWS ECS and LocalStack.

## 🏗️ Architecture

This system follows a microservices architecture pattern with the following services:

<img width="1577" height="850" alt="image" src="https://github.com/user-attachments/assets/5f9d7805-b834-4734-b00a-a8b01c75d54b" />


## 🛠️ Technologies Used

### Backend Framework & Language
- **Java 21** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **Spring Cloud Gateway** - API Gateway

### Communication & Messaging
- **gRPC** - High-performance RPC between services
- **Apache Kafka** - Event streaming and messaging
- **Protocol Buffers (protobuf)** - Data serialization
- **REST APIs** - External communication

### Databases
- **PostgreSQL** - Primary database
- **H2** - In-memory database for testing

### Security
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password hashing

### Build & Deployment
- **Maven** - Build tool and dependency management
- **Docker & Docker Compose** - Containerization
- **AWS CDK** - Infrastructure as Code
- **AWS ECS Fargate** - Container orchestration
- **LocalStack** - Local AWS testing

### Documentation & Testing
- **OpenAPI/Swagger** - API documentation
- **JUnit 5** - Unit testing
- **REST Assured** - Integration testing

### Development Tools
- **Lombok** - Code generation
- **Spring Boot DevTools** - Development utilities

## 📋 Services Overview

### 1. API Gateway (`api-gateway`)
- **Port**: 4004
- **Purpose**: Single entry point for all client requests
- **Features**:
  - Route management and load balancing
  - JWT token validation
  - Request/response transformation
- **Technology**: Spring Cloud Gateway

### 2. Authentication Service (`auth-service`)
- **Port**: 4005
- **Purpose**: Handles user authentication and JWT token management
- **Database**: PostgreSQL (Port: 5001)
- **Features**:
  - User login and token generation
  - Token validation
  - Password encryption with BCrypt

### 3. Patient Service (`patient-service`)
- **Port**: 4000
- **Purpose**: Core business logic for patient management
- **Database**: PostgreSQL (Port: 5000)
- **Features**:
  - CRUD operations for patients
  - Integration with Billing Service via gRPC
  - Event publishing to Kafka
  - OpenAPI documentation

### 4. Billing Service (`billing-service`)
- **HTTP Port**: 4001
- **gRPC Port**: 9001
- **Purpose**: Manages billing accounts for patients
- **Features**:
  - gRPC server for billing account creation
  - Synchronous communication with Patient Service

### 5. Analytics Service (`analytics-service`)
- **Port**: 4002
- **Purpose**: Processes patient events for analytics
- **Features**:
  - Kafka consumer for patient events
  - Real-time event processing
  - Analytics data aggregation

## 🔌 API Endpoints

### Authentication Service (`/api/v1/auth`)
- **POST** `/login` - User login and token generation
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **GET** `/validate` - Token validation (Header: `Authorization: Bearer <token>`)

### Patient Service (`/api/v1/patients`)
> 🔒 **All endpoints require JWT authentication**

- **GET** `/` - Retrieve all patients
- **POST** `/` - Create a new patient
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "1990-01-01",
    "phoneNumber": "+1234567890"
  }
  ```
- **PUT** `/{id}` - Update an existing patient
- **DELETE** `/{id}` - Delete a patient

### Billing Service (gRPC)
- **CreatingBillingAccount** - Create billing account for a patient
  ```protobuf
  message BillingRequest {
    string patientId = 1;
    string name = 2;
    string email = 3;
  }
  ```

## 🚀 Deployment Options

### Option 1: Docker Compose (Recommended for Development)

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd patient-management
   ```

2. **Build all services**:
   ```bash
   # Build each service
   cd patient-service && mvn clean package -DskipTests && cd ..
   cd auth-service && mvn clean package -DskipTests && cd ..
   cd billing-service && mvn clean package -DskipTests && cd ..
   cd analytics-service && mvn clean package -DskipTests && cd ..
   cd api-gateway && mvn clean package -DskipTests && cd ..
   ```

3. **Start all services**:
   ```bash
   docker-compose up -d
   ```

4. **Verify services are running**:
   ```bash
   docker-compose ps
   ```

5. **Access services**:
   - API Gateway: http://localhost:4004
   - Patient Service Swagger: http://localhost:4000/swagger-ui.html
   - Auth Service Swagger: http://localhost:4005/swagger-ui.html

### Option 2: AWS Deployment with LocalStack

LocalStack allows you to test AWS services locally before deploying to the cloud.

1. **Prerequisites**:
   ```bash
   # Install AWS CLI
   pip install awscli-local
   
   # Install LocalStack
   pip install localstack
   ```

2. **Start LocalStack**:
   ```bash
   localstack start
   ```

3. **Deploy infrastructure**:
   ```bash
   cd infrastructure
   ./localstack-deploy.sh
   ```

4. **The deployment includes**:
   - ECS Fargate cluster
   - Application Load Balancer
   - RDS PostgreSQL databases
   - MSK (Managed Kafka) cluster
   - VPC with public/private subnets
   - Security groups and IAM roles

### Option 3: Production AWS Deployment

For production deployment, modify the CDK stack to use real AWS resources:

1. **Configure AWS credentials**:
   ```bash
   aws configure
   ```

2. **Deploy to AWS**:
   ```bash
   cd infrastructure
   cdk deploy
   ```

## 🧪 Testing

### Integration Tests
Run the integration tests to verify the system works end-to-end:

```bash
cd integration-tests
mvn test
```

### Manual Testing with cURL

1. **Login to get JWT token**:
   ```bash
   curl -X POST http://localhost:4004/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email": "testuser@test.com", "password": "password123"}'
   ```

2. **Create a patient**:
   ```bash
   curl -X POST http://localhost:4004/api/v1/patients \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <your-token>" \
     -d '{
       "name": "John Doe",
       "email": "john.doe@example.com",
       "dateOfBirth": "1990-01-01",
       "phoneNumber": "+1234567890"
     }'
   ```

3. **Get all patients**:
   ```bash
   curl -X GET http://localhost:4004/api/v1/patients \
     -H "Authorization: Bearer <your-token>"
   ```

## 🔧 Configuration

### Environment Variables

#### Docker Compose Configuration
All necessary environment variables are configured in `docker-compose.yaml`:

- **Database connections** (PostgreSQL)
- **Kafka bootstrap servers**
- **gRPC service addresses**
- **JWT secrets**
- **Service discovery**

#### Production Configuration
For production deployment, configure these environment variables:

- `JWT_SECRET` - Secret key for JWT token signing
- `SPRING_DATASOURCE_*` - Database connection details
- `SPRING_KAFKA_BOOTSTRAP_SERVERS` - Kafka cluster endpoints
- `BILLING_SERVICE_*` - Billing service gRPC connection details

## 📊 Monitoring and Observability

### Logging
- Structured logging with SLF4J
- Request/response logging in API Gateway
- gRPC call logging
- Kafka message processing logs

### Health Checks
Each service exposes health check endpoints:
- `/actuator/health` - Spring Boot Actuator health check

### Metrics
- Application metrics via Spring Boot Actuator
- Custom business metrics
- Kafka consumer lag monitoring

## 🚦 Service Ports

| Service | HTTP Port | gRPC Port | Database Port |
|---------|-----------|-----------|---------------|
| API Gateway | 4004 | - | - |
| Auth Service | 4005 | - | 5001 (PostgreSQL) |
| Patient Service | 4000 | - | 5000 (PostgreSQL) |
| Billing Service | 4001 | 9001 | - |
| Analytics Service | 4002 | - | - |
| Kafka | 9092/9094 | - | - |

## 🔄 Event Flow

1. **Patient Creation**:
   ```
   Client → API Gateway → Patient Service → Billing Service (gRPC)
                      ↓
                   Kafka → Analytics Service
   ```

2. **Authentication**:
   ```
   Client → API Gateway → Auth Service
          ← JWT Token ←
   ```

3. **Subsequent Requests**:
   ```
   Client → API Gateway (JWT Validation) → Target Service
   ```

## 🚨 Troubleshooting

### Common Issues

1. **Services not starting**:
   - Check Docker daemon is running
   - Verify ports are not in use
   - Check logs: `docker-compose logs <service-name>`

2. **Database connection issues**:
   - Wait for databases to fully initialize
   - Check connection strings and credentials

3. **Kafka connectivity issues**:
   - Ensure Kafka is fully started before dependent services
   - Check Kafka logs for broker issues

4. **gRPC communication failures**:
   - Verify billing service is running and accessible
   - Check network connectivity between services

### Useful Commands

```bash
# View all service logs
docker-compose logs -f

# Restart specific service
docker-compose restart patient-service

# Check service health
curl http://localhost:4000/actuator/health

# View Kafka topics
docker exec -it kafka kafka-topics.sh --list --bootstrap-server localhost:9092

# Scale services
docker-compose up -d --scale patient-service=2
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License.

## 📞 Support


For questions and support, please open an issue in the repository.
