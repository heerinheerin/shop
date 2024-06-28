package com.shop.controller;

import com.shop.repository.MailServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
public class AccountController {

    @PostMapping(value = "/login/mailConfirm")
    public @ResponseBody String mailConfirm (@RequestParam("email") String email) throws Exception {
        MailServiceInter registerMail = null;
        String code = registerMail.sendSimpleMessage(email);

        return code;
    }


}
