package com.example.atmproject.controller;

import com.example.atmproject.entity.User;
import com.example.atmproject.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/homepage")
    public String homePage(Model model) {
        // User user = userRepository.findByUserName(principal.getName());
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username);
        model.addAttribute("user", user);
        return "homepage";
    }

    @GetMapping("/deposit")
    public String depositPage(Model model) {
        //User user = userRepository.findByUserName(principal.getName());
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username);
        model.addAttribute("user", user);

        return "deposit";
    }

    // @CurrentSecurityContext(expression =
    //         "authentication?.name") String name,
    @PostMapping("/deposit")
    public String depositPage(@RequestParam("amount") double amount) {
        //User user = userRepository.findByUserName(name);
        //User user = userRepository.findByUserName(principal.getName());
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username);
        Double currentBalance = user.getBalance();
        Double updatedBalance = currentBalance + amount;
        user.setUserName(user.getUserName());
        user.setRoles(user.getRoles());
        user.setPassword(user.getPassword());
        user.setId(user.getId());
        user.setBalance(updatedBalance);
        userRepository.save(user);
        return "redirect:/homepage";
    }

    @GetMapping("/withdraw")
    public String withDrawPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username);
        model.addAttribute("user", user);

        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withDraw(@RequestParam("withdrawamount") double amount){
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username);
        Double currentBalance = user.getBalance();
        if(currentBalance > 0 && (amount<=currentBalance || amount == currentBalance)){
            Double updatedBalance = currentBalance - amount;
            user.setUserName(user.getUserName());
            user.setRoles(user.getRoles());
            user.setPassword(user.getPassword());
            user.setId(user.getId());
            user.setBalance(updatedBalance);
        }


        userRepository.save(user);
        return "redirect:/homepage";
    }


}