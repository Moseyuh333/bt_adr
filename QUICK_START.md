# 🚀 QUICK START - Bookstore App v2.0

## ⚡ Cài Đặt Nhanh (1 Phút)

### Yêu Cầu
- Android Studio 2021.3+
- Android Emulator chạy (API 28+)
- ADB kế nối

### Bước 1: Build
```bash
cd pj
gradlew.bat clean assembleDebug
```

### Bước 2: Cài Đặt
```bash
adb uninstall com.example.bookstore
adb install app/build/outputs/apk/debug/app-debug.apk
```

Hoặc: Mở Android Studio → Run → Chọn Emulator

### Bước 3: Mở App
App sẽ tự động mở trên emulator

---

## 🎯 Tính Năng Mới

### 1️⃣ **Xem Số Lượng Sách Trong Kho**
- Tại màn hình chi tiết sách
- Khi nhấn "+" không thể vượt quá stock
- Nhận được thông báo nếu hết hàng

### 2️⃣ **Chọn Địa Chỉ Khi Thanh Toán**
- Tại checkout, nhấn "Chọn địa chỉ đã lưu"
- Chọn từ danh sách các địa chỉ đã lưu
- Tự động điền vào ô địa chỉ

---

## 📦 Project Structure

```
bt_adr/
├── pj/ (Android Project)
│   ├── app/
│   │   ├── src/main/java/com/example/bookstore/
│   │   ├── build/outputs/apk/debug/app-debug.apk ✅
│   │   └── ...
│   ├── gradlew.bat
│   └── ...
├── books_full_9xx.csv (Dữ liệu sách)
├── README.md (Tài liệu gốc)
├── INSTALL_GUIDE.md (📌 Chi tiết)
└── CHANGELOG.md (📝 Nhật ký thay đổi)
```

---

## 🐛 Troubleshooting

| Vấn Đề | Giải Pháp |
|--------|----------|
| Build fail | `gradlew clean` → `gradlew assembleDebug` |
| APK không cài | `adb uninstall com.example.bookstore` trước |
| Emulator không kết nối | Khởi động lại emulator |
| Không thấy địa chỉ | Lưu địa chỉ trong Profile trước |

---

## 📞 Liên Hệ
Chi tiết xem: `INSTALL_GUIDE.md` & `CHANGELOG.md`

**Last Update**: 2025-11-19

