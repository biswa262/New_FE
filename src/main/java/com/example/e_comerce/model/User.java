package com.example.e_comerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String first_name;
    private String last_name;
    private String password;
    private String email;
    private String mobile;

    // --- REMOVED: role field and its annotations ---
    // Previously:
    // @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ROLE_USER'")
    // private String role;
    // --- END REMOVAL ---

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
        // --- REMOVED: Role default setting in PrePersist ---
        // Previously:
        // if (role == null) {
        //     role = "ROLE_USER";
        // }
        // --- END REMOVAL ---
    }

    // --- REMOVED: Getter and Setter for role field if they were explicitly defined ---
    // (Assuming Lombok's @Getter/@Setter will now correctly exclude 'role' as it's no longer a field)
    // If you had:
    // public String getRole() { return role; }
    // public void setRole(String role) { this.role = role; }
    // Those methods should be removed.
}