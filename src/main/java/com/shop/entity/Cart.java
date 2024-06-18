package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@ToString

public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne //1:1맵핑
    @JoinColumn(name = "member_id") // Joincolumn 맵핑할 외래키를 지정합니다. 외래키 이름 설정
    //name을 명시하지 않으면 JPQ가 알아서 ID를 찾지만 원하는 이름이 아닐 수 있음.
    private Member member;

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
