# Auth Features Added - Login, Register, Forgot Password

## ✅ Chức năng đã thêm thành công!

App Book Bliss Heaven giờ đã có đầy đủ các chức năng authentication:

### 1. **Login Screen (Màn hình Đăng nhập)**
- Email và Password input với Material Design
- Nút "Forgot Password?" để reset mật khẩu
- Link "Sign Up" để chuyển đến màn hình đăng ký
- Tài khoản demo: `demo@bookstore.com` / `demo123`
- Validation: Kiểm tra email/password rỗng
- Lưu trạng thái đăng nhập bằng SharedPreferences

### 2. **Register Screen (Màn hình Đăng ký)**
- Full Name, Email, Password, Confirm Password inputs
- Validation:
  - Kiểm tra các trường rỗng
  - Validate email format
  - Password phải tối thiểu 6 ký tự
  - Confirm password phải khớp
- Tự động đăng nhập sau khi đăng ký thành công
- Link "Sign In" để quay lại màn hình đăng nhập

### 3. **Forgot Password Screen (Màn hình Quên mật khẩu)**
- Email input để gửi link reset
- Validation email format
- Giả lập gửi email reset (hiển thị toast)
- Tự động quay về login sau 2 giây
- Nút "Back to Sign In"

### 4. **Navigation Flow**
- App bắt đầu từ màn hình Login
- Bottom Navigation ẩn ở các màn hình auth
- Bottom Navigation hiện ở các màn hình chính (Home, Catalog, Cart, Profile)
- Kiểm tra trạng thái đăng nhập khi mở app

### 5. **Profile Screen - Logout**
- Thêm nút "Logout" trong Profile
- Load thông tin user từ SharedPreferences
- Logout sẽ xóa trạng thái đăng nhập và quay về Login

## 🎨 UI/UX Features

- **Material Design Components**: TextInputLayout với outline style
- **Color Scheme**: Amber 600 làm màu chủ đạo
- **Responsive Layout**: ScrollView để hỗ trợ các màn hình nhỏ
- **Password Toggle**: Hiện/ẩn mật khẩu
- **Clickable Links**: Các link màu amber với bold text
- **Toast Messages**: Thông báo thành công/lỗi

## 📱 Cách sử dụng

### Chạy app lần đầu:
1. App mở màn hình Login
2. Sử dụng tài khoản demo: `demo@bookstore.com` / `demo123`
3. Hoặc đăng ký tài khoản mới bằng "Sign Up"

### Đăng ký tài khoản mới:
1. Click "Sign Up" trên màn hình Login
2. Điền đầy đủ thông tin
3. Password tối thiểu 6 ký tự
4. Tự động đăng nhập sau khi đăng ký

### Quên mật khẩu:
1. Click "Forgot Password?" trên màn hình Login
2. Nhập email
3. Nhận thông báo "Reset link sent"
4. Tự động quay về Login

### Đăng xuất:
1. Vào tab Profile (bottom navigation)
2. Click nút "Logout"
3. Quay về màn hình Login

## 🔧 Technical Details

### Files Created:
- `LoginFragment.java`
- `RegisterFragment.java`
- `ForgotPasswordFragment.java`
- `fragment_login.xml`
- `fragment_register.xml`
- `fragment_forgot_password.xml`

### Files Modified:
- `MainActivity.java` - Thêm logic ẩn/hiện bottom nav, kiểm tra login
- `ProfileFragment.java` - Thêm chức năng logout
- `nav_graph.xml` - Thêm auth fragments và navigation actions
- `colors.xml` - Thêm gray_600 color

### Data Storage:
Sử dụng SharedPreferences để lưu:
- `is_logged_in` - Trạng thái đăng nhập
- `user_name` - Tên người dùng
- `user_email` - Email
- `user_password` - Mật khẩu (demo only - không nên lưu plaintext trong production)
- `user_phone` - Số điện thoại
- `user_address` - Địa chỉ

## 🚀 Build & Install

```bash
# Build APK
.\gradlew assembleDebug

# Install on emulator/device
.\gradlew installDebug
```

## ✨ App đã sẵn sàng!

Giờ bạn có thể:
1. ✅ Đăng nhập với tài khoản demo
2. ✅ Đăng ký tài khoản mới
3. ✅ Reset mật khẩu
4. ✅ Xem và chỉnh sửa profile
5. ✅ Đăng xuất
6. ✅ Navigate giữa các màn hình Home, Catalog, Cart, Profile

App đang chạy trên emulator của bạn. Thử các chức năng mới nhé! 🎉

