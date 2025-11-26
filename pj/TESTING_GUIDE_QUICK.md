# 🧪 Hướng Dẫn Test Nhanh - Quick Testing Guide

## ✅ Build Status
```
✓ BUILD SUCCESSFUL
✓ 0 Compilation Errors
✓ All Issues Fixed
```

## 🚀 Cài Đặt & Chạy (Installation & Run)

### Option 1: Android Studio
1. Mở Android Studio
2. Open Project → Chọn folder `D:\New folder\bt_adr\pj`
3. Wait for Gradle sync
4. Click "Run" button (hoặc Shift+F10)
5. Chọn emulator hoặc device

### Option 2: Command Line
```bash
cd "D:\New folder\bt_adr\pj"
.\gradlew.bat installDebug
```

## 🧪 Test Cases - Phải Chạy Tất Cả

### ✅ Test 1: Categories Hiển Thị
**Mục đích:** Kiểm tra danh mục hiển thị đúng

**Bước test:**
1. Mở app
2. Click tab "Danh mục" (icon 4 ô vuông)
3. **Kỳ vọng:**
   - ✓ Thấy 12 categories với icons
   - ✓ Văn học 📖, Lịch sử 🏛️, Khoa học 🔬, etc.
   - ✓ Mỗi category có số lượng sách
4. Click vào "Văn học"
5. **Kỳ vọng:**
   - ✓ Hiển thị 5 sách trong danh mục Văn học
   - ✓ Tất cả có title và author

**Status:** [ ] PASS / [ ] FAIL

---

### ✅ Test 2: Tên Sách Hiển Thị
**Mục đích:** Kiểm tra title của sách không bị null/empty

**Bước test:**
1. Vào tab "Trang chủ" (Home)
2. Scroll xuống xem tất cả sách
3. **Kỳ vọng:**
   - ✓ Mọi sách đều có tên rõ ràng
   - ✓ Không có blank title
   - ✓ Có author name (vd: "George Orwell")
   - ✓ Có category tag
4. Vào tab "Thư viện" (Catalog)
5. **Kỳ vọng:** Tương tự, tất cả sách có đầy đủ info

**Status:** [ ] PASS / [ ] FAIL

---

### ✅ Test 3: Chi Tiết Đơn Hàng Không Crash
**Mục đích:** Kiểm tra xem chi tiết order không bị crash

**Bước test:**
1. Vào tab "Hồ sơ" (Profile)
2. Chọn "Đơn hàng của tôi"
3. **Kỳ vọng:**
   - ✓ Thấy 3 đơn hàng mẫu:
     - ORD1 - Chờ xác nhận
     - ORD2 - Đang giao
     - ORD3 - Đã giao
4. Click vào "ORD1"
5. **Kỳ vọng:**
   - ✓ App KHÔNG crash
   - ✓ Hiển thị thông tin đơn hàng:
     - Mã đơn: ORD1
     - Khách hàng: Khách hàng 1
     - Địa chỉ: 123 Nguyễn Huệ...
     - Sản phẩm: Sách mẫu 1
     - Tổng tiền: 235,000₫
6. Quay lại, click vào "ORD2" và "ORD3"
7. **Kỳ vọng:** Tất cả đều hiển thị OK, không crash

**Status:** [ ] PASS / [ ] FAIL

---

### ✅ Test 4: Search Sách Theo Tên
**Mục đích:** Kiểm tra search có hoạt động với title mới

**Bước test:**
1. Vào tab "Tìm kiếm" (Search icon)
2. Gõ "Sapiens" vào search box
3. **Kỳ vọng:**
   - ✓ Tìm thấy sách "Sapiens" của Yuval Noah Harari
   - ✓ Hiển thị đầy đủ title, author, price
4. Gõ "Harry Potter"
5. **Kỳ vọng:**
   - ✓ Tìm thấy "Harry Potter và Hòn Đá Phù Thủy"
6. Gõ "xyz123" (không tồn tại)
7. **Kỳ vọng:**
   - ✓ Hiển thị "Không tìm thấy kết quả"

**Status:** [ ] PASS / [ ] FAIL

---

### ✅ Test 5: Lọc Theo Category
**Mục đích:** Kiểm tra filter category hoạt động

