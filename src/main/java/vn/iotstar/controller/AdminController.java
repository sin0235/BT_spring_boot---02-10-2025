package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Kiểm tra quyền admin
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || loggedInUser.getRole() != User.Role.ADMIN) {
            return "redirect:/auth/login";
        }

        model.addAttribute("title", "Admin Dashboard - Category, Product & User Management");
        model.addAttribute("loggedInUser", loggedInUser);
        return "admin/dashboard";
    }

    @GetMapping("")
    public String adminHome(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || loggedInUser.getRole() != User.Role.ADMIN) {
            return "redirect:/auth/login";
        }
        return "redirect:/admin/dashboard";
    }
}
