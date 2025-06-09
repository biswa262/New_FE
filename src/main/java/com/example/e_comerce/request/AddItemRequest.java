package com.example.e_comerce.request;

import jakarta.validation.constraints.*; 

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Size is required")
    private String size;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Min(value = 0, message = "Price cannot be negative") // Assuming price can be 0 for free items or if calculated server-side
    private Integer price; // Changed to Integer if it can be null, but validation still applies if present.
                           // If it's always expected, it should be 'int' and not nullable in DB.
                           // For now, keeping as Integer to match original.
}