**Bước test:**
1. Vào tab "Danh mục"
2. Click "Kỹ năng"
3. **Kỳ vọng:**
   - ✓ Hiển thị 3 sách:
     - Atomic Habits
     - Đắc Nhân Tâm
     - 7 Thói Quen Hiệu Quả
4. Click "Giáo dục"
5. **Kỳ vọng:**
   - ✓ Hiển thị 4 sách fantasy:
     - The Hobbit
     - Harry Potter
     - The Name of the Wind
     - A Game of Thrones

**Status:** [ ] PASS / [ ] FAIL

---

### ✅ Test 6: Xem Chi Tiết Sách
**Mục đích:** Kiểm tra book detail screen

**Bước test:**
1. Vào Home, click vào sách "1984"
2. **Kỳ vọng:**
   - ✓ Hiển thị trang chi tiết sách
   - ✓ Title: "1984"
   - ✓ Author: "George Orwell"
   - ✓ Category: "Văn học"
   - ✓ Description có nội dung
   - ✓ Price: 95,000₫
   - ✓ Có button "Thêm vào giỏ"

**Status:** [ ] PASS / [ ] FAIL

---

### ✅ Test 7: Thêm Vào Giỏ Hàng
**Mục đích:** Kiểm tra cart functionality

**Bước test:**
1. Từ trang chi tiết sách "1984", click "Thêm vào giỏ"
2. **Kỳ vọng:** Toast "Đã thêm vào giỏ hàng"
3. Vào tab "Giỏ hàng" (Cart)
4. **Kỳ vọng:**
   - ✓ Thấy sách "1984" trong giỏ
   - ✓ Có title, author, price
   - ✓ Có button tăng/giảm số lượng
   - ✓ Tổng tiền hiển thị đúng

**Status:** [ ] PASS / [ ] FAIL

---

## 🐛 Nếu Gặp Lỗi (If Issues Found)

### Lỗi: Categories vẫn không hiển thị
**Giải pháp:**
```bash
# Xóa data app và chạy lại
adb shell pm clear com.example.bookstore
# Hoặc: Settings → Apps → Bookstore → Clear Data
```

### Lỗi: Titles vẫn blank
**Giải pháp:**
1. Android Studio → Build → Clean Project
2. Build → Rebuild Project
3. Chạy lại app

### Lỗi: Order details vẫn crash
**Kiểm tra:**
1. Logcat để xem error message
2. Đảm bảo đã cài đặt app mới nhất
3. Clear app data
4. Reinstall app

## 📊 Báo Cáo Test (Test Report)

Điền vào sau khi test:

| Test Case | Status | Note |
|-----------|--------|------|
| Test 1: Categories | [ ] PASS / [ ] FAIL | |
| Test 2: Book Titles | [ ] PASS / [ ] FAIL | |
| Test 3: Order Details | [ ] PASS / [ ] FAIL | |
| Test 4: Search | [ ] PASS / [ ] FAIL | |
| Test 5: Filter Category | [ ] PASS / [ ] FAIL | |
| Test 6: Book Detail | [ ] PASS / [ ] FAIL | |
| Test 7: Cart | [ ] PASS / [ ] FAIL | |

**Overall Status:** [ ] ALL PASS / [ ] SOME FAIL

**Date:** __________
**Tester:** __________
**Device/Emulator:** __________

## 🎯 Expected Results Summary

✅ **Categories:** 12 categories hiển thị với icons đẹp
✅ **Book Titles:** Tất cả sách có tên rõ ràng, không null
✅ **Order Details:** Không crash, hiển thị đầy đủ info
✅ **Sample Data:** 37 sách + 3 orders tự động tạo
✅ **UI:** Mọi screen hiển thị OK, không blank

## 💡 Tips

1. **First Run:** Lần đầu mở app sẽ init database (3-5 giây)
2. **Toast Messages:** Chú ý toast "Database ready!" khi app start
3. **Sample Orders:** 3 orders mẫu được tạo tự động
4. **Performance:** Scroll mượt mà, không lag

---

**Created:** 26/11/2025
**Status:** Ready for Testing ✅
**Build:** SUCCESSFUL ✅

