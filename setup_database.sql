drop database shopdatabase;
-- Tạo database
CREATE DATABASE IF NOT EXISTS ShopDatabase;

-- Sử dụng database vừa tạo
USE ShopDatabase;

-- Xóa các bảng cũ nếu tồn tại (theo thứ tự ngược để tránh lỗi foreign key)
DROP TABLE IF EXISTS UserCategory;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Category;

-- Tạo bảng Category
CREATE TABLE Category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    images VARCHAR(500),
    sort_order INT NOT NULL DEFAULT 0
) ENGINE=InnoDB;

-- Tạo bảng User
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
) ENGINE=InnoDB;

-- Tạo bảng Product
CREATE TABLE Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    images VARCHAR(500),
    category_id INT,
    userid INT NOT NULL,
    discount INT DEFAULT 0,
    status BOOLEAN DEFAULT TRUE,
    sort_order INT NOT NULL DEFAULT 0,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE SET NULL,
    FOREIGN KEY (userid) REFERENCES User(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tạo bảng junction cho mối quan hệ nhiều-nhiều giữa Category và User
CREATE TABLE UserCategory (
    userid INT NOT NULL,
    categoryid INT NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (userid, categoryid),
    FOREIGN KEY (userid) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (categoryid) REFERENCES Category(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Chèn dữ liệu mẫu vào bảng Category
INSERT INTO Category (name, sort_order) VALUES
('Điện thoại', 1),
('Laptop', 2),
('Máy tính bảng', 3),
('Phụ kiện', 4),
('Đồng hồ thông minh', 5);

-- Chèn dữ liệu mẫu vào bảng User (bao gồm 1 admin và 4 users)
INSERT INTO User (fullname, email, password, phone, role) VALUES
('Admin User', 'admin@email.com', 'hashed_password_admin', '0900000000', 'ADMIN'),
('Nguyễn Văn An', 'an.nguyen@email.com', 'hashed_password_1', '0901234567', 'USER'),
('Trần Thị Bình', 'binh.tran@email.com', 'hashed_password_2', '0912345678', 'USER'),
('Lê Văn Cường', 'cuong.le@email.com', 'hashed_password_3', '0923456789', 'USER'),
('Phạm Thị Dung', 'dung.pham@email.com', 'hashed_password_4', '0934567890', 'USER'),
('Hoàng Văn Thành', 'thanh.hoang@email.com', 'hashed_password_5', '0945678901', 'USER');

-- Chèn dữ liệu mẫu vào bảng Product
INSERT INTO Product (title, quantity, description, price, category_id, userid, sort_order) VALUES
('iPhone 15 Pro', 50, 'Điện thoại iPhone 15 Pro màu xanh, 256GB', 28990000.00, 1, 2, 1),
('Samsung Galaxy S24', 30, 'Điện thoại Samsung Galaxy S24 màu đen, 128GB', 22990000.00, 1, 3, 2),
('MacBook Pro M3', 20, 'Laptop MacBook Pro chip M3, 14 inch, 512GB SSD', 52990000.00, 2, 2, 1),
('Dell XPS 13', 25, 'Laptop Dell XPS 13 Intel Core i7, 16GB RAM, 1TB SSD', 35990000.00, 2, 4, 2),
('AirPods Pro 2', 100, 'Tai nghe không dây AirPods Pro thế hệ 2', 6990000.00, 4, 3, 1),
('iPad Air M2', 15, 'Máy tính bảng iPad Air chip M2, 10.9 inch, 256GB', 18990000.00, 3, 5, 1),
('Apple Watch Series 9', 40, 'Đồng hồ thông minh Apple Watch Series 9, 45mm', 10990000.00, 5, 6, 1),
('Xiaomi Mi Band 8', 80, 'Vòng đeo tay thông minh Xiaomi Mi Band 8', 1290000.00, 5, 4, 2);

-- Chèn dữ liệu mẫu vào bảng UserCategory (mối quan hệ nhiều-nhiều)
INSERT INTO UserCategory (userid, categoryid) VALUES
-- User 2 (An) quan tâm đến Điện thoại và Laptop
(2, 1), -- Điện thoại
(2, 2), -- Laptop
-- User 3 (Bình) quan tâm đến Điện thoại và Phụ kiện
(3, 1), -- Điện thoại  
(3, 4), -- Phụ kiện
-- User 4 (Cường) quan tâm đến Laptop, Phụ kiện và Đồng hồ thông minh
(4, 2), -- Laptop
(4, 4), -- Phụ kiện
(4, 5), -- Đồng hồ thông minh
-- User 5 (Dung) quan tâm đến Máy tính bảng
(5, 3), -- Máy tính bảng
-- User 6 (Thành) quan tâm đến Đồng hồ thông minh và Điện thoại
(6, 5), -- Đồng hồ thông minh
(6, 1); -- Điện thoại
