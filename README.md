# 📚 Bookish Bliss Haven - Android Bookstore Application

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java-007396?logo=java&logoColor=white)
![Room](https://img.shields.io/badge/Database-Room-4285F4?logo=android&logoColor=white)
![API](https://img.shields.io/badge/Min%20SDK-24-brightgreen)
![Version](https://img.shields.io/badge/Version-1.0-blue)

## 📖 Giới thiệu

**Bookish Bliss Haven** là một ứng dụng bán sách Android hiện đại với giao diện thân thiện và đầy đủ tính năng quản lý. Ứng dụng hỗ trợ hai chế độ: **Khách hàng** và **Quản trị viên**, mang đến trải nghiệm mua sắm sách trực tuyến hoàn chỉnh.

### ✨ Tính năng nổi bật

#### 👥 Dành cho Khách hàng
- 🔐 Đăng ký, đăng nhập và quản lý tài khoản
- 📚 Duyệt và tìm kiếm sách theo danh mục
- 🔍 Xem chi tiết sách với mô tả đầy đủ
- 🛒 Thêm sách vào giỏ hàng và quản lý giỏ hàng
- 💳 Đặt hàng với nhiều phương thức thanh toán
- 📦 Theo dõi đơn hàng và lịch sử mua hàng
- 📍 Quản lý địa chỉ giao hàng
- ⭐ Đánh giá và nhận xét sách
- ❤️ Yêu thích và xem sách đã xem gần đây

#### 👨‍💼 Dành cho Quản trị viên
- 📊 Dashboard tổng quan hệ thống
- 📖 Quản lý sách (CRUD - Thêm, Sửa, Xóa)
- 👥 Quản lý khách hàng
- 📦 Quản lý đơn hàng (xác nhận, giao hàng, hủy)
- 📈 Thống kê doanh thu và báo cáo

---

## 🏗️ Kiến trúc ứng dụng

### 📐 Sơ đồ tổng quan hệ thống

```
┌─────────────────────────────────────────────────────────────────┐
│                     BOOKISH BLISS HAVEN                         │
│                    Android Application                          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │
        ┌─────────────────────┴─────────────────────┐
        │                                             │
        ▼                                             ▼
┌──────────────┐                            ┌──────────────┐
│  MainActivity│                            │AdminActivity │
│              │                            │              │
│  (Customer)  │                            │   (Admin)    │
└──────┬───────┘                            └──────┬───────┘
       │                                            │
       │                                            │
       ├─ Navigation Controller                    ├─ Navigation Controller
       │                                            │
       ├─ Bottom Navigation                        ├─ Navigation Graph
       │                                            │
       └─ Fragments:                                └─ Fragments:
          • HomeFragment                               • AdminDashboardFragment
          • CatalogFragment                            • AdminProductsFragment
          • CartFragment                               • AdminCustomersFragment
          • ProfileFragment                            • AdminOrdersFragment
          • BookDetailFragment                         • AdminEditProductFragment
          • CheckoutFragment
          • OrdersFragment
          • SearchFragment
          • FavoritesFragment
          etc...
```

### 🔄 Luồng hoạt động chính

```
┌──────────────┐
│   App Start  │
└──────┬───────┘
       │
       ▼
┌──────────────────────┐
│  Database Init       │
│  Load Sample Data    │
└──────┬───────────────┘
       │
       ▼
┌──────────────────────┐     YES    ┌──────────────────┐
│  User Logged In?     ├──────────→ │  MainActivity    │
└──────┬───────────────┘            │  (Customer View) │
       │ NO                          └──────────────────┘
       ▼
┌──────────────────────┐
│  LoginFragment       │
└──────┬───────────────┘
       │
       ├─── Login Success ───→ Check Role
       │                            │
       │                            ├─ Customer → MainActivity
       │                            └─ Admin → AdminActivity
       │
       └─── Register ────→ RegisterFragment
```

---

## 🗄️ Cấu trúc Database (Room)

### 📊 Sơ đồ ERD (Entity Relationship Diagram)

```
┌─────────────────────┐
│       USERS         │
├─────────────────────┤
│ PK  id             │◄────────────┐
│     username (UK)  │             │
│     password       │             │
│     fullName       │             │
│     email          │             │
│     phone          │             │
│     isAdmin        │             │
│     isActive       │             │
│     createdAt      │             │
└─────────────────────┘             │
         │                          │
         │ 1                        │
         │                          │
         │                          │ N
         │ N                ┌───────┴──────────┐
         └──────────────────┤     ORDERS       │
                            ├──────────────────┤
┌─────────────────────┐     │ PK  id          │
│      BOOKS          │     │ FK  userId      │
├─────────────────────┤     │ FK  addressId   │
│ PK  id             │◄──┐ │     orderNumber │
│     title          │   │ │     totalAmount │
│     author         │   │ │     status      │
│     publisher      │   │ │     paymentMethod│
│     publishYear    │   │ │     shippingAddr│
│     category       │   │ │     recipientName│
│     language       │   │ │     recipientPhone│
│     description    │   │ │     note        │
│     price          │   │ │     cancelReason│
│     stock          │   │ │     createdAt   │
│     imageUrl       │   │ │     updatedAt   │
│     isbn           │   │ └─────────────────┘
│     pages          │   │          │
│     isActive       │   │          │ 1
└─────────────────────┘   │          │
         │                │          │
         │ 1              │          │ N
         │                │  ┌───────┴──────────┐
         │ N              │  │   ORDER_ITEMS    │
         └────────────────┼──┤──────────────────┤
                          │  │ PK  id          │
┌─────────────────────┐   │  │ FK  orderId     │
│      CART           │   │  │ FK  bookId      │
├─────────────────────┤   │  │     quantity    │
│ PK  id             │   │  │     price       │
│ FK  userId         │   │  │     subtotal    │
│ FK  bookId         ├───┘  └─────────────────┘
│     quantity       │
│     addedAt        │
└─────────────────────┘
         ▲
         │ N
         │
         │ 1
┌─────────────────────┐
│     ADDRESSES       │
├─────────────────────┤
│ PK  id             │
│ FK  userId         │
│     recipientName  │
│     phone          │
│     address        │
│     district       │
│     city           │
│     isDefault      │
│     createdAt      │
└─────────────────────┘
```

### 📋 Chi tiết các bảng

#### 1. **USERS** - Quản lý người dùng
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| id | INTEGER (PK) | ID tự động tăng |
| username | TEXT (UNIQUE) | Tên đăng nhập |
| password | TEXT | Mật khẩu đã mã hóa |
| fullName | TEXT | Họ và tên |
| email | TEXT | Email |
| phone | TEXT | Số điện thoại |
| isAdmin | BOOLEAN | Phân quyền admin |
| isActive | BOOLEAN | Trạng thái hoạt động |
| createdAt | LONG | Thời gian tạo |

#### 2. **BOOKS** - Quản lý sách
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| id | INTEGER (PK) | ID tự động tăng |
| title | TEXT | Tên sách |
| author | TEXT | Tác giả |
| publisher | TEXT | Nhà xuất bản |
| publishYear | TEXT | Năm xuất bản |
| category | TEXT | Danh mục |
| language | TEXT | Ngôn ngữ |
| description | TEXT | Mô tả |
| price | REAL | Giá bán |
| stock | INTEGER | Số lượng tồn kho |
| imageUrl | TEXT | URL hình ảnh |
| isbn | TEXT | Mã ISBN |
| pages | INTEGER | Số trang |
| isActive | BOOLEAN | Trạng thái |

#### 3. **ORDERS** - Quản lý đơn hàng
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| id | INTEGER (PK) | ID tự động tăng |
| userId | INTEGER (FK) | ID người dùng |
| addressId | INTEGER (FK) | ID địa chỉ |
| orderNumber | TEXT | Mã đơn hàng |
| totalAmount | REAL | Tổng tiền |
| status | TEXT | Trạng thái: PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED |
| paymentMethod | TEXT | Phương thức thanh toán |
| shippingAddress | TEXT | Địa chỉ giao hàng |
| recipientName | TEXT | Tên người nhận |
| recipientPhone | TEXT | SĐT người nhận |
| note | TEXT | Ghi chú |
| cancelReason | TEXT | Lý do hủy |
| createdAt | LONG | Thời gian tạo |
| updatedAt | LONG | Thời gian cập nhật |

#### 4. **ORDER_ITEMS** - Chi tiết đơn hàng
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| id | INTEGER (PK) | ID tự động tăng |
| orderId | INTEGER (FK) | ID đơn hàng |
| bookId | INTEGER (FK) | ID sách |
| quantity | INTEGER | Số lượng |
| price | REAL | Giá |
| subtotal | REAL | Thành tiền |

#### 5. **CART** - Giỏ hàng
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| id | INTEGER (PK) | ID tự động tăng |
| userId | INTEGER (FK) | ID người dùng |
| bookId | INTEGER (FK) | ID sách |
| quantity | INTEGER | Số lượng |
| addedAt | LONG | Thời gian thêm |

#### 6. **ADDRESSES** - Địa chỉ giao hàng
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| id | INTEGER (PK) | ID tự động tăng |
| userId | INTEGER (FK) | ID người dùng |
| recipientName | TEXT | Tên người nhận |
| phone | TEXT | Số điện thoại |
| address | TEXT | Địa chỉ |
| district | TEXT | Quận/huyện |
| city | TEXT | Thành phố |
| isDefault | BOOLEAN | Địa chỉ mặc định |
| createdAt | LONG | Thời gian tạo |

---

## 🔐 Luồng xác thực (Authentication Flow)

```
┌────────────────┐
│  User Access   │
└────────┬───────┘
         │
         ▼
    ┌─────────┐      NO      ┌──────────────────┐
    │ Logged? ├─────────────→│  LoginFragment   │
    └────┬────┘               └────────┬─────────┘
         │ YES                         │
         │                             ▼
         │                  ┌─────────────────────┐
         │                  │  Check Credentials  │
         │                  │  (DatabaseHelper)   │
         │                  └──────────┬──────────┘
         │                             │
         │                    ┌────────┴────────┐
         │                    │                 │
         │                    ▼                 ▼
         │            ┌──────────┐      ┌──────────┐
         │            │  Valid   │      │ Invalid  │
         │            └─────┬────┘      └─────┬────┘
         │                  │                  │
         │                  │                  ▼
         │                  │          Show Error Message
         │                  │
         │                  ▼
         │          ┌──────────────┐
         │          │ Save Session │
         │          │ SharedPrefs  │
         │          └──────┬───────┘
         │                 │
         └─────────────────┘
                 │
                 ▼
         ┌──────────────┐
         │  Check Role  │
         └──────┬───────┘
                │
        ┌───────┴────────┐
        │                │
        ▼                ▼
  ┌──────────┐    ┌─────────────┐
  │ Customer │    │    Admin    │
  │  (Main)  │    │  (Admin)    │
  └──────────┘    └─────────────┘
```

---

## 🛒 Luồng mua hàng (Shopping Flow)

```
┌─────────────────┐
│  Browse Books   │
│  (Home/Catalog) │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Select Book     │
│ (BookDetail)    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Add to Cart     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  CartFragment   │
│  • View items   │
│  • Update qty   │
│  • Remove items │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Checkout        │
└────────┬────────┘
         │
         ├─→ Select/Add Address
         ├─→ Choose Payment Method
         └─→ Add Notes (optional)
         │
         ▼
┌─────────────────┐
│ Confirm Order   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Create Order    │
│ • Clear Cart    │
│ • Update Stock  │
│ • Save Order    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Order Success   │
│ (OrdersFragment)│
└─────────────────┘
         │
         ▼
┌─────────────────┐
│ Track Order     │
│ • PENDING       │
│ • CONFIRMED     │
│ • SHIPPING      │
│ • DELIVERED     │
│ • CANCELLED     │
└─────────────────┘
```

---

## 📦 Cấu trúc thư mục dự án

```
pj/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/bookstore/
│   │   │   │   ├── adapters/              # RecyclerView Adapters
│   │   │   │   │   ├── BookAdapter.java
│   │   │   │   │   ├── CartAdapter.java
│   │   │   │   │   ├── OrderAdapter.java
│   │   │   │   │   ├── AdminProductAdapter.java
│   │   │   │   │   └── ...
│   │   │   │   │
│   │   │   │   ├── database/              # Room Database
│   │   │   │   │   ├── AppDatabase.java   # Database instance
│   │   │   │   │   ├── DatabaseHelper.java # Init & utilities
│   │   │   │   │   ├── entities/          # Room Entities
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   ├── Book.java
│   │   │   │   │   │   ├── Order.java
│   │   │   │   │   │   ├── OrderItem.java
│   │   │   │   │   │   ├── Cart.java
│   │   │   │   │   │   └── Address.java
│   │   │   │   │   └── dao/               # Data Access Objects
│   │   │   │   │       ├── UserDao.java
│   │   │   │   │       ├── BookDao.java
│   │   │   │   │       ├── OrderDao.java
│   │   │   │   │       ├── OrderItemDao.java
│   │   │   │   │       ├── CartDao.java
│   │   │   │   │       └── AddressDao.java
│   │   │   │   │
│   │   │   │   ├── models/                # POJO Models
│   │   │   │   │   ├── Book.java
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── Order.java
│   │   │   │   │   ├── Cart.java
│   │   │   │   │   └── ...
│   │   │   │   │
│   │   │   │   ├── ui/                    # UI Components
│   │   │   │   │   ├── fragments/         # All Fragments
│   │   │   │   │   │   ├── HomeFragment.java
│   │   │   │   │   │   ├── CatalogFragment.java
│   │   │   │   │   │   ├── CartFragment.java
│   │   │   │   │   │   ├── ProfileFragment.java
│   │   │   │   │   │   ├── LoginFragment.java
│   │   │   │   │   │   ├── RegisterFragment.java
│   │   │   │   │   │   ├── BookDetailFragment.java
│   │   │   │   │   │   ├── CheckoutFragment.java
│   │   │   │   │   │   ├── OrdersFragment.java
│   │   │   │   │   │   ├── SearchFragment.java
│   │   │   │   │   │   ├── AdminDashboardFragment.java
│   │   │   │   │   │   ├── AdminProductsFragment.java
│   │   │   │   │   │   ├── AdminCustomersFragment.java
│   │   │   │   │   │   ├── AdminOrdersFragment.java
│   │   │   │   │   │   └── ...
│   │   │   │   │   └── adapters/          # UI-specific adapters
│   │   │   │   │
│   │   │   │   ├── utils/                 # Utility classes
│   │   │   │   │   └── OrderManager.java
│   │   │   │   │
│   │   │   │   ├── MainActivity.java      # Customer main activity
│   │   │   │   └── AdminActivity.java     # Admin main activity
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── layout/                # XML layouts
│   │   │   │   ├── drawable/              # Images & icons
│   │   │   │   ├── values/                # Strings, colors, styles
│   │   │   │   ├── navigation/            # Navigation graphs
│   │   │   │   └── menu/                  # Menu files
│   │   │   │
│   │   │   ├── assets/                    # Books CSV data
│   │   │   │   └── books_full_9xx.csv
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   ├── androidTest/                   # Android tests
│   │   └── test/                          # Unit tests
│   │
│   ├── build.gradle.kts                   # App-level Gradle
│   └── google-services.json               # Firebase config (if any)
│
├── gradle/
│   ├── libs.versions.toml                 # Version catalog
│   └── wrapper/
│
├── build.gradle.kts                       # Project-level Gradle
├── settings.gradle.kts
├── gradle.properties
├── local.properties
├── install_and_clear.bat                  # Installation script
├── verify_database.bat                    # DB verification script
└── README.md                              # This file
```

---

## 🛠️ Công nghệ sử dụng

### 📱 Core Technologies

| Công nghệ | Phiên bản | Mô tả |
|-----------|-----------|-------|
| **Android SDK** | 24 - 36 | Nền tảng Android |
| **Java** | 11 | Ngôn ngữ lập trình |
| **Gradle** | 8.x | Build system |
| **Material Design** | Latest | UI/UX components |

### 📚 Thư viện chính

#### Database
- **Room** - ORM cho SQLite
  ```kotlin
  implementation("androidx.room:room-runtime:2.x.x")
  annotationProcessor("androidx.room:room-compiler:2.x.x")
  ```

#### UI/Navigation
- **Navigation Component** - Quản lý navigation
  ```kotlin
  implementation("androidx.navigation:navigation-fragment:2.7.6")
  implementation("androidx.navigation:navigation-ui:2.7.6")
  ```

- **RecyclerView** - Hiển thị danh sách
  ```kotlin
  implementation("androidx.recyclerview:recyclerview:1.3.2")
  ```

- **CardView** - Card UI
  ```kotlin
  implementation("androidx.cardview:cardview:1.0.0")
  ```

#### Networking
- **Retrofit** - REST API client
  ```kotlin
  implementation("com.squareup.retrofit2:retrofit:2.10.0")
  implementation("com.squareup.retrofit2:converter-gson:2.10.0")
  ```

- **OkHttp** - HTTP client
  ```kotlin
  implementation("com.squareup.okhttp3:okhttp:4.11.0")
  ```

#### Image Loading
- **Glide** - Image loading & caching
  ```kotlin
  implementation("com.github.bumptech.glide:glide:4.16.0")
  annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
  ```

#### Data Processing
- **Gson** - JSON parser
  ```kotlin
  implementation("com.google.code.gson:gson:2.10.1")
  ```

#### Security
- **Security-Crypto** - Encryption
  ```kotlin
  implementation("androidx.security:security-crypto:1.1.0-alpha06")
  ```

---

## 🚀 Hướng dẫn cài đặt

### 📋 Yêu cầu hệ thống

- **Android Studio**: Flamingo (2022.2.1) trở lên
- **JDK**: 11 trở lên
- **Android SDK**: API 24 (Android 7.0) trở lên
- **Gradle**: 8.0+

### 📥 Cài đặt

1. **Clone repository**
   ```bash
   git clone <repository-url>
   cd bt_adr/pj
   ```

2. **Mở project trong Android Studio**
   - File → Open → Chọn thư mục `pj`

3. **Sync Gradle**
   - Android Studio sẽ tự động sync Gradle files
   - Nếu không, chọn: File → Sync Project with Gradle Files

4. **Build project**
   ```bash
   # Windows
   gradlew build
   
   # Mac/Linux
   ./gradlew build
   ```

5. **Chạy ứng dụng**
   - Kết nối thiết bị Android hoặc khởi động emulator
   - Click nút Run (▶️) trong Android Studio
   - Hoặc sử dụng script:
   ```bash
   # Windows
   install_and_clear.bat
   ```

### 🔑 Tài khoản mặc định

#### Admin
- **Username**: `admin`
- **Password**: `admin`

#### Khách hàng mẫu
- **Username**: `demo@bookstore.com`
- **Password**: `demo123`

---

## 📱 Hướng dẫn sử dụng

### 👤 Dành cho Khách hàng

1. **Đăng ký/Đăng nhập**
   - Mở ứng dụng → Đăng nhập hoặc Đăng ký tài khoản mới

2. **Duyệt sách**
   - Tab Home: Xem sách nổi bật
   - Tab Catalog: Xem tất cả sách, lọc theo danh mục
   - Search: Tìm kiếm sách theo tên, tác giả

3. **Mua sách**
   - Chọn sách → Xem chi tiết → Thêm vào giỏ
   - Tab Cart → Kiểm tra giỏ hàng → Thanh toán
   - Nhập thông tin giao hàng → Đặt hàng

4. **Quản lý đơn hàng**
   - Tab Profile → My Orders
   - Xem chi tiết, theo dõi trạng thái
   - Hủy đơn hàng nếu cần

### 👨‍💼 Dành cho Admin

1. **Đăng nhập Admin**
   - Đăng nhập với tài khoản admin
   - Tự động chuyển sang AdminActivity

2. **Quản lý sách**
   - Products → Xem danh sách sách
   - Thêm/Sửa/Xóa sách
   - Quản lý tồn kho

3. **Quản lý đơn hàng**
   - Orders → Xem tất cả đơn hàng
   - Xác nhận/Giao hàng/Hủy đơn
   - Xem chi tiết đơn hàng

4. **Quản lý khách hàng**
   - Customers → Xem danh sách khách hàng
   - Xem chi tiết thông tin
   - Kích hoạt/Vô hiệu hóa tài khoản

---

## 🎨 Screenshots & UI Flow

### Màn hình chính
```
┌─────────────────────────────────┐
│  🏠 Home                         │
│  ┌──────────────────────────┐   │
│  │   Featured Books         │   │
│  │  ┌────┐ ┌────┐ ┌────┐   │   │
│  │  │📖  │ │📖  │ │📖  │   │   │
│  │  └────┘ └────┘ └────┘   │   │
│  └──────────────────────────┘   │
│                                  │
│  Categories: Fiction, Science... │
└─────────────────────────────────┘
│ 🏠 Home | 📚 Catalog | 🛒 Cart | 👤 Profile
```

### Flow tương tác
```
Home Screen
    │
    ├─→ Search Books
    │       └─→ Search Results → Book Detail
    │
    ├─→ Browse Categories
    │       └─→ Category Books → Book Detail
    │
    ├─→ Featured Books
    │       └─→ Book Detail
    │               ├─→ Add to Cart
    │               ├─→ Add to Favorites
    │               └─→ View Reviews
    │
    ├─→ Cart
    │       └─→ Checkout
    │               └─→ Order Success
    │
    └─→ Profile
            ├─→ My Orders
            ├─→ Addresses
            ├─→ Favorites
            └─→ Settings
```

---

## 🧪 Testing

### Unit Tests
```bash
# Windows
gradlew test

# Mac/Linux
./gradlew test
```

### Android Tests
```bash
# Windows
gradlew connectedAndroidTest

# Mac/Linux
./gradlew connectedAndroidTest
```

### Kiểm tra Database
```bash
# Windows
verify_database.bat
```

---

## 📊 Dữ liệu mẫu

Ứng dụng đi kèm với:
- 53+ sách tiếng Việt trong `books_full_9xx.csv`
- 2 tài khoản người dùng (admin, customer)
- Đơn hàng mẫu
- Địa chỉ giao hàng mẫu

Database sẽ được khởi tạo tự động khi lần đầu chạy app.

---

## 🔒 Bảo mật

### Các biện pháp bảo mật
1. **Mã hóa mật khẩu**: Sử dụng Security-Crypto
2. **Session Management**: SharedPreferences an toàn
3. **Input Validation**: Kiểm tra dữ liệu đầu vào
4. **SQL Injection Prevention**: Sử dụng Room ORM
5. **Permission Control**: Phân quyền Admin/User

---

## 🐛 Troubleshooting

### Lỗi thường gặp

1. **Gradle sync failed**
   - Kiểm tra kết nối internet
   - File → Invalidate Caches / Restart

2. **Database version conflict**
   - Xóa app và cài đặt lại
   - Hoặc chạy `install_and_clear.bat`

3. **Build error**
   - Clean project: Build → Clean Project
   - Rebuild: Build → Rebuild Project

4. **Emulator not starting**
   - Kiểm tra HAXM/Virtualization
   - Tạo lại AVD mới

---

## 🚧 Roadmap

### Version 1.1 (Planned)
- [ ] Tích hợp thanh toán online
- [ ] Thông báo push notifications
- [ ] Chat hỗ trợ khách hàng
- [ ] Wishlist nâng cao
- [ ] Social sharing

### Version 1.2 (Future)
- [ ] Machine Learning recommendations
- [ ] AR book preview
- [ ] Multiple language support
- [ ] Dark mode
- [ ] Offline mode

---

## 👥 Đóng góp

Chúng tôi hoan nghênh mọi đóng góp! Vui lòng:

1. Fork repository
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

---

## 📄 License

Dự án này được cấp phép theo [MIT License](LICENSE).

---

## 📞 Liên hệ

- **Email**: support@bookishbliss.com
- **Website**: https://bookishbliss.com
- **GitHub**: https://github.com/yourusername/bookish-bliss-haven

---

## 🙏 Acknowledgments

- [Material Design Icons](https://material.io/icons)
- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Glide](https://github.com/bumptech/glide)
- [Retrofit](https://square.github.io/retrofit/)

---

## 📈 Statistics

- **Total Lines of Code**: ~15,000+
- **Total Classes**: 50+
- **Fragments**: 25+
- **Database Tables**: 6
- **API Endpoints**: Ready for integration
- **UI Screens**: 30+

---

<div align="center">

### ⭐ Nếu bạn thấy dự án hữu ích, hãy cho chúng tôi một star!

Made with ❤️ by Bookish Bliss Haven Team

**Version 1.0.0** | **Last Updated: November 26, 2025**

</div>

