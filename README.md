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

### Transactions
- `GET /api/v1/transactions` - Tüm hareketleri listele (filtreli)
- `POST /api/v1/transactions` - Yeni hareket oluştur
- `GET /api/v1/transactions/{id}` - Hareket detayı
- `PUT /api/v1/transactions/{id}` - Hareket güncelle
- `DELETE /api/v1/transactions/{id}` - Hareket sil

### Attachments
- `POST /api/v1/transactions/{id}/attachments` - Transaction'a dosya ekle
- `GET /api/v1/transactions/{id}/attachments` - Transaction'ın dosyalarını listele
- `GET /api/v1/attachments/{id}/download` - Dosya indir
- `DELETE /api/v1/attachments/{id}` - Dosya sil

### Reports
- `GET /api/v1/reports/summary` - Özet rapor (gelir, gider, net)
- `GET /api/v1/reports/by-category` - Kategori bazında kırılım
- `GET /api/v1/reports/monthly` - Aylık trend raporu

### Purchases
- `GET /api/v1/purchases` - Tüm alımları listele (filtreli)
- `POST /api/v1/purchases` - Yeni alım oluştur
- `GET /api/v1/purchases/{id}` - Alım detayı
- `PUT /api/v1/purchases/{id}` - Alım güncelle
- `DELETE /api/v1/purchases/{id}` - Alım sil
- `POST /api/v1/purchases/{id}/attachments` - Alıma dosya ekle
- `GET /api/v1/purchases/{id}/attachments` - Alımın dosyalarını listele

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

### Create Transaction
```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "type": "EXPENSE",
    "subtype": "PURCHASE",
    "amount": 1250.50,
    "currency": "TRY",
    "date": "2026-01-27",
    "description": "Ofis malzemeleri",
    "categoryId": 3,
    "accountId": 1,
    "contactId": 10
  }'
```

### Get Transactions with Filters
```bash
curl -X GET "http://localhost:8080/api/v1/transactions?from=2026-01-01&to=2026-01-31&type=EXPENSE&subtype=PURCHASE&page=0&size=20" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Upload Attachment
```bash
curl -X POST http://localhost:8080/api/v1/transactions/1/attachments \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@/path/to/document.pdf"
```

Response:
```json
{
  "data": {
    "id": 1,
    "ownerType": "TRANSACTION",
    "ownerId": 1,
    "fileName": "document.pdf",
    "contentType": "application/pdf",
    "size": 245678,
    "uploadedAt": "2026-01-27T10:30:00Z",
    "uploadedBy": "user@example.com"
  }
}
```

### Download Attachment
```bash
curl -X GET http://localhost:8080/api/v1/attachments/1/download \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -o downloaded_file.pdf
```

### List Attachments for Transaction
```bash
curl -X GET http://localhost:8080/api/v1/transactions/1/attachments \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Get Summary Report
```bash
curl -X GET "http://localhost:8080/api/v1/reports/summary?from=2026-01-01&to=2026-01-31&currency=TRY" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

Response:
```json
{
  "data": {
    "from": "2026-01-01",
    "to": "2026-01-31",
    "currency": "TRY",
    "totalIncome": 10000.00,
    "totalExpense": 6500.00,
    "net": 3500.00
  }
}
```

### Get Report by Category
```bash
curl -X GET "http://localhost:8080/api/v1/reports/by-category?from=2026-01-01&to=2026-01-31&type=EXPENSE&currency=TRY" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

Response:
```json
{
  "data": [
    {
      "categoryId": 3,
      "categoryName": "Ofis",
      "total": 1200.00
    },
    {
      "categoryId": 5,
      "categoryName": "Kira",
      "total": 3000.00
    }
  ]
}
```

### Get Monthly Trend Report
```bash
curl -X GET "http://localhost:8080/api/v1/reports/monthly?year=2026&type=EXPENSE&currency=TRY" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

Response:
```json
{
  "data": [
    {
      "month": "2026-01",
      "total": 6500.00
    },
    {
      "month": "2026-02",
      "total": 1200.00
    }
  ]
}
```

### Create Purchase
```bash
curl -X POST http://localhost:8080/api/v1/purchases \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "supplierId": 10,
    "accountId": 1,
    "date": "2026-01-27",
    "currency": "TRY",
    "note": "Kırtasiye alımı",
    "lines": [
      {
        "description": "A4 Kağıt",
        "quantity": 10,
        "unitPrice": 85.50,
        "categoryId": 3
      },
      {
        "description": "Toner",
        "quantity": 2,
        "unitPrice": 450.00,
        "categoryId": 3
      }
    ]
  }'
```

Response:
```json
{
  "data": {
    "id": 1,
    "supplierId": 10,
    "supplierName": "ABC Tedarik A.Ş.",
    "accountId": 1,
    "accountName": "Ziraat Bankası",
    "date": "2026-01-27",
    "currency": "TRY",
    "note": "Kırtasiye alımı",
    "total": 1755.00,
    "transactionId": 5,
    "lines": [
      {
        "id": 1,
        "description": "A4 Kağıt",
        "quantity": 10,
        "unitPrice": 85.50,
        "lineTotal": 855.00,
        "categoryId": 3,
        "categoryName": "Ofis"
      },
      {
        "id": 2,
        "description": "Toner",
        "quantity": 2,
        "unitPrice": 450.00,
        "lineTotal": 900.00,
        "categoryId": 3,
        "categoryName": "Ofis"
      }
    ],
    "createdAt": "2026-01-27T10:30:00",
    "updatedAt": "2026-01-27T10:30:00"
  }
}
```

### Upload Attachment to Purchase
```bash
curl -X POST http://localhost:8080/api/v1/purchases/1/attachments \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@/path/to/invoice.pdf"
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
6. ✅ Transactions + validasyon
7. ✅ Attachments (upload/list/download)
8. ✅ Reports (summary, by-category, monthly)
9. ✅ Purchases (multi-line purchase with auto transaction)

## Lisans

Bu proje eğitim/öğrenme amaçlıdır.
