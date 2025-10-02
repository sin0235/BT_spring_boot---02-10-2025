package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;
import vn.iotstar.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    // Tìm user theo email
    Optional<User> findByEmail(String email);
    
    // Tìm user theo email và password (for login)
    Optional<User> findByEmailAndPassword(String email, String password);
    
    // Tìm user theo phone
    Optional<User> findByPhone(String phone);
    
    // Tìm kiếm theo tên với phân trang
    Page<User> findByFullnameContainingIgnoreCase(String fullname, Pageable pageable);
    
    // Tìm kiếm theo tên (không phân trang)
    List<User> findByFullnameContaining(String fullname);
    
    // Tìm kiếm theo email với phân trang
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    
    // Tìm tất cả users với phân trang
    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);
    
    // Kiểm tra email đã tồn tại
    boolean existsByEmail(String email);
    
    // Kiểm tra email đã tồn tại với ID khác (cho update)
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Integer id);
    
    // Kiểm tra phone đã tồn tại
    boolean existsByPhone(String phone);
    
    // Kiểm tra phone đã tồn tại với ID khác (cho update)
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.phone = :phone AND u.id != :id")
    boolean existsByPhoneAndIdNot(@Param("phone") String phone, @Param("id") Integer id);
    
    // Tìm users theo danh mục yêu thích
    @Query("SELECT u FROM User u JOIN u.categories c WHERE c.id = :categoryId")
    List<User> findUsersByFavoriteCategory(@Param("categoryId") Integer categoryId);
    
    // Đếm tổng số users
    long count();
}