package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/user")
public class UserDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Kiểm tra quyền user (không phải admin)
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || loggedInUser.getRole() == User.Role.ADMIN) {
            return "redirect:/auth/login";
        }

        model.addAttribute("loggedInUser", loggedInUser);

        // Thống kê cơ bản
        model.addAttribute("productCount", productService.count());
        model.addAttribute("categoryCount", categoryService.count());
        model.addAttribute("userCount", userService.count());
        model.addAttribute("orderCount", 0); // Có thể thêm bảng Order sau

        return "user/dashboard";
    }
}
