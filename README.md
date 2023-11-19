# DoPinPan
MỞ ĐẦU

    - App được thiết kế và tạo ra bởi 1 người, dựa trên ý tưởng và các nguồn tham khảo khác nhau

    - Nếu có bât kì vấn đề về bản quyền vui lòng liên hệ : phanvulinh2003@.gmail.com

I. BÀI GIỚI THIỆU VỀ DOPINPAN

    - DoPinPan là 1 ứng dụng mua đồ ăn nhanh online được thiết kế hoạt động độc lập,
      được phân quyền sử dụng với người dùng bình thường hoặc quản trị.

    - DoPinPan được xây dựng trên Android Studio Bằng Ngôn Ngứ Java, dữ liệu của
      app được lưu trên FireBase và SQLite.
   
    - Các chức năng của người dùng :
        + Thực hiện đầy đủ các thao tác đăng nhập, đăng ký, quản lý thông tin cá nhân, mua, đặt hàng,
          thanh toán, chat bot để biết thêm chi tiết sản phẩm.
          
        + Thông tin chi tiết sản phẩm, chương trình ưu đãi, blog giới thiệu các sản phẩm.
        
    - Các chức năng của quản trị viên :
        + Thực hiện đầy đủ các thao tác đăng nhập, 
        
        +  Quản lý tài khoản và phân quyền sử dụng.
        
        +  Quản lý sản phẩm và các danh mục sản phẩm.
        
        +  Quản lý đơn hàng
        
        + Báo cáo thống kê doanh thu, doanh số theo thời gian.
    


II. PHÂN TÍCH CHI TIẾT CÁC CHỨC NĂNG CHÍNH 

    1. Đăng nhập: Người dùng và người quản trị đều cần có chức năng đăng nhập để truy cập vào ứng dụng.   
    
    2. Đăng ký: Người dùng có thể đăng ký tài khoản mới để sử dụng ứng dụng.
    
    3. Quản lý thông tin cá nhân: Người dùng có thể thay đổi thông tin cá nhân của mình
       (tên, địa chỉ, số điện thoại, email, hình ảnh đại diện, mật khẩu, v.v.).
    
    4. Mua hàng: Người dùng có thể xem danh sách sản phẩm, chọn mua và thêm vào giỏ hàng.
    
    5. Đặt hàng: Người dùng có thể xem giỏ hàng, chỉnh sửa số lượng và đặt hàng.
    
    6. Thanh toán: Người dùng có thể chọn phương thức thanh toán và thực hiện thanh toán cho đơn hàng.
    
    7. Chat bot: Người dùng có thể trò chuyện với chat bot để biết thêm chi tiết về sản phẩm.
    
    8. Xem thông tin chi tiết sản phẩm: Người dùng có thể xem thông tin chi tiết về từng sản phẩm (mô tả, giá, ảnh, v.v.).
    
    9. Xem chương trình ưu đãi: Người dùng có thể xem các chương trình ưu đãi đang diễn ra và áp dụng cho đơn hàng của mình.
    
    10. Xem blog giới thiệu sản phẩm: Người dùng có thể xem các bài viết blog giới thiệu về các sản phẩm.
    
    11. Quản lý tài khoản: Người quản trị có thể quản lý tài khoản người dùng (thêm, sửa, xóa).
    
    12. Phân quyền sử dụng: Người quản trị có thể phân quyền sử dụng cho các tài khoản người dùng.
    
    13. Quản lý sản phẩm: Người quản trị có thể quản lý danh sách sản phẩm (thêm, sửa, xóa).
    
    14. Quản lý danh mục sản phẩm: Người quản trị có thể quản lý danh mục sản phẩm (thêm, sửa, xóa).
    
    15. Quản lý đơn hàng: Người quản trị có thể xem danh sách đơn hàng, xem chi tiết từng đơn hàng, cập nhật trạng thái đơn hàng.
    
    16. Báo cáo thống kê doanh thu: Người quản trị có thể xem báo cáo thống kê doanh thu theo thời gian (ngày, tuần, tháng, quý, năm).

