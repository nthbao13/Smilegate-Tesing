# Game Management System

Một hệ thống quản lý game đa ngôn ngữ được xây dựng bằng Spring Boot, cho phép tạo, chỉnh sửa, tìm kiếm và quản lý các game với hỗ trợ nhiều ngôn ngữ.

## 🚀 Tính năng chính

- **Quản lý Game**: Tạo mới, chỉnh sửa, xóa và tìm kiếm games
- **Hỗ trợ đa ngôn ngữ**: Mỗi game có thể có tên bằng nhiều ngôn ngữ khác nhau
- **Phân loại Game**: Quản lý games theo categories (ACTION, PUZZLE, RPG, SPORTS, RACING, ADVENTURE)
- **Tìm kiếm và lọc**: Tìm kiếm theo tên, mã game và lọc theo category
- **Bulk Operations**: Chọn và xóa nhiều games cùng lúc
- **Giao diện thân thiện**: UI hiện đại với Bootstrap và Font Awesome
- **Validation**: Kiểm tra dữ liệu đầu vào nghiêm ngặt
- **Pagination**: Phân trang kết quả tìm kiếm

## 🛠 Công nghệ sử dụng

### Backend
- **Spring Boot** 3.x
- **Spring Data JPA** - ORM và database operations
- **Spring Web MVC** - Web framework
- **Spring Validation** - Input validation
- **Thymeleaf** - Template engine
- **MySQL** - Database
- **Lombok** - Giảm boilerplate code

### Frontend
- **Bootstrap 5.3.0** - CSS framework
- **Font Awesome 6.0.0** - Icons
- **JavaScript ES6+** - Client-side logic
- **HTML5 & CSS3** - Markup và styling

## 📋 Yêu cầu hệ thống

- **Java 17** hoặc cao hơn
- **Maven 3.6+**
- **MySQL 8.0+**

## 🔧 Cài đặt và chạy

### 1. Clone repository
```bash
git clone <repository-url>
cd game-management-system
```

### 2. Cấu hình database
Tạo database MySQL:
```sql
CREATE DATABASE gaming;
```

Cập nhật thông tin kết nối database trong `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gaming
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=create-drop
```

### 3. Build và chạy ứng dụng
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Truy cập ứng dụng
Mở trình duyệt và truy cập: `http://localhost:8080/games`

## 📊 Cấu trúc Database

### Bảng chính

#### `game`
- `game_id` (PK): ID duy nhất của game
- `game_code`: Mã game (unique, tối thiểu 5 ký tự)
- `category_id` (FK): ID của category
- `create_at`, `update_at`: Timestamps

#### `game_name`
- `name_id` (PK): ID duy nhất của tên game
- `game_id` (FK): ID của game
- `game_name`: Tên game bằng ngôn ngữ cụ thể
- `language_id` (FK): Mã ngôn ngữ
- `is_default`: Đánh dấu ngôn ngữ mặc định

#### `category`
- `category_id` (PK): ID duy nhất của category
- `category_name`: Tên category

#### `language`
- `language_id` (PK): Mã ngôn ngữ (EN, KO, JA)
- `language_name`: Tên đầy đủ của ngôn ngữ

## 🗂 Cấu trúc thư mục

```
src/
├── main/
│   ├── java/com/example/game/
│   │   ├── controller/          # Controllers
│   │   ├── dto/                # Data Transfer Objects
│   │   │   ├── request/        # Request DTOs
│   │   │   └── response/       # Response DTOs
│   │   ├── entity/             # JPA Entities
│   │   ├── exception/          # Custom Exceptions
│   │   ├── repository/         # JPA Repositories
│   │   ├── service/            # Business Logic
│   │   └── constant/           # Constants
│   └── resources/
│       ├── static/             # Static resources
│       │   ├── css/           # Stylesheets
│       │   └── js/            # JavaScript files
│       ├── templates/          # Thymeleaf templates
│       ├── data.sql           # Sample data
│       └── schema.sql         # Database schema
```

## 🎯 API Endpoints

### Game Management
- `GET /games` - Danh sách games (với pagination và filter)
- `GET /games/register` - Form đăng ký game mới
- `POST /games/register` - Tạo game mới
- `GET /games/{id}/edit` - Form chỉnh sửa game
- `POST /games/{id}/edit` - Cập nhật game
- `POST /games/bulk-delete` - Xóa nhiều games

### Parameters
- `page`: Số trang (mặc định: 1)
- `keyword`: Từ khóa tìm kiếm
- `category`: ID của category để lọc

## 🔍 Tính năng chi tiết

### 1. Quản lý Game
- **Tạo mới**: Form validation nghiêm ngặt, hỗ trợ nhiều ngôn ngữ
- **Chỉnh sửa**: Optimistic locking để tránh xung đột
- **Xóa**: Bulk delete với confirmation modal

### 2. Tìm kiếm và lọc
- Tìm kiếm theo tên game (tất cả ngôn ngữ)
- Lọc theo category
- URL-friendly parameters
- Pagination với navigation

### 3. Đa ngôn ngữ
- Hỗ trợ English (EN), Korean (KO), Japanese (JA)
- Mỗi game phải có ít nhất một ngôn ngữ
- Chỉ định ngôn ngữ mặc định
- Hiển thị tên theo ngôn ngữ mặc định

### 4. Giao diện người dùng
- Responsive design
- Modern UI với gradients và animations
- Toast notifications
- Form validation real-time
- Bulk selection với checkboxes

## ⚙ Cấu hình

### Constants
- `PAGE_LIMIT = 10`: Số items trên mỗi trang
- `THRESH_HOLD_DETECT_LANGUAGE = 0.5`: Ngưỡng detect language (chưa sử dụng)

### Error Handling
- Global exception handler
- Custom error page
- Validation error messages
- Business logic exceptions

## 🧪 Testing

Chạy tests:
```bash
mvn test
```

## 🔒 Validation Rules

### Game Code
- Bắt buộc
- Tối thiểu 5 ký tự
- Unique trong hệ thống

### Category
- Bắt buộc phải chọn

### Game Names
- Ít nhất một ngôn ngữ
- Mỗi ngôn ngữ chỉ được chọn một lần
- Phải có một ngôn ngữ mặc định

## 🚦 Status Codes

- `200 OK`: Thành công
- `400 Bad Request`: Validation error
- `404 Not Found`: Resource không tồn tại
- `409 Conflict`: Game đã được cập nhật (optimistic locking)
- `500 Internal Server Error`: Lỗi server

## 📞 Support

Nếu bạn gặp vấn đề hoặc có câu hỏi, vui lòng tạo issue trong repository này.