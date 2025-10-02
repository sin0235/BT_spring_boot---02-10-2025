package vn.iotstar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private Integer quantity = 0;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(length = 500)
    private String images;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;
    
    private Integer discount = 0;
    
    private Boolean status = true;
    
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
    
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();
    
    // Convenience fields for form binding (transient - not stored in DB)
    @Transient
    private Integer categoryId;
    
    @Transient
    private Integer userId;
    
    // Constructors
    public Product() {}
    
    public Product(String title, Integer quantity, String description, BigDecimal price, 
                   String images, Category category, User user, Integer discount, 
                   Boolean status, Integer sortOrder) {
        this.title = title;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
        this.images = images;
        this.category = category;
        this.user = user;
        this.discount = discount;
        this.status = status;
        this.sortOrder = sortOrder;
        this.createDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getImages() {
        return images;
    }
    
    public void setImages(String images) {
        this.images = images;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
        // Sync the convenience field
        this.categoryId = (category != null) ? category.getId() : null;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
        // Sync the convenience field
        this.userId = (user != null) ? user.getId() : null;
    }
    
    public Integer getDiscount() {
        return discount;
    }
    
    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
    
    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    
    // Convenience getters and setters for form binding
    public Integer getCategoryId() {
        return (category != null) ? category.getId() : categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public Integer getUserId() {
        return (user != null) ? user.getId() : userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}