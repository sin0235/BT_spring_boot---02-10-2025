package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String loginForm(HttpSession session, Model model) {
        // Nếu đã đăng nhập thì chuyển hướng về trang chủ tương ứng
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return "redirect:" + getRedirectPath(loggedInUser.getRole());
        }
        return "auth/login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@RequestParam String email,
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {

        // Tìm user theo email
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại!");
            return "redirect:/auth/login";
        }

        User user = userOpt.get();

        // Kiểm tra mật khẩu (đơn giản, trong thực tế nên dùng hash như BCrypt)
        // Ở đây tạm thời kiểm tra trực tiếp vì dữ liệu mẫu chưa được hash
        if (!user.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu không đúng!");
            return "redirect:/auth/login";
        }

        // Lưu thông tin user vào session
        session.setAttribute("loggedInUser", user);

        // Chuyển hướng về trang chủ tương ứng với role
        return "redirect:" + getRedirectPath(user.getRole());
    }

    // Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        return "redirect:/auth/login";
    }


    // Lấy đường dẫn chuyển hướng theo role
    private String getRedirectPath(User.Role role) {
        if (role == User.Role.ADMIN) {
            return "/admin/dashboard";
        } else {
            return "/user/dashboard";
        }
    }
}
