package vn.iotstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.iotstar.validator.StrongPassword;
import vn.iotstar.validator.VietnamesePhone;

@Entity
@Table(name = "User")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Role {
        ADMIN, USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 255, message = "Họ tên không được quá 255 ký tự")
    @Column(name = "fullname", nullable = false)
    private String fullname;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email không được quá 255 ký tự")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @StrongPassword
    @Column(name = "password", nullable = false)
    private String password;

    @VietnamesePhone
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "Vai trò không được để trống")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER; // Mặc định là USER

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "UserCategory",
        joinColumns = @JoinColumn(name = "userid"),
        inverseJoinColumns = @JoinColumn(name = "categoryid")
    )
    private List<Category> categories;

    // Constructors
    public User() {}

    public User(String fullname, String email, String password, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = Role.USER; // Mặc định là USER
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


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                '}';
    }
}