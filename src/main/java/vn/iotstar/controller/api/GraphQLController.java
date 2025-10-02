package vn.iotstar.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.UserService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/graphql")
public class GraphQLController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(GraphQLController.class);

    // Product REST endpoints
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.findAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error getting all products", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        try {
            Integer productId = Integer.parseInt(id);
            Optional<Product> product = productService.findById(productId);
            return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            logger.error("Invalid product ID format: {}", id);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/products/price/{id}")
    public ResponseEntity<String> getProductPrice(@PathVariable String id) {
        try {
            Integer productId = Integer.parseInt(id);
            Optional<Product> product = productService.findById(productId);
            return product.map(p -> ResponseEntity.ok(p.getPrice() != null ? p.getPrice().toString() : null))
                         .orElse(ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            logger.error("Invalid product ID format: {}", id);
            return ResponseEntity.badRequest().build();
        }
    }

    // Category REST endpoints
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryService.findAll();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Error getting all categories", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable String id) {
        try {
            Integer categoryId = Integer.parseInt(id);
            Optional<Category> category = categoryService.findById(categoryId);
            return category.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            logger.error("Invalid category ID format: {}", id);
            return ResponseEntity.badRequest().build();
        }
    }

    // User REST endpoints
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error getting all users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        try {
            Integer userId = Integer.parseInt(id);
            Optional<User> user = userService.findById(userId);
            return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format: {}", id);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody Map<String, Object> input) {
        try {
            // Validate required fields
            String fullname = (String) input.get("fullname");
            String email = (String) input.get("email");
            String password = (String) input.get("password");

            if (fullname == null || fullname.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Validate email format and uniqueness
            if (userService.existsByEmail(email)) {
                return ResponseEntity.badRequest().build();
            }

            User user = new User();
            user.setFullname(fullname.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPassword(password);
            user.setPhone((String) input.get("phone"));

            // Handle categories if provided
            @SuppressWarnings("unchecked")
            List<Integer> categoryIds = (List<Integer>) input.get("categoryIds");
            if (categoryIds != null && !categoryIds.isEmpty()) {
                List<Category> categories = categoryService.findAllById(categoryIds);
                user.setCategories(categories);
            }

            User savedUser = userService.save(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Map<String, Object> input) {
        try {
            // Validate required fields
            String name = (String) input.get("name");
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Validate category name uniqueness
            if (categoryService.existsByName(name)) {
                return ResponseEntity.badRequest().build();
            }

            Category category = new Category();
            category.setName(name.trim());
            category.setImages((String) input.get("images"));

            Category savedCategory = categoryService.save(category);
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            logger.error("Error creating category", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> input) {
        try {
            // Validate required fields
            String title = (String) input.get("title");
            Object quantityObj = input.get("quantity");
            Object priceObj = input.get("price");
            Object userIdObj = input.get("userId");

            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (quantityObj == null || priceObj == null || userIdObj == null) {
                return ResponseEntity.badRequest().build();
            }

            // Validate title uniqueness
            if (productService.existsByTitle(title)) {
                return ResponseEntity.badRequest().build();
            }

            // Validate quantity
            Integer quantity;
            if (quantityObj instanceof Integer) {
                quantity = (Integer) quantityObj;
            } else {
                quantity = Integer.parseInt(quantityObj.toString());
            }
            if (quantity < 0) {
                return ResponseEntity.badRequest().build();
            }

            // Validate price
            BigDecimal price;
            if (priceObj instanceof Double) {
                price = BigDecimal.valueOf((Double) priceObj);
            } else if (priceObj instanceof Integer) {
                price = BigDecimal.valueOf((Integer) priceObj);
            } else {
                price = new BigDecimal(priceObj.toString());
            }
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().build();
            }

            // Validate user exists
            Integer userId = (Integer) userIdObj;
            Optional<User> user = userService.findById(userId);
            if (!user.isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            Product product = new Product();
            product.setTitle(title.trim());
            product.setQuantity(quantity);
            product.setDescription((String) input.get("description"));
            product.setPrice(price);
            product.setUser(user.get());

            // Handle images if provided
            String images = (String) input.get("images");
            if (images != null) {
                product.setImages(images);
            }

            // Set category if provided
            Object categoryIdObj = input.get("categoryId");
            if (categoryIdObj != null) {
                Integer categoryId = (Integer) categoryIdObj;
                Optional<Category> category = categoryService.findById(categoryId);
                if (category.isPresent()) {
                    product.setCategory(category.get());
                }
            }

            Product savedProduct = productService.save(product);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            logger.error("Error creating product", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}