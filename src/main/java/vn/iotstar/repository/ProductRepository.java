package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;
import vn.iotstar.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    // Tìm kiếm theo title với phân trang
    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Tìm tất cả products với phân trang
    @NonNull
    Page<Product> findAll(@NonNull Pageable pageable);
    
    // Tìm products theo category với phân trang
    Page<Product> findByCategory_Id(Integer categoryId, Pageable pageable);
    
    // Tìm products theo category (không phân trang)
    List<Product> findByCategory_Id(Integer categoryId);
    
    // Tìm products theo user với phân trang
    Page<Product> findByUser_Id(Integer userId, Pageable pageable);
    
    // Tìm products theo user (không phân trang)
    List<Product> findByUser_Id(Integer userId);
    
    // Tìm products theo status
    Page<Product> findByStatus(Boolean status, Pageable pageable);
    
    // Tìm products có giá trong khoảng
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    // Tìm products theo category và status
    Page<Product> findByCategory_IdAndStatus(Integer categoryId, Boolean status, Pageable pageable);
    
    // Products được sắp xếp theo price tăng dần
    Page<Product> findByStatusOrderByPriceAsc(Boolean status, Pageable pageable);
    
    // Tìm tất cả products sắp xếp theo price tăng dần (không phân trang)
    List<Product> findAllByOrderByPriceAsc();
    
    // Products được sắp xếp theo price giảm dần
    Page<Product> findByStatusOrderByPriceDesc(Boolean status, Pageable pageable);
    
    // Products được sắp xếp theo ngày tạo mới nhất
    Page<Product> findByStatusOrderByCreateDateDesc(Boolean status, Pageable pageable);
    
    // Tìm tất cả products sắp xếp theo ngày tạo mới nhất (không phân trang)
    List<Product> findAllByOrderByCreateDateDesc();
    
    // Tìm products có quantity > 0 (còn hàng)
    Page<Product> findByQuantityGreaterThanAndStatus(Integer quantity, Boolean status, Pageable pageable);
    
    // Tìm product với relations được load
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.user WHERE p.id = :id")
    Optional<Product> findByIdWithRelations(@Param("id") Integer id);
    
    // Tìm kiếm theo title và category với JPQL query
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')) AND p.category.id = :categoryId")
    Page<Product> findByTitleContainingIgnoreCaseAndCategoryId(@Param("title") String title, @Param("categoryId") Integer categoryId, Pageable pageable);
    
    // Đếm products theo category
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    Long countByCategoryId(@Param("categoryId") Integer categoryId);
    
    // Đếm products theo user
    @Query("SELECT COUNT(p) FROM Product p WHERE p.user.id = :userId")
    Long countByUserId(@Param("userId") Integer userId);
    
    // Tìm top products theo số lượng bán (giả sử có field salesCount)
    @Query("SELECT p FROM Product p WHERE p.status = true ORDER BY p.quantity DESC")
    List<Product> findTopSellingProducts(Pageable pageable);
    
    // Tìm products mới nhất
    @Query("SELECT p FROM Product p WHERE p.status = true ORDER BY p.createDate DESC")
    List<Product> findLatestProducts(Pageable pageable);
    
    // Tìm products có discount
    Page<Product> findByDiscountGreaterThanAndStatus(Integer discount, Boolean status, Pageable pageable);
    
    // Tìm kiếm tổng hợp (title, description)
    @Query("SELECT p FROM Product p WHERE p.status = true AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
    
    // Validation methods - kiểm tra tồn tại
    boolean existsByTitleIgnoreCase(String title);
    
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE LOWER(p.title) = LOWER(:title) AND p.id != :id")
    boolean existsByTitleIgnoreCaseAndIdNot(@Param("title") String title, @Param("id") Integer id);
    
    // Đếm tổng số products
    long count();
    
    // Đếm products active
    long countByStatus(Boolean status);
}