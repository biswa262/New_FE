package com.example.e_comerce.response;

import com.example.e_comerce.model.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private int price;
    private int discountedPrice;
    private int discountedPercent;
    private int quantity;
    private String brand;
    private String color;
    private Set<Size> sizes;
    private String imageUrl;
    private LocalDateTime createdAt;

    // For category info, you can nest DTO or just return the names of categories
    private String topLevelCategory;
    private String secondLevelCategory;
    private String thirdLevelCategory;
}
