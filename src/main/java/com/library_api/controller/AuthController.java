package com.library_api.controller;

import com.library_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.library_api.model.User;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    // страница входа
    @GetMapping("/login")
    public String login() {
        return "login"; // вернет шаблон login.html
    }

    //страница регистрации
    @GetMapping("/register")
    public String registerForm() {
        return "register"; //вернет шаблон register.html
    }

    // обработка регистрации
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String fullName,
            Model model) {
        try {
            userService.registerNewUser(username, password, email, fullName);
            return "redirect:/login?registered"; //успешно - на логин
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage()); // ошибка- показывается на форме
            return "register";
        }
    }

    //страница запроса восстановления пароля
    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "forgot-password";
    }

    //обратботка запроса восстанволения пароля
    @PostMapping("/forgot-password")
    public  String forgotPassword(@RequestParam String email, Model model) {
        try {
            userService.createPasswordResetToken(email);
            model.addAttribute("success", "Ссылка для сброса пароля отправлена на Ваш e-mail");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "forgot-password";
    }

    //страница сброса пароля по токену
    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam String token, Model model) {
        if (userService.validatePasswordResetToken(token)) {
            model.addAttribute("token", token);
            return "reset-password";
        } else {
            model.addAttribute("erorr", "Недействительная или просроченная ссылка");
            return "forgot-password";
        }
    }

    //обработка сброса пароля

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("token", token);
            return "reset-password";
        }
        try {
            userService.resetPassword(token, password);
            model.addAttribute("success", "Пароль успешно изменен! Войдите с новым паролем.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            return "reset-password";
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        // Форматируем дату в Java
        if (user.getCreatedAt() != null) {
            String formattedDate = user.getCreatedAt().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            );
            model.addAttribute("formattedDate", formattedDate);
        } else {
            model.addAttribute("formattedDate", "Не указана");
        }

        return  "profile";
    }
    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String email,
            @RequestParam String fullName,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        try {
            userService.updateProfile(username, email, fullName);
            redirectAttributes.addFlashAttribute("success", "Профиль успешно обновлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

            String username = authentication.getName();

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Новые пароли не совпадают");
                return "redirect:/profile";
            }

            try {
                userService.changePassword(username, currentPassword, newPassword);
                redirectAttributes.addFlashAttribute("success", "Паротль успешно изменен!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
             return "redirect:/profile";

        }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("fullName", user.getFullName());
        return "index";
    }



}