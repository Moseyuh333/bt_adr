# 🧪 HƯỚNG DẪN KIỂM TRA VÀ XÁC THỰC CÁC SỬA LỖI

## 📱 CÁC BƯỚC KIỂM TRA TRÊN DEVICE

### 1. Chuẩn Bị
```
1. Uninstall app cũ hoàn toàn
2. Clear app cache/data
3. Cài app mới từ APK: pj/app/build/outputs/apk/debug/app-debug.apk
```

### 2. KIỂM TRA LỖI #1: DANH MỤC HIỂN THỊ TÙM LUM

**Tại Home Fragment**:
- ✅ Nhìn vào phần "Danh Mục" 
- ✅ Danh mục phải hiển thị rõ ràng, không có HTML tags
- ✅ Mỗi danh mục phải ngắn gọn (không quá 50 ký tự)
- ✅ Không có ký tự lạ hoặc text thừa

**Expected**: Danh mục sạch sẽ, rõ ràng
```
✓ Fiction
✓ Fantasy  
✓ Science Fiction
✓ Romance
✓ Mystery
✓ Thriller
```

**NOT Expected**: 
```
✗ <fiction>Fiction</fiction>
✗ Fiction|Tiểu Thuyết Viễn Tưởng|...
✗ Sách về những câu chuyện hư cấu...
```

---

**Tại Category Fragment**:
- ✅ Danh sách categories trên thanh ngang
- ✅ Mỗi category hiển thị với icon
- ✅ Khi click vào category, danh sách sách thay đổi
- ✅ Không có HTML, không có text dài

---

### 3. KIỂM TRA LỖI #2: SÁCH HIỂN THỊ MÔ TẢ THAY VÌ TÊN

**Tại Tất Cả Danh Sách Sách**:
- ✅ **Title (Tên Sách)**: PHẢI hiển thị tên ngắn gọn, rõ ràng
  - Ví dụ: "Những Người Khôn Ngoan", "Sapiens", "1984"
  - NOT: "Cuốn sách khám phá những bí mật của..."

- ✅ **Author (Tác Giả)**: PHẢI hiển thị tác giả
  - Ví dụ: "Nhân Phạm", "Yuval Noah Harari"
  - NOT: "Người viết cuốn sách này..."

- ✅ **Category (Danh Mục)**: PHẢI hiển thị loại sách
  - Ví dụ: "Self-Help", "History", "Fiction"
  - NOT: "Sách về những người thành công..."

**Test Case**: 
```
Sách "Sapiens" của "Yuval Noah Harari" (History)
- Title PHẢI là: "Sapiens" (ngắn, rõ ràng)
- Author PHẢI là: "Yuval Noah Harari"
- NOT title là: "Hành trình từ động vật hoang dã đến chủ nhân thế giới..."
```

---

### 4. KIỂM TRA LỖI #3: CRASH KHI XEM CHI TIẾT ĐƠN HÀNG

**Tại Order History**:
- ✅ Bấm vào bất kỳ đơn hàng nào
- ✅ Chi tiết đơn hàng phải hiển thị đúng
- ✅ **Danh sách sản phẩm**:
  - Phải hiển thị ảnh sách
  - Phải hiển thị tên sách (không phải mô tả)
  - Phải hiển thị tác giả
  - Phải hiển thị số lượng
  - Phải hiển thị giá
- ✅ **Không crash**, hiển thị bình thường

**Expected**:
```
[Book Image] | Sapiens
              | Tác giả: Yuval Noah Harari
              | SL: 1
              | 150,000₫ × 1 = 150,000₫
```

**NOT Expected**:
```
App crash / Force close / Blank screen
NullPointerException
Lỗi hiển thị sản phẩm
```

---

### 5. KIỂM TRA TRÊN ADMIN PANEL

