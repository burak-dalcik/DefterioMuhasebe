# Defterio - Ön Muhasebe Uygulaması

Spring Boot ile geliştirilmiş REST API tabanlı ön muhasebe uygulaması.

## Teknolojiler

- Java 17
- Spring Boot 3.2.0
- PostgreSQL
- Spring Security + JWT
- OpenAPI/Swagger
- Flyway Migration
- MapStruct
- Lombok

## Gereksinimler

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (PostgreSQL için)

## Kurulum

### 1. PostgreSQL'i Başlat

```bash
docker-compose up -d
```

### 2. Uygulamayı Çalıştır

```bash
mvn spring-boot:run
```

veya

```bash
mvn clean install
java -jar target/defterio-1.0.0.jar
```

## Endpointler

### Health Check
- `GET /api/v1/health` - Uygulama durumu

### Swagger UI
- `http://localhost:8080/swagger-ui.html` - API dokümantasyonu

## Yapılandırma

Uygulama ayarları `src/main/resources/application.yml` dosyasındadır.

### JWT Secret
Varsayılan olarak development secret kullanılır. Production için environment variable olarak ayarlayın:

```bash
export JWT_SECRET=your-secure-256-bit-secret-key
```

## Proje Yapısı

```
src/main/java/com/defterio/
├── controller/     # REST Controllers
├── service/        # Business Logic
├── repository/     # Data Access
├── entity/         # JPA Entities
├── dto/            # Data Transfer Objects
├── mapper/         # MapStruct Mappers
├── config/         # Configuration Classes
└── exception/      # Exception Handlers
```

## Geliştirme Planı

1. ✅ Proje iskeleti ve health check
2. ⏳ Auth (register/login + JWT)
3. ⏳ Contacts CRUD
4. ⏳ Categories CRUD
5. ⏳ Accounts CRUD
6. ⏳ Transactions + validasyon
7. ⏳ Attachments (upload/list/download)

## Lisans

Bu proje eğitim/öğrenme amaçlıdır.
