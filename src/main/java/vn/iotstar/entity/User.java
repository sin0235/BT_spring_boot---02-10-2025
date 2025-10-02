package vn.iotstar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "User")
public class User {
    
    public enum Role {
        ADMIN, USER
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String fullname;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(length = 20)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER; // Default role is USER
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "UserCategory",
        joinColumns = @JoinColumn(name = "userid"),
        inverseJoinColumns = @JoinColumn(name = "categoryid")
    )
    @JsonIgnore
    private List<Category> categories;
    
    // Constructors
    public User() {}
    
    public User(String fullname, String email, String password, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = Role.USER;
    }
    
    public User(String fullname, String email, String password, String phone, Role role) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getFullname() {
        return fullname;
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public List<Category> getCategories() {
        return categories;
    }
    
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}