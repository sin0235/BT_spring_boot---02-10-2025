package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;
import vn.iotstar.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    // Tìm kiếm theo tên category với phân trang
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Tìm tất cả categories với phân trang
    @NonNull
    Page<Category> findAll(@NonNull Pageable pageable);

    // Kiểm tra tên category đã tồn tại
    boolean existsByNameIgnoreCase(String name);

    // Kiểm tra tên category đã tồn tại với ID khác (để update)
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.name) = LOWER(:name) AND c.id != :id")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") Integer id);

    // Đếm tổng số categories
    long count();

    // Tìm category theo tên
    Optional<Category> findByName(String name);
    
    // Tìm các danh mục có tên chứa (không phân biệt chữ hoa chữ thường)
    List<Category> findByNameContainingIgnoreCase(String name);
    
    // Tìm tất cả danh mục được sắp xếp theo thứ tự tăng dần
    List<Category> findAllByOrderBySortOrderAsc();
    
    // Tìm danh mục có sản phẩm
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.products WHERE c.products IS NOT EMPTY")
    List<Category> findCategoriesWithProducts();
    
    // Đếm sản phẩm trong danh mục
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    Long countProductsInCategory(@Param("categoryId") Integer categoryId);
}