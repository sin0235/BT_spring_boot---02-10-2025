package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // Nếu đã đăng nhập, chuyển hướng đến trang chủ tương ứng với role
            if (loggedInUser.getRole() == User.Role.ADMIN) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/dashboard";
            }
        }
        // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
        return "redirect:/auth/login";
    }

}
