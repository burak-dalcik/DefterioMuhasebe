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

### Authentication
- `POST /api/v1/auth/register` - Kullanıcı kaydı
- `POST /api/v1/auth/login` - Giriş yap
- `GET /api/v1/me` - Mevcut kullanıcı bilgisi

### Contacts
- `GET /api/v1/contacts` - Tüm cari kayıtları listele
- `POST /api/v1/contacts` - Yeni cari kayıt oluştur
- `GET /api/v1/contacts/{id}` - Cari kayıt detayı
- `PUT /api/v1/contacts/{id}` - Cari kayıt güncelle
- `DELETE /api/v1/contacts/{id}` - Cari kayıt sil

### Categories
- `GET /api/v1/categories` - Tüm kategorileri listele
- `POST /api/v1/categories` - Yeni kategori oluştur
- `PUT /api/v1/categories/{id}` - Kategori güncelle
- `DELETE /api/v1/categories/{id}` - Kategori sil

### Accounts
- `GET /api/v1/accounts` - Tüm hesapları listele
- `POST /api/v1/accounts` - Yeni hesap oluştur
- `PUT /api/v1/accounts/{id}` - Hesap güncelle
- `DELETE /api/v1/accounts/{id}` - Hesap sil

### Swagger UI
- `http://localhost:8080/swagger-ui.html` - API dokümantasyonu

## API Örnekleri

### Register
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "Password123"
  }'
```

Response:
```json
{
  "data": {
    "id": 1,
    "email": "user@example.com",
    "role": "USER"
  }
}
```

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "Password123"
  }'
```

Response:
```json
{
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400
  }
}
```

### Create Contact
```bash
curl -X POST http://localhost:8080/api/v1/contacts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "type": "SUPPLIER",
    "name": "ABC Tedarik A.Ş.",
    "taxNo": "1234567890",
    "phone": "+90 555 123 4567",
    "email": "info@abctedarik.com",
    "address": "İstanbul, Türkiye"
  }'
```

### Create Category
```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "type": "EXPENSE",
    "name": "Kira"
  }'
```

### Create Account
```bash
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "type": "BANK",
    "name": "Ziraat Bankası",
    "currency": "TRY"
  }'
```

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
2. ✅ Auth (register/login + JWT)
3. ✅ Contacts CRUD
4. ✅ Categories CRUD
5. ✅ Accounts CRUD
6. ⏳ Transactions + validasyon
7. ⏳ Attachments (upload/list/download)

## Lisans

Bu proje eğitim/öğrenme amaçlıdır.
