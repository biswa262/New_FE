package com.example.e_comerce.repository;

import com.example.e_comerce.model.Cart;
import com.example.e_comerce.model.CartItem;
import com.example.e_comerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("SELECT ci FROM CartItem ci where ci.cart=:cart and ci.product=:product and ci.size=:size and ci.userId=:userId")
    public CartItem isCartItemExist(@Param("cart")Cart cart, @Param("product")Product product, @Param("size")String size,@Param("userId")Long userId);

    int countByUserId(Long userId);
}
