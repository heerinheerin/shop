package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity // 나 엔티티야
@Table(name = "member")//테이블 명
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    //기본키 컬럼명 member_id AI->데이터 저장시 1씩 증가
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //알아서 설정
    private String name;
    //중복 허용 안됨.
    @Column(unique = true)
    private String email;
    //패스워드, 주소는 알아서
    private String password;
    private String address;

    private String callnum;

    //Enum->컴 : 숫자, 우리: 문자
    //데이터베이스 문자 그대로.> User, Admin
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder) { //회원가입하는거
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());

        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;

    }

}
