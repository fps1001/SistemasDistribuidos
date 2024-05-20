package com.ubu.sistdist.taller_coches.Controllers;

import com.ubu.sistdist.taller_coches.Services.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String MostrarPantallaLogin(Model model) {
        return "login";
    }


    @PostMapping("/login")
    public String controlLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        if (userService.validateAuthentication(username,password)){
            System.out.println("Valid username and password");
            return "welcome";
        }
        else {
            System.out.println("Invalid username and password");
            return "loginerror";
        }

    }

}
