package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MailService;
import com.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private  final PasswordEncoder passwordEncoder;
    private  final MailService mailService;


    String confirm = "";

    boolean confirmCheck =false;

    @GetMapping(value = "/new")
    public  String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @GetMapping(value = "/popup/{str}")
    public  String memberM(@PathVariable String str){
        System.out.println("000000000000000000000000000");
        if (str.equals("email")){
            return "popup/emailp";
        }else if(str.equals("cart")){
            return "popup/cartp";
        } else if (str.equals("order")) {
            return "popup/orderp";
        } else if (str.equals("cancle")) {
            return "popup/canclep";
        } else if (str.equals("email2")){
            return "popup/email2p";
        }
        return "member/emailp";

    }



    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult
            , Model model){
        //@valid 붙은 객체를 검사해서 결과에 에러가 있으면 실행.
        if (bindingResult.hasErrors()){
            System.out.println("오류1");
            return "member/memberForm";//다시 회원가입으로 돌려보냄.
        }
        if(!confirmCheck){
            model.addAttribute("errorMessage", "이메일 인증을 하세요");
            return "member/memberForm";
        }
        //결과에 문제가 없으면 내려옴.
        try{
            //멤버 객체 생성. 데이터 베이스에 저장.
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);


        }
        //만약 같은 메일이 있으면 아래로 들어감.
        catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return  "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return  "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }

    // 이메일 인증
    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity mailConfirm(@PathVariable("email") String email) throws Exception {
        System.out.println("email");

        confirm = mailService.sendSimpleMessage(email);
        System.out.println("인증코드 : " + confirm);
        String jsonResponse = "{ \"message\": \"인증 메일을 보냈습니다.\" }";

        return new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
    }
    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codefirm(@PathVariable("code")String code)
        throws Exception{
        System.out.println("악@@@@@@!!!");
        if (confirm.equals(code)) {
            confirmCheck=true;
            String jsonResponse = "{ \"message\": \"인증을 성공했습니다\" }";
            return new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증 코드를 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);

    }

    }