**Tại Admin - Danh Sách Sản Phẩm**:
- ✅ Tên sách (Title) hiển thị đúng
- ✅ Tác giả (Author) hiển thị đúng  
- ✅ Danh mục (Category) hiển thị đúng, ngắn gọn
- ✅ Giá (Price) hiển thị đúng
- ✅ Tồn kho (Stock) hiển thị đúng

---

## 🔍 KIỂM TRA CODE LEVEL

### Verify Comments Có Rõ Ràng

**Tìm trong BookAdapter.java**:
```java
// title = tên sách (Book name) ← PHẢI CÓ
// author = tác giả (Author name) ← PHẢI CÓ
```

**Tìm trong CategoryAdapter.java**:
```java
// category = danh mục (Category name) ← PHẢI CÓ
```

**Tìm trong AdminProductAdapter.java**:
```java
// title = tên sách (Book name) ← PHẢI CÓ
// category = danh mục (Category name) ← PHẢI CÓ
```

---

### Verify Null Checks

**BookAdapter.onBindViewHolder()**:
```java
if (b == null) { ... }
if (b.title != null && !b.title.isEmpty()) { ... }
String author = (b.author != null && !b.author.isEmpty()) ? b.author : "Tác giả";
```

**CategoryAdapter.onBindViewHolder()**:
```java
if (category == null || category.isEmpty()) { category = "Sách"; }
String cleanCategory = category.replaceAll("<[^>]*>", "").trim();
```

**OrderDetailFragment.onViewCreated()**:
```java
List<?> itemsToDisplay = (order.items != null && !order.items.isEmpty()) 
    ? order.items 
    : new ArrayList<>();
```

---

## 📊 KIỂM TRA LOGCAT

**Khi chạy, không nên thấy lỗi:**
```
✗ NullPointerException at BookAdapter.java
✗ NullPointerException at CategoryAdapter.java
✗ NullPointerException at OrderDetailFragment.java
✗ Unable to find item_order_product layout
```

**Chỉ nên thấy**:
```
✓ Normal app logs
✓ Deprecation warnings (OK)
✓ Success messages
```

---

## ✅ CHECKLIST FINAL

- [ ] App không crash khi mở Home Fragment
- [ ] App không crash khi mở Category Fragment  
- [ ] App không crash khi xem danh sách sách
- [ ] App không crash khi xem chi tiết sách
- [ ] App không crash khi xem chi tiết đơn hàng
- [ ] App không crash khi xem admin panel
- [ ] Danh mục hiển thị sạch sẽ (không HTML)
- [ ] Tên sách hiển thị đúng (không bị lẫn với description)
- [ ] Tác giả hiển thị đúng
- [ ] Giá hiển thị đúng
- [ ] Stock hiển thị đúng
- [ ] Order items hiển thị đúng
- [ ] Không có text tùm lum ở bất kỳ nơi nào
- [ ] Không có HTML tags hiển thị trên UI

---

## 🐛 Nếu Vẫn Có Lỗi

### Nếu Category vẫn bị tùm lum
1. Xóa data app: Settings > Apps > BookStore > Storage > Clear Data
2. Mở lại app - sẽ reimport dữ liệu
3. Kiểm tra lại

### Nếu Title vẫn hiển thị description
1. Check logcat xem có error không
2. Xóa data app
3. Reimport từ CSV

### Nếu Order Detail vẫn crash
1. Check logcat - tìm NullPointerException
2. Liên hệ developer với error message
3. Xóa data app, reinstall app

---

## 📝 Report Template (Nếu có lỗi)

Nếu tìm thấy lỗi, report theo format:

```
Bug Title: [Describe issue]
Location: [Screen/Fragment name]
Steps to reproduce:
1. ...
2. ...
3. ...

Expected: [What should happen]
Actual: [What actually happens]

Logcat error (nếu có):
[Paste error message]

Screenshots: [Attach if possible]
```

---

✅ **Đó là tất cả! Hệ thống đã được sửa toàn diện.**

Build Status: ✅ SUCCESS
No compilation errors
All null checks in place
Comments clear and descriptive
Error handling implemented

