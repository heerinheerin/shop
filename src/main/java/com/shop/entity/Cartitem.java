package com.shop.entity;

import com.shop.dto.CartItemDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class Cartitem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")

    private Item item;
    private int count;

    public static Cartitem createCartItem(Cart cart, Item item, int count) {
        Cartitem cartItem = new Cartitem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;

    }
    public void addCount(int count) { this.count += count; }
}
