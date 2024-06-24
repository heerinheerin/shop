package com.shop.repository;

import com.shop.entity.Cartitem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<Cartitem, Long> {

    Cartitem findByCartIdAndItemId(Long cartId, Long itemId);
}

