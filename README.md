# ToDo API

A comprehensive ToDo management REST API built with Spring Boot, featuring JWT authentication, user management, and advanced filtering capabilities.

## 🚀 Features

- **JWT Authentication** - Secure token-based authentication
- **User Management** - Registration, login with role-based access
- **ToDo CRUD Operations** - Create, read, update, delete todo items
- **Advanced Filtering** - Filter by status, priority, completion, and search
- **Pagination & Sorting** - Efficient data retrieval with pagination
- **Rate Limiting** - 100 requests per minute per user
- **API Documentation** - Swagger/OpenAPI 3.0 documentation
- **Environment-Specific Config** - Dev/Prod configurations
- **Validation** - Comprehensive input validation
- **Optimistic Locking** - Prevent concurrent modification conflicts

## 📋 Prerequisites

- Java 17 or later
- PostgreSQL 12 or later
- Maven 3.6 or later

## 🛠️ Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ToDoApi
   ```

2. **Database Setup**
   ```sql
   CREATE DATABASE todoapp;
   CREATE USER todo_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE todoapp TO todo_user;
   ```

3. **Environment Configuration**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

4. **Build and Run**
   ```bash
   mvn clean package
   java -jar target/ToDoApi-0.0.1-SNAPSHOT.jar
   ```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active profile | `dev` |
| `SERVER_PORT` | Server port | `8080` |
| `DB_URL` | Database URL | `jdbc:postgresql://localhost:5432/todoapp` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `JWT_SECRET` | JWT secret key | `mySecretKey...` |
| `JWT_EXPIRATION` | JWT expiration (ms) | `86400000` |
| `LOG_LEVEL` | Logging level | `INFO` |

### Profiles

- **dev**: Development with debug logging and SQL queries
- **prod**: Production with optimized settings
- **test**: Testing with H2 in-memory database

## 📚 API Documentation

### Swagger UI

Access the interactive API documentation at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

### Authentication

All protected endpoints require a JWT token in the `Authorization` header:
```
Authorization: Bearer <your-jwt-token>
```

### API Endpoints

#### Authentication (`/auth`)
- `POST /auth/login` - User login
- `POST /auth/register` - User registration

#### ToDo Management (`/api/todo`)
- `GET /api/todo` - Get all todos (with filtering and pagination)
- `POST /api/todo` - Create new todo
- `GET /api/todo/{id}` - Get todo by ID
- `PUT /api/todo/{id}` - Update todo
- `PATCH /api/todo/{id}` - Partial update todo
- `DELETE /api/todo/{id}` - Delete todo

### Query Parameters (GET /api/todo)

| Parameter | Type | Description |
|-----------|------|-------------|
| `search` | String | Search in title and description |
| `status` | Enum | `PENDING`, `IN_PROGRESS`, `COMPLETED` |
| `priority` | Enum | `LOW`, `MEDIUM`, `HIGH` |
| `completed` | Boolean | Filter by completion status |
| `page` | Integer | Page number (default: 0) |
| `size` | Integer | Page size (default: 10) |
| `sortBy` | String | Sort field (default: `createdAt`) |
| `direction` | String | Sort direction: `asc` or `desc` (default: `desc`) |

## 🔐 Security Features

- **JWT Authentication**: Stateless token-based auth
- **Password Encryption**: BCrypt hashing
- **Rate Limiting**: 100 requests/minute per user
- **Role-Based Access**: USER and ADMIN roles
- **Input Validation**: Comprehensive request validation
- **CORS Protection**: Configurable cross-origin policies

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
- Unit tests for service layer
- Integration tests for controllers
- Security tests for authentication
- Validation tests for input constraints

## 📦 Project Structure

```
src/main/java/com/application/todoapi/
├── controller/          # REST controllers
├── service/           # Business logic
├── repository/        # Data access layer
├── entity/           # JPA entities
├── security/          # Security components
│   ├── jwt/          # JWT utilities
│   ├── config/       # Security configuration
│   └── ratelimit/   # Rate limiting
├── common/           # DTOs and mappers
│   ├── request/      # Request DTOs
│   ├── response/     # Response DTOs
│   └── mapper/       # Entity-DTO mapping
├── exception/        # Exception handling
└── config/           # Application configuration
```

## 🚀 Deployment

### Docker (Optional)
```bash
# Build
docker build -t todo-api .

# Run
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://db:5432/todoapp \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=password \
  todo-api
```

### Production Considerations
- Use environment variables for sensitive data
- Enable HTTPS in production
- Configure proper database connection pooling
- Set up monitoring and logging
- Use a production-grade JWT secret

## 📊 Monitoring

### Health Checks
- `GET /actuator/health` - Application health
- `GET /actuator/info` - Application information

### Rate Limiting
Rate limiting is applied per user (100 requests/minute). Exceeded limits return HTTP 429.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the API documentation at `/swagger-ui.html`
- Review the logs for detailed error information
