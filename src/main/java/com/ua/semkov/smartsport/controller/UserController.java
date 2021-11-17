package com.ua.semkov.smartsport.controller;


import com.ua.semkov.smartsport.entity.User;
import com.ua.semkov.smartsport.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


/**
 * @author Semkov.S
 */

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/sign-in")
    ModelAndView signIn(@RequestParam(value = "error", defaultValue = "false") boolean loginError) {
        return new ModelAndView("/user/sign-in");
    }

    @GetMapping("/sign-up")
    ModelAndView signUpPage() {
        ModelAndView mav = new ModelAndView("user/sign-up");

        mav.addObject("user", new User());
        return mav;
    }

    @PostMapping("/sign-up")
    ModelAndView signUp(@ModelAttribute @Valid User user, BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mav.setViewName("user/sign-up");
        } else {
            userService.create(user);
            mav.setViewName("redirect:/sign-in");
        }
        return mav;
    }



    @GetMapping("/update/editUser/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long userId) {

        ModelAndView mav = new ModelAndView("/user/updateUserForm");

        User user = userService.getById(userId);

        mav.addObject("user", user);

        return mav;
    }

    @PostMapping("/update/updateUser/{id}")
    public ModelAndView updateEvent(@PathVariable("id") Long id,
                                    @Valid User user,
                                    BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView("redirect:/" + "userProfile");

        if (bindingResult.hasErrors()) {
            mav.setViewName("/user/updateUserForm");
        } else {
            userService.update(user);
            mav.addObject("user", user);
        }

        return mav;
    }




}
