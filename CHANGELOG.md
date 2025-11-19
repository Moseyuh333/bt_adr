# 📝 CHANGELOG - Phiên Bản 2.0

## ✅ Các Vấn Đề Đã Giải Quyết

### 1. ✨ Số Lượng Sản Phẩm Còn Lại Trong Kho
**Vấn đề gốc**: Chỉ hiển thị boolean `inStock` không rõ ràng  
**Giải pháp**:
- ✅ Thêm trường `int quantity` vào model `Book.java`
- ✅ Mặc định 100 cuốn nếu `inStock = true`
- ✅ Kiểm tra giới hạn khi tăng quantity: không vượt quá stock
- ✅ Thông báo rõ ràng khi hết hàng hoặc vượt quá stock

**File thay đổi**:
- `Book.java` - Thêm trường quantity + 2 constructor
- `BookDetailFragment.java` - Kiểm tra stock + thông báo

---

### 2. 📍 Checkout Chưa Chọn Địa Chỉ Đã Lưu
**Vấn đề gốc**: Phải nhập thủ công địa chỉ, không dùng được địa chỉ đã lưu  
**Giải pháp**:
- ✅ Thêm nút **"Chọn địa chỉ đã lưu"** trong checkout
- ✅ Hiển thị AlertDialog với danh sách địa chỉ (mô tả + địa chỉ)
- ✅ Tự động điền vào EditText khi chọn
- ✅ Tích hợp với hệ thống Address lưu trữ có sẵn

**File thay đổi**:
- `CheckoutFragment.java` - Thêm method `showSavedAddresses()` + listener
- `fragment_checkout.xml` - Thêm nút select address

---

### 3. 🧹 Dọn Dẹp Project
**Các file đã xóa**:
- ❌ ADB_SETUP.txt
- ❌ AUTO_INSTALL.bat
- ❌ BUILD_AND_INSTALL.bat
- ❌ FINAL_REPORT.txt
- ❌ FIND_ADB_AND_INSTALL.bat
- ❌ GO.txt
- ❌ INSTALL_NEW.bat
- ❌ INSTALL_NEW_APP.bat
- ❌ INSTALL_NOW.txt
- ❌ LAUNCH_APP.bat
- ❌ NEW_FEATURES_ADDED.txt
- ❌ QUICK_START.txt
- ❌ REINSTALL_APP.bat
- ❌ REINSTALL_GUIDE.txt
- ❌ RUN_APP.bat
- ❌ START_APP.bat

**File được giữ**:
- ✅ README.md - Documentation
- ✅ gradlew.bat - Gradle wrapper (cần để build)
- ✅ INSTALL_GUIDE.md - Hướng dẫn cài đặt (tạo mới)

---

## 🔧 Chi Tiết Thay Đổi Mã

### Book.java
```java
// Trường mới
public int quantity; // Số lượng tồn kho

// Constructor cũ - backward compatible
public Book(..., boolean inStock) {
    this.quantity = inStock ? 100 : 0; // Mặc định 100
}

// Constructor mới - cho phép customize quantity
public Book(..., boolean inStock, int quantity) {
    this.quantity = quantity;
}
```

### BookDetailFragment.java
```java
// Kiểm tra giới hạn khi tăng
if (currentQuantity < book.quantity) {
    currentQuantity++;
} else {
    Toast.makeText(getContext(), 
        String.format("Chỉ còn %d cuốn trong kho", book.quantity), 
        Toast.LENGTH_SHORT).show();
}

// Kiểm tra trước khi add to cart
if (!book.inStock || book.quantity <= 0) {
    Toast.makeText(getContext(), "Sản phẩm hết hàng", Toast.LENGTH_SHORT).show();
    return;
}
```

### CheckoutFragment.java
```java
// Nút chọn địa chỉ
private Button selectAddressBtn;

// Listener
selectAddressBtn.setOnClickListener(v -> showSavedAddresses());

// Method mới
private void showSavedAddresses() {
    // Load danh sách địa chỉ từ SharedPreferences
    // Hiển thị AlertDialog
    // Tự động điền khi chọn
}
```

---

## 🏗️ Build Status

```
✅ BUILD SUCCESSFUL in 36s
✅ 35 actionable tasks: 35 executed
✅ APK output: app/build/outputs/apk/debug/app-debug.apk (8.1 MB)
```

---

## 📱 Cách Cài Đặt Bản Mới

### Uninstall app cũ (trên emulator)
```bash
adb uninstall com.example.bookstore
```

### Install app mới
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Hoặc dùng Android Studio: **Run** → Chọn emulator

---

## ❓ Câu Hỏi Thường Gặp

**Q: Tại sao chỉ có 100 cuốn mặc định?**  
A: Đó là số mặc định cho testing. Bạn có thể thay đổi trong `Book.java` hoặc dùng constructor thứ 2.

**Q: Số lượng tồn kho có được cập nhật sau khi order?**  
A: Hiện tại chưa. Cần thêm logic trong `OrderManager` để giảm quantity sau mỗi order.

**Q: Địa chỉ đã lưu ở đâu?**  
A: Lưu trong SharedPreferences qua `AddressFragment` trong Profile.

**Q: CSV có được xử lý?**  
A: Hiện tại vẫn chưa. Dữ liệu được hardcode trong `BookDataLoader.java`. 

---

## 📅 Timeline

- **v1.0**: Initial version (25/10/2025)
- **v2.0**: Cải thiện stock + address selection + cleanup (19/11/2025)

---

**Tác giả**: AI Assistant  
**Cập nhật lần cuối**: 2025-11-19 12:30

