# Enterprise Platform - Backend

Production-grade Spring Boot microservices backend for the Enterprise Platform.

## Architecture

```
api-gateway/          → Request routing and authentication
├─ auth-service/     → OAuth2 and JWT authentication
├─ user-service/     → User management
├─ product-service/  → Product catalog
├─ order-service/    → Order processing
└─ shared-libs/      → Common utilities
```

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Redis 7+
- Docker (optional)

## Quick Start

```bash
# Build all services
./mvnw clean install

# Run API Gateway
cd api-gateway
./mvnw spring-boot:run

# Run Auth Service (new terminal)
cd auth-service
./mvnw spring-boot:run

# View API Documentation
http://localhost:8080/swagger-ui.html
```

## Services

### API Gateway
- **Port**: 8080
- **Purpose**: Request routing, rate limiting, authentication
- **Docs**: http://localhost:8080/swagger-ui.html

### Auth Service
- **Port**: 8081
- **Purpose**: OAuth2/JWT authentication, token management
- **Features**: Login, registration, token refresh

### User Service
- **Port**: 8082
- **Purpose**: User management
- **Features**: CRUD operations, profiles, roles

### Product Service
- **Port**: 8083
- **Purpose**: Product catalog management
- **Features**: Product CRUD, categories, search

### Order Service
- **Port**: 8084
- **Purpose**: Order processing
- **Features**: Order creation, status tracking, fulfillment

## Database

```bash
# Run PostgreSQL with Docker
docker run -d \
  --name postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=enterprise_db \
  -p 5432:5432 \
  postgres:14

# Run migrations
./mvnw flyway:migrate

# Create test data
./mvnw flyway:clean && ./mvnw flyway:migrate
```

## Testing

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# Run integration tests
./mvnw verify -P integration-tests
```

## Building Docker Images

```bash
# Build image
./mvnw clean package docker:build

# Run container
docker run -p 8080:8080 enterprise-platform:2.0.0
```

## Configuration

### Environment Variables

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/enterprise_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379
JWT_SECRET=your-secret-key
```

### Application Properties

Edit `application.yml` in each service for configuration.

## Features

- ✅ REST APIs with OpenAPI/Swagger documentation
- ✅ OAuth2 & JWT authentication
- ✅ Role-Based Access Control (RBAC)
- ✅ Database persistence (PostgreSQL)
- ✅ Caching (Redis)
- ✅ Logging and monitoring
- ✅ Error handling and validation
- ✅ API rate limiting
- ✅ Request/response tracking
- ✅ Health checks

## Development

### Project Structure

```
src/
├── main/
│   ├── java/com/enterprise/
│   │   ├── controller/    # REST endpoints
│   │   ├── service/       # Business logic
│   │   ├── repository/    # Data access
│   │   ├── model/         # Entity models
│   │   ├── config/        # Configuration
│   │   └── exception/     # Error handling
│   └── resources/
│       ├── application.yml
│       └── db/           # Migrations
├── test/
│   └── java/com/enterprise/
│       ├── controller/
│       ├── service/
│       └── repository/
└── docker/
    └── Dockerfile
```

### Code Style

- Follow Google Java Style Guide
- 4-space indentation
- Max line length: 120 characters
- Format: `./mvnw fmt:format`

### Naming Conventions

- Classes: PascalCase
- Methods/variables: camelCase
- Constants: UPPER_CASE
- Packages: lowercase
- Database tables: snake_case

## Deployment

### Local Development

```bash
# Start all services with Docker Compose
docker-compose up -d
```

### Kubernetes

```bash
# Deploy to K8s
kubectl apply -f kubernetes/

# Check deployment status
kubectl get deployments -n production
kubectl get pods -n production
```

### Cloud Deployment

- AWS EKS: See `infrastructure/terraform/aws`
- Azure AKS: See `infrastructure/terraform/azure`
- GCP GKE: See `infrastructure/terraform/gcp`

## Performance Tuning

### Database Optimization

```bash
# View slow queries
SELECT query, mean_time FROM pg_stat_statements 
ORDER BY mean_time DESC;

# Create indexes
CREATE INDEX idx_user_email ON users(email);
```

### JVM Tuning

```
JAVA_OPTS=-Xmx2g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

### Caching Strategy

- Application cache (Redis)
- Query result caching
- HTTP caching headers

## Monitoring

### Metrics Endpoints

- `/actuator/health` - System health
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

### Logs

- Console: Real-time application output
- File: `/var/log/enterprise-platform.log`
- Elasticsearch: Central log aggregation

## Troubleshooting

### Connection Issues

```bash
# Test database connection
psql -h localhost -U postgres -d enterprise_db

# Test Redis connection
redis-cli ping
```

### Build Problems

```bash
# Clean and rebuild
./mvnw clean install -X

# Check plugin versions
./mvnw versions:display-plugin-updates
```

### Runtime Issues

```bash
# Check logs
grep ERROR /var/log/enterprise-platform.log

# Monitor metrics
curl http://localhost:8080/actuator/metrics

# Check database connections
curl http://localhost:8080/actuator/health/db
```

## Security

- OAuth2 authentication
- JWT tokens
- RBAC authorization
- Input validation
- SQL injection prevention
- HTTPS/TLS encryption
- Secrets management

## Contributing

See [CONTRIBUTING.md](../CONTRIBUTING.md) for development guidelines.

## Support

- Documentation: [../docs](../docs)
- Issues: GitHub Issues
- Chat: GitHub Discussions

## License

See [LICENSE](../LICENSE) for license terms.