III. PHÂN TÍCH VÀ THIẾT KẾ CẤU TRÚC DỮ LIỆU

     1. Ngôn ngữ lập trình và công nghệ sử dụng:
        - Ứng dụng di động: Android Java.
        - Cơ sở dữ liệu: Firebase.
     
     2. Thiết kế cơ sở dữ liệu:
        a) Bảng "Users" (người dùng):
           - userId (khóa chính)
           - username
           - password
           - name
           - address
           - phone
           - email
           - avatarUrl
        
        b) Bảng "Products" (sản phẩm):
           - productId (khóa chính)
           - name
           - description
           - imageUrl
           - price
           - categoryId (khóa ngoại đến bảng "Categories")
     
        c) Bảng "Categories" (danh mục sản phẩm):
           - categoryId (khóa chính)
           - name
     
        d) Bảng "Orders" (đơn hàng):
           - orderId (khóa chính)
           - userId (khóa ngoại đến bảng "Users")
           - orderDate
           - totalAmount
           - status
     
        e) Bảng "OrderDetails" (chi tiết đơn hàng):
           - orderId (khóa ngoại đến bảng "Orders")
           - productId (khóa ngoại đến bảng "Products")
           - quantity
     
        f) Bảng "Roles" (phân quyền):
           - roleId (khóa chính)
           - name
     
        g) Bảng "UserRoles" (phân quyền cho tài khoản người dùng):
           - userId (khóa ngoại đến bảng "Users")
           - roleId (khóa ngoại đến bảng "Roles")
           
         3. Mô hình ứng dụng:
           - Mô hình MVC (Model-View-Controller) được sử dụng để phân

IV. MỘT SỐ HÌNH ẢNH MINH HỌA
- Màn hình khởi đầu
   ![image](https://github.com/Linh203/DOPINPAN/assets/115698525/8d3306a1-0906-4d5a-b44f-fe5bedb97b8a)
- Màn hình đăng ký
  ![image](https://github.com/Linh203/DOPINPAN/assets/115698525/120ea2df-b3b7-4625-9390-bef75400b289)
- Màn hình đăng nhập
  ![image](https://github.com/Linh203/DOPINPAN/assets/115698525/0dc4fc1e-cebe-4aba-ab28-5789eb6f9ec1)
- Màn hình trang chủ
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/a59ddbff-0d08-44fe-9835-daf453ea82e1)
- Màn hình sản phẩm
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/32aab20d-475e-4ecc-bd54-2f69f354c47f)
- Màn hình danh mục
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/2fbfa80a-e265-44f9-9a7d-69e66ad90c82)

- Chi tiết sản phẩm
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/47acb1be-2628-418f-a7ef-52b1e81fe1f6)

- Giỏ Hàng
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/212755e4-ffc9-4ddd-b751-133a703e88b3)

- Thanh toán
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/a4fdd43d-6fae-47be-b171-31311ab3d698)



- Quản lý đơn hàng
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/501c188e-8152-4edf-9320-4e302923fc25)
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/ce54d67e-da02-4282-8282-088a5ad5be05)



- Quản lý thông tin người dùng
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/cc4cd548-3d0b-44ea-a221-55eae7768748)

- Trang chủ quản trị viên
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/011ee73e-d3d0-4101-99bd-21ed6ddf46f1)
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/8683f719-4b2f-49ef-bc79-5ff5e1d6557d)

- Thống kê doanh thu
![image](https://github.com/Linh203/DOPINPAN/assets/115698525/39741038-3ffa-4898-82c0-9de70aa09bf1)


V. TỔNG KẾT

    - Tiếp tục nghiên cứu và phát triển ứng dụng hoàn thiện hơn, cải thiện hiệu xuất của ứng dụng
    
    - Nâng cao triển vọng của ứng dụng để áp dụng vào cuộc sống thực tế



#Ký Tên Tác Giả :
  Phan Vũ Linh
    





















