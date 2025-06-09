package com.example.e_comerce.request;

import com.example.e_comerce.model.Size;
import jakarta.validation.constraints.Min;        // Import this
import jakarta.validation.constraints.NotBlank;    // Import this
import jakarta.validation.constraints.NotNull;   // Import this

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "Title is required") // Added validation
    private String title;

    @NotBlank(message = "Description is required") // Added validation
    private String description;

    @Min(value = 1, message = "Price must be at least 1") // Added validation
    private int price;

    @Min(value = 0, message = "Discounted price cannot be negative") // Added validation
    private int dicountedPrice; // Keeping original spelling

    @Min(value = 0, message = "Discount percent cannot be negative") // Added validation
    private int dicountPercent; // Keeping original spelling

    @Min(value = 1, message = "Quantity must be at least 1") // Added validation
    private int quantity;

    @NotBlank(message = "Brand is required") // Added validation
    private String brand;

    private String color;

    @NotNull(message = "Size information is required") // Added validation
    private Set<Size> size = new HashSet<>();

    @NotBlank(message = "Image URL is required") // Added validation
    private String imageUrl;

    @NotBlank(message = "Top level category is required") // Added validation
    private String topLevelCategory;

    @NotBlank(message = "Second level category is required") // Added validation
    private String secondLevelCategory;

    @NotBlank(message = "Third level category is required") // Added validation
    private String thirdLevelCategory;

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    public int getDicountedPrice() {
//        return dicountedPrice;
//    }
//
//    public void setDicountedPrice(int dicountedPrice) {
//        this.dicountedPrice = dicountedPrice;
//    }
//
//    public int getDicountPercent() {
//        return dicountPercent;
//    }
//
//    public void setDicountPercent(int dicountPercent) {
//        this.dicountPercent = dicountPercent;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public Set<Size> getSize() {
//        return size;
//    }
//
//    public void setSize(Set<Size> size) {
//        this.size = size;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public String getTopLavelCategory() { 
//        return topLevelCategory;
//    }
//
//    public void setTopLevelCategory(String topLavelCategory) { 
//        this.topLevelCategory = topLevelCategory;
//    }
//
//    public String getSecondLavelCategory() { 
//        return secondLevelCategory;
//    }
//
//    public void setSecondLevelCategory(String secondLavelCategory) { 
//        this.secondLevelCategory = secondLevelCategory;
//    }
//
//    public String getThirdLavelCategory() { 
//        return thirdLevelCategory;
//    }
//
//    public void setThirdLevelCategory(String thirdLavelCategory) { 
//        this.thirdLevelCategory = thirdLevelCategory;
//    }
}