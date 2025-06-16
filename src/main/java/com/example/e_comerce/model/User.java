package com.example.e_comerce.model;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank; // Added for validation

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "First name is required")
 private String first_name;

 @NotBlank(message = "Last name is required")
 private String last_name;

@NotBlank(message = "Password is required")
 @Size(min = 6, message = "Password must be at least 6 characters")
 private String password;


	@NotBlank(message = "Email is required")
 @Email(message = "Invalid email format")
 private String email;

	@NotBlank(message = "Mobile number is required")
 @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
 private String mobile;




    // Modified: Mark role as non-blank for validation in DTO context
    // Removed @Column(columnDefinition="...") so the database doesn't set a default.
    // The role *must* be provided by the client or handled by the controller logic.
    @NotBlank(message = "Role is required") // Added validation
    @Column(nullable = false) // Ensures database constraint
    private String role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Address> address=new ArrayList<>();

    @Embedded
    @ElementCollection
    @CollectionTable(name = "payment_information",joinColumns = @JoinColumn(name = "user_id"))
    private List<PaymentInformation> paymentInformation=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Rating> ratings=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews=new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist // This annotation ensures the method is called before persisting the entity
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        // Removed the role defaulting logic from here.
        // Role is now expected to be provided and validated before persisting.
    }
}