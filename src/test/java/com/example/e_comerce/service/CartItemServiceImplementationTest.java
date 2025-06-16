package com.example.e_comerce.service;
import com.example.e_comerce.exception.CartItemException;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.*;
import com.example.e_comerce.repository.CartItemRepository;
import com.example.e_comerce.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartItemServiceImplementationTest {

	@Mock
    private UserService userService;

    @Mock
    private CartRepository cartRepository;
    

    @Mock
    private CartItemRepository cartItemRepository;


    @InjectMocks
    private CartItemServiceImplementation cartItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCartItem() {
        Product product = new Product();
        product.setPrice(100);
        product.setDiscountedPrice(80);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);

        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> i.getArgument(0));

        CartItem result = cartItemService.createCartItem(cartItem);

        assertEquals(1, result.getQuantity());
        assertEquals(100, result.getPrice());
        assertEquals(80, result.getDiscountedPrice());
    }

    @Test
    void testUpdateCartItem_Success() throws CartItemException, UserException {
        Long userId = 1L;
        Long itemId = 10L;

        Product product = new Product();
        product.setPrice(100);
        product.setDiscountedPrice(80);

        CartItem existingItem = new CartItem();
        existingItem.setId(itemId);
        existingItem.setUserId(userId);
        existingItem.setProduct(product);

        CartItem updateRequest = new CartItem();
        updateRequest.setQuantity(3);

        User user = new User();
        user.setId(userId);

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(userService.findUserById(userId)).thenReturn(user);
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> i.getArgument(0));

        CartItem updated = cartItemService.updateCartItem(userId, itemId, updateRequest);

        assertEquals(3, updated.getQuantity());
        assertEquals(300, updated.getPrice());
        assertEquals(240, updated.getDiscountedPrice());
    }

    @Test
    void testUpdateCartItem_Unauthorized() throws UserException {
        Long userId = 1L;
        Long itemId = 10L;

        CartItem existingItem = new CartItem();
        existingItem.setId(itemId);
        existingItem.setUserId(2L); // Different user

        User user = new User();
        user.setId(2L);

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(userService.findUserById(2L)).thenReturn(user);

        assertThrows(CartItemException.class, () -> {
            cartItemService.updateCartItem(userId, itemId, new CartItem());
        });
    }

    @Test
    void testRemoveCartItem_Success() throws CartItemException, UserException {
        Long userId = 1L;
        Long itemId = 5L;

        CartItem item = new CartItem();
        item.setId(itemId);
        item.setUserId(userId);

        User user = new User();
        user.setId(userId);

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userService.findUserById(userId)).thenReturn(user);

        cartItemService.removeCartItem(userId, itemId);

        verify(cartItemRepository).deleteById(itemId);
    }

    @Test
    void testRemoveCartItem_Unauthorized() throws CartItemException, UserException {
        Long userId = 1L;
        Long itemId = 5L;

        CartItem item = new CartItem();
        item.setId(itemId);
        item.setUserId(2L); // Different user

        User actualUser = new User();
        actualUser.setId(2L);

        User requestUser = new User();
        requestUser.setId(userId);

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userService.findUserById(2L)).thenReturn(actualUser);
        when(userService.findUserById(userId)).thenReturn(requestUser);

        assertThrows(UserException.class, () -> {
            cartItemService.removeCartItem(userId, itemId);
        });
    }

    @Test
    void testFindCartItemById_Found() throws CartItemException {
        CartItem item = new CartItem();
        item.setId(1L);

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(item));

        CartItem result = cartItemService.findCartItemById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testFindCartItemById_NotFound() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CartItemException.class, () -> {
            cartItemService.findCartItemById(1L);
        });
    }

    @Test
    void testGetCartItemCount() {
        when(cartItemRepository.countByUserId(1L)).thenReturn(5);

        int count = cartItemService.getCartItemCount(1L);

        assertEquals(5, count);
    }
}
