package vn.iotstar.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.iotstar.entity.User;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        String requestURI = request.getRequestURI();

        // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return false;
        }

        // Kiểm tra quyền truy cập các URL dành cho admin
        if (requestURI.startsWith(request.getContextPath() + "/admin/") ||
            requestURI.startsWith("/admin/")) {
            if (loggedInUser.getRole() != User.Role.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return false;
            }
        }

        // Kiểm tra quyền truy cập các URL dành cho user (không phải admin)
        if (requestURI.startsWith(request.getContextPath() + "/user/") ||
            requestURI.startsWith("/user/")) {
            if (loggedInUser.getRole() == User.Role.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return false;
            }
        }

        // Các URL chung như /products, /categories, /users sẽ được truy cập bởi cả admin và user
        // Các URL dành riêng cho admin: /admin/*
        // Các URL dành riêng cho user: /user/*

        return true;
    }
}
