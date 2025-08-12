package tylerbank.web.app.TylerBank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Card entity represents a card owned by a user and linked to an account.
 * @since v1.0
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;

    @Column(nullable = false, unique = true)
    private long cardNumber;

    private String cardHolder;
    private Double balance;
    private LocalDateTime exp;
    private String cvv;
    private String pin;
    private String billingAddress;

    @CreationTimestamp
    private LocalDateTime iss;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relation with User Entity
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    //Relationship with Transaction Entity
    @JsonIgnore
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}