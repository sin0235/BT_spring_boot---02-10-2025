package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    private final String CATEGORY_UPLOAD_DIR = "uploads/categories/";
    
    // Redirect từ root về categories
    @GetMapping("/")
    public String redirectToCategories() {
        return "redirect:/categories";
    }
    
    @GetMapping
    public String listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String keyword,
            HttpSession session,
            Model model) {

        // Thêm thông tin user đăng nhập vào model
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        
        // Tạo Pageable object
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Tìm kiếm với phân trang
        Page<Category> categoryPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryPage = categoryService.searchByName(keyword, pageable);
        } else {
            categoryPage = categoryService.findAll(pageable);
        }
        
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("totalItems", categoryPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        
        return "categories/list";
    }
    
    @GetMapping("/new")
    public String newCategoryForm(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "categories/form";
    }
    
    @GetMapping("/view/{id}")
    public String viewCategory(@PathVariable Integer id, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            model.addAttribute("category", category);
            return "categories/view";
        }
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, HttpSession session, Model model) {
        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            model.addAttribute("category", category);
            model.addAttribute("isEdit", true);
            return "categories/form";
        }
        return "redirect:/categories";
    }
    
    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute Category category,
                              BindingResult result,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes,
                              HttpSession session,
                              Model model) {

        // Thêm thông tin user đăng nhập vào model để hiển thị trên form
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        // Kiểm tra validation errors
        if (result.hasErrors()) {
            model.addAttribute("isEdit", category.getId() != null);
            return "categories/form";
        }

        // Xử lý upload hình ảnh
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Tạo tên file unique
                String originalFilename = imageFile.getOriginalFilename();
                if (originalFilename == null || !originalFilename.contains(".")) {
                    throw new IllegalArgumentException("File không hợp lệ");
                }
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID().toString() + extension;

                // Lưu file vào thư mục uploads/categories/
                Path uploadPath = Paths.get(CATEGORY_UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(newFilename);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Cập nhật tên file vào category
                category.setImages(newFilename);

            } catch (IOException e) {
                model.addAttribute("error", "Lỗi khi upload hình ảnh: " + e.getMessage());
                model.addAttribute("isEdit", category.getId() != null);
                return "categories/form";
            }
        } else if (category.getId() != null) {
            // Khi chỉnh sửa mà không upload hình mới, giữ nguyên hình cũ
            Optional<Category> existingCategory = categoryService.findById(category.getId());
            if (existingCategory.isPresent()) {
                category.setImages(existingCategory.get().getImages());
            }
        }

        // Kiểm tra tên category đã tồn tại
        try {
            if (category.getId() == null) {
                // Tạo mới
                if (categoryService.existsByName(category.getName())) {
                    model.addAttribute("error", "Tên category đã tồn tại!");
                    return "categories/form";
                }
            } else {
                // Cập nhật
                if (categoryService.existsByNameAndNotId(category.getName(), category.getId())) {
                    model.addAttribute("error", "Tên category đã tồn tại!");
                    return "categories/form";
                }
            }

            categoryService.save(category);
            redirectAttributes.addFlashAttribute("successMessage",
                category.getId() == null ? "Tạo category thành công!" : "Cập nhật category thành công!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/categories";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Category> categoryOpt = categoryService.findById(id);
            if (categoryOpt.isPresent()) {
                categoryService.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Xóa category thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Category không tồn tại!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa category: " + e.getMessage());
        }
        
        return "redirect:/categories";
    }
}