package com.example.e_comerce.service;

import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.Category;
import com.example.e_comerce.model.Product;
import com.example.e_comerce.model.Size;
import com.example.e_comerce.repository.CategoryRepository;
import com.example.e_comerce.repository.ProductRepository;
import com.example.e_comerce.request.CreateProductRequest;
import com.example.e_comerce.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplementationTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProductServiceImplementation productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct_Success() {
        CreateProductRequest request = new CreateProductRequest();
        request.setTitle("Phone");
        request.setDescription("Smartphone");
        request.setPrice(1000);
        request.setDicountedPrice(900);
        request.setDicountPercent(10);
        request.setBrand("BrandX");
        request.setColor("Black");
        request.setSize(Set.of(new Size("M", 5)));
        request.setImageUrl("http://image.jpg");
        request.setQuantity(5);
        request.setTopLevelCategory("Electronics");
        request.setSecondLevelCategory("Mobiles");
        request.setThirdLevelCategory("Smartphones");

        Category top = new Category();
        top.setName("Electronics");
        top.setLevel(1);

        Category mid = new Category();
        mid.setName("Mobiles");
        mid.setLevel(2);
        mid.setParentCategory(top);

        Category low = new Category();
        low.setName("Smartphones");
        low.setLevel(3);
        low.setParentCategory(mid);

        when(categoryRepository.findByNameIgnoreCase("Electronics")).thenReturn(List.of(top));
        when(categoryRepository.findByNameAndParent("Mobiles", top)).thenReturn(mid);
        when(categoryRepository.findByNameAndParent("Smartphones", mid)).thenReturn(low);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Phone", response.getTitle());
        assertEquals("Smartphones", response.getThirdLevelCategory());
    }

    @Test
    void testCreateProduct_MissingCategory() {
        CreateProductRequest request = new CreateProductRequest();
        request.setTopLevelCategory("Unknown");
        request.setSecondLevelCategory("Unknown");
        request.setThirdLevelCategory("Unknown");

        when(categoryRepository.findByNameIgnoreCase("Unknown")).thenReturn(Collections.emptyList());
        when(categoryRepository.findByNameAndParent(any(), any())).thenReturn(null);
        when(productRepository.save(any())).thenReturn(new Product());

        assertDoesNotThrow(() -> productService.createProduct(request));
    }

    @Test
    void testDeleteProduct_Success() throws ProductException {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        String result = productService.deleteProduct(1L);

        assertEquals("Product Deleted Sucessfully", result);
        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void testUpdateProduct_Success() throws ProductException {
        Product existing = new Product();
        existing.setId(1L);
        existing.setQuantity(5);

        Product update = new Product();
        update.setQuantity(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any())).thenReturn(existing);

        Product result = productService.updateProduct(1L, update);

        assertEquals(10, result.getQuantity());
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product update = new Product();
        update.setQuantity(10);

        assertThrows(ProductException.class, () -> productService.updateProduct(1L, update));
    }
}
