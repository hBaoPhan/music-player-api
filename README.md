# 🎵 Spotifour Music Player API 

Hệ thống REST API mạnh mẽ, bảo mật và hiệu năng cao cho ứng dụng trình phát nhạc trực tuyến **Spotifour**, được phát triển dựa trên **Spring Boot**, **Spring Security JWT**, **MySQL** và **Redis**.

---

## 🔗 Các Repository Trong Dự Án

* 🖥️ **Repository Frontend (ReactJS):** [music-player-client](https://github.com/hBaoPhan/music-player-client)
* ⚙️ **Repository Backend (Spring Boot):** [music-player-api](https://github.com/hBaoPhan/music-player-api)

---

## ✨ Các Tính Năng Backend Cung Cấp

* **Xác Thực Stateless (JWT)**: Đăng ký, đăng nhập tài khoản an toàn. Hỗ trợ hệ thống cơ chế tự động làm mới mã Token (Access Token & Refresh Token) lưu trữ ở Redis.
* **Đăng Nhập Google OAuth2**: Đăng nhập trực tiếp bằng tài khoản Google, tự động ánh xạ thông tin người dùng an toàn.
* **Quản Lý Bài Hát & Album**: Tìm kiếm thông minh theo tên, nghệ sĩ, album. Xếp hạng Top Trending và Top Favorites của tuần.
* **Tương Tác Người Dùng**: 
  * Lưu trữ và phân tích lịch sử nghe nhạc cá nhân.
  * Đánh dấu yêu thích bài hát (Toggle Favorites).
  * Quản lý danh sách phát (Playlists) cá nhân.
* **Gửi Email Tự Động**: Tích hợp với dịch vụ Brevo API để khôi phục mật khẩu.
* **Thống Kê Cho Admin (Dashboard)**: API thống kê tổng hợp số lượng người dùng, bài hát, lượt nghe, lượt yêu thích phục vụ cho giao diện Dashboard quản trị.

---

## 🛠️ Công Nghệ & Kiến Trúc Sử Dụng

* **Core Framework**: [Spring Boot 3.x](https://spring.io/projects/spring-boot)
* **Language**: Java 21
* **Architecture**: Kiến trúc phân lớp 3 tầng chuẩn (Layered Architecture: Controller, Service, Repository, DTO, Entity).
* **Security & Auth**: Spring Security, JWT (Json Web Token), Google OAuth2 Client
* **Database & Persistence**: MySQL, Spring Data JPA, Hibernate
* **Caching & Storage**: [Redis](https://redis.io/) (Lưu trữ và quản lý phiên đăng nhập Refresh Token, Caching dữ liệu tối ưu hóa hiệu năng)
* **Mailing Service**: Brevo API
* **Boilerplate Reduction**: Project Lombok
* **Build Tool**: Maven

---

## ⚠️ Tuyên Bố Miễn Trừ Trách Nhiệm

Dự án này được thực hiện hoàn toàn phục vụ cho **mục đích học tập, nghiên cứu và phát triển cá nhân**. Các nguồn tài nguyên âm nhạc, hình ảnh được sử dụng trong hệ thống chỉ phục vụ cho mục đích phi thương mại.
