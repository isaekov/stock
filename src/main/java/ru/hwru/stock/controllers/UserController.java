package ru.hwru.stock.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hwru.stock.models.UserModel;
import ru.hwru.stock.services.UserService;

@Controller
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("registration")
    public String registration(Model model) {
        model.addAttribute("userModel", new UserModel());
        return "user/registration";
    }

    @PostMapping("registration")
    public String createUser(UserModel userModel, Model model) {

        try {
            userService.createUser(userModel);
        } catch (RuntimeException e) {
            model.addAttribute("message", e.getMessage());
            return "user/registration";
        }

        return "redirect:/login";
    }

    @GetMapping("login")
    public String login(@RequestParam(name = "error", required = false) String error) {
        System.out.println(error);
        return "user/login";
    }

    @GetMapping
    public String index() {
        return "index";
    }

}
