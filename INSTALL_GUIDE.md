# Hướng Dẫn Cài Đặt Ứng Dụng Bookstore

## 📱 Cài Đặt Trên Android Studio Emulator

### Bước 1: Build APK
App đã được build thành công. File APK nằm tại:
```
pj/app/build/outputs/apk/debug/app-debug.apk
```

### Bước 2: Cài Đặt Trên Emulator
Sử dụng Android Studio hoặc ADB:

**Cách 1: Dùng Android Studio**
1. Mở Android Studio
2. Đảm bảo emulator đang chạy
3. Nhấp chuột phải trên project → "Run 'app'"
4. Chọn emulator đang chạy

**Cách 2: Dùng ADB (Command Line)**
```bash
# Uninstall app cũ (nếu có)
adb uninstall com.example.bookstore

# Install app mới
adb install -r pj/app/build/outputs/apk/debug/app-debug.apk
```

## ✨ Các Tính Năng Mới (v2.1)

### 1. **✅ Số Lượng Sản Phẩm Tồn Kho - UI Cải Tiến**
- ✨ **UI Quantity Selector mới**: Buttons đẹp hơn với màu sắc rõ ràng
- 📊 **Hiển thị Stock Status**: 
  - Màu xanh: Còn > 50 cuốn
  - Màu cam: Còn 10-50 cuốn  
  - Màu đỏ: Chỉ còn < 10 cuốn
- ⚠️ Không được vượt quá số lượng tồn kho
- 💬 Thông báo chi tiết khi hết hàng

### 2. **💬 Đánh Giá Khách Hàng**
- Hiển thị **reviews của khách hàng** trong chi tiết sách
- Mỗi review bao gồm: Tên, Rating, Ngày, Nội dung
- Layout đẹp với CardView

### 3. **🛒 Không Quay Về Sau Khi Thêm Giỏ Hàng**
- ✅ Vẫn ở lại trang chi tiết sách sau khi thêm vào giỏ
- 🔄 Quantity tự động reset về 1
- 👍 Dễ dàng tiếp tục mua sắm hoặc xem thông tin

### 4. **🏠 HomePage Đẹp Hơn**
- 🎨 **Hero Banner** với gradient và icon đẹp
- 📂 **Danh Mục Nổi Bật** với Grid layout
- 🔥 **Sách Bán Chạy** (rating >= 4.7)
- ✨ **Sách Mới Nhất** (10 cuốn mới nhất)
- Tất cả với horizontal scroll

### 5. **📍 Chọn Địa Chỉ Đã Lưu Tại Checkout**
- Thêm nút **"Chọn địa chỉ đã lưu"** trong phần checkout
- Hiển thị dialog với danh sách tất cả địa chỉ đã lưu
- Tự động điền vào trường "Địa chỉ giao hàng" khi chọn

### 6. **🧹 Dọn Dẹp Project**
- Xóa tất cả file .txt và .bat không cần thiết
- Giữ lại README.md và gradlew.bat (cần để build)

## 🛠️ Thay Đổi Mã Nguồn

### File Chỉnh Sửa:

#### 1. **Book.java** - Thêm trường quantity
```java
public int quantity; // Số lượng tồn kho

// Constructor cũ - mặc định 100 nếu có sẵn
public Book(..., boolean inStock) {
    this.quantity = inStock ? 100 : 0;
}

// Constructor mới - cho phép set quantity tùy ý
public Book(..., boolean inStock, int quantity) {
    this.quantity = quantity;
}
```

#### 2. **BookDetailFragment.java** - UI đẹp + Reviews + Stock status
- ✅ Giới hạn tăng quantity không vượt quá `book.quantity`
- ✅ Kiểm tra stock trước khi add to cart
- ✅ Thêm TextView hiển thị stock status với màu sắc
- ✅ Thêm RecyclerView hiển thị customer reviews
- ✅ **Không quay về** sau khi add to cart (reset quantity về 1)

#### 3. **fragment_book_detail.xml** - Cải thiện UI
- ✅ Sửa quantity selector buttons (background màu đẹp)
- ✅ Thêm Stock Status section với màu động
- ✅ Thêm Customer Reviews RecyclerView

#### 4. **Review.java** (NEW) - Model cho review
```java
public class Review {
    String reviewerName;
    float rating;
    String date;
    String content;
}
```

#### 5. **ReviewAdapter.java** (NEW) - Adapter cho reviews
- Hiển thị danh sách đánh giá khách hàng

#### 6. **item_review.xml** (NEW) - Layout cho review item
- CardView đẹp với reviewer name, rating, date, content

#### 7. **HomeFragment.java** - Cải thiện logic
- ✅ Thêm Categories section
- ✅ Featured Books (rating >= 4.7)
- ✅ New Arrivals (10 cuốn mới nhất)
- ✅ Navigation buttons

#### 8. **fragment_home.xml** - UI mới hoàn toàn
- ✅ Hero Banner với gradient đẹp
- ✅ Danh Mục Nổi Bật (GridLayout)
- ✅ Sách Bán Chạy section
- ✅ Sách Mới Nhất section

#### 9. **CheckoutFragment.java** - Dialog chọn địa chỉ
- Thêm nút "Chọn địa chỉ đã lưu"
- Hiển thị AlertDialog với danh sách địa chỉ
- Tự động điền địa chỉ khi chọn

#### 10. **fragment_checkout.xml** - UI
- Thêm nút "Select Saved Address"
- Cập nhật layout để hỗ trợ

## 📊 Cấu Trúc Project

```
pj/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/bookstore/
│   │       │   ├── models/
│   │       │   │   ├── Book.java (✏️ Modified)
│   │       │   │   ├── Cart.java
│   │       │   │   ├── CartItem.java
│   │       │   │   ├── Order.java
│   │       │   │   ├── User.java
│   │       │   │   └── Voucher.java
│   │       │   ├── ui/fragments/
│   │       │   │   ├── BookDetailFragment.java (✏️ Modified)
│   │       │   │   ├── CheckoutFragment.java (✏️ Modified)
│   │       │   │   ├── AddressFragment.java
│   │       │   │   └── ... (other fragments)
│   │       │   ├── adapters/
│   │       │   ├── utils/
│   │       │   └── MainActivity.java
│   │       └── res/
│   │           ├── layout/
│   │           │   ├── fragment_checkout.xml (✏️ Modified)
│   │           │   ├── fragment_book_detail.xml
│   │           │   └── ... (other layouts)
│   │           └── ... (other resources)
│   └── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── README.md

books_full_9xx.csv  (Dữ liệu gốc)
```

## 🚀 Chạy Ứng Dụng

```bash
# Build debug
gradlew.bat clean assembleDebug

# Chạy trên emulator
gradlew.bat installDebug

# Hoặc cài trực tiếp qua Android Studio
# Run → Select Emulator
```

## 📝 Lưu Ý

- **Số lượng mặc định**: Mỗi sách mặc định 100 cuốn nếu `inStock = true`
- **Địa chỉ**: Phải lưu địa chỉ trước trong "Profile" → "Địa chỉ của tôi"
- **CSV**: File `books_full_9xx.csv` ở thư mục gốc (chưa được xử lý bằng Python)

---
**Phiên bản**: v2.0  
**Ngày update**: 2025-11-19

