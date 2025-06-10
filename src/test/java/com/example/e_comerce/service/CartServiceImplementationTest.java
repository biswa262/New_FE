package com.example.e_comerce.service;

import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.*;
import com.example.e_comerce.repository.CartRepository;
import com.example.e_comerce.request.AddItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImplementation cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart() {
        User user = new User();
        Cart cart = new Cart();
        cart.setUser(user);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart createdCart = cartService.createCart(user);

        assertNotNull(createdCart);
        assertEquals(user, createdCart.getUser());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddCartItem_NewItem() throws ProductException {
        Long userId = 1L;
        AddItemRequest req = new AddItemRequest();
        req.setProductId(100L);
        req.setQuantity(2);
        req.setSize("M");

        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());

        Product product = new Product();
        product.setId(100L);
        product.setDiscountedPrice(50);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(productService.findProductById(100L)).thenReturn(product);
        when(cartItemService.isCartItemExist(cart, product, "M", userId)).thenReturn(null);

        CartItem newItem = new CartItem();
        newItem.setProduct(product);
        newItem.setQuantity(2);
        newItem.setSize("M");
        newItem.setPrice(100);

        when(cartItemService.createCartItem(any(CartItem.class))).thenReturn(newItem);

        CartItem result = cartService.addCartItem(userId, req);

        assertNotNull(result);
        assertEquals(100, result.getPrice());
        verify(cartItemService).createCartItem(any(CartItem.class));
    }

    @Test
    void testFindUserCart() {
        Long userId = 1L;
        Cart cart = new Cart();
        CartItem item1 = new CartItem();
        item1.setPrice(100);
        item1.setDiscountedPrice(80);
        item1.setQuantity(1);

        CartItem item2 = new CartItem();
        item2.setPrice(200);
        item2.setDiscountedPrice(150);
        item2.setQuantity(2);

        cart.setCartItems(new HashSet<>());
        cart.getCartItems().add(item1);
        cart.getCartItems().add(item2);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.findUserCart(userId);

        assertEquals(300, result.getTotalPrice());
        assertEquals(230, result.getTotalDiscountedPrice());
        assertEquals(3, result.getTotalItem());
        assertEquals(70, result.getDiscount());
    }
}
