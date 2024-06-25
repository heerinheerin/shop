package com.shop.service;
import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.dto.OrderDto;
import com.shop.entity.Cart;
import com.shop.entity.Cartitem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        Cartitem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(),item.getId());
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        }
        else {
            Cartitem catItem = Cartitem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(catItem);
            return catItem.getId();
        }


        }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            return cartDetailDtoList;
        }
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;

    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        // 아매알울 서용해서 멤버 엔티티 객체 추출
        Member curMember = memberRepository.findByEmail(email);
        //카트 아이템아이디를 이용해서 카트 아이템 엔티티 객체 추출
        Cartitem cartitem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityExistsException::new);
        // Cart->Member 엔티티 객체 추출
        Member savedMember = cartitem.getCart().getMember();
//현재 로그인 된 멤버 == 카트 아이템에 있는 멤버 -> 같지 않으면 return false
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        //현재 로그인 된 멤버 == 카트 아이템에 있는 멤버 ->eturn true
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count) {
        Cartitem cartitem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityExistsException::new);
        cartitem.updateCount(count);
    }
    public void deleteCarItem(Long cartItemId){
        Cartitem cartitem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityExistsException::new);
        cartItemRepository.delete(cartitem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        // 주문 DTO List 객체 생성
        List<OrderDto> orderDtoList = new ArrayList<>();
        // 카트 주문 리스트에 있는 목록을 -> 카트 아이템 객체로 추출
        // 주문 Dto에 CartItem 정보를 담고, 위에 선언된 주문 Dto List에 추가
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            Cartitem cartitem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityExistsException::new);
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartitem.getItem().getId());
            orderDto.setCount(cartitem.getCount());
            orderDtoList.add(orderDto);
        }
        // 주문 Dto리스트 현재 로그인된 이메일 매개변수 넣고, 주문 서비스 실행
        Long orderId = orderService.orders(orderDtoList, email);

        //카트에 있던 아이템 주문이 진행 되면, 카트아이템이 모두 삭제됨
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            Cartitem cartitem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityExistsException::new);
            cartItemRepository.delete(cartitem);

        }
        return orderId;
    }
    }

