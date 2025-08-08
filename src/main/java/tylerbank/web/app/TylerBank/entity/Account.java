package tylerbank.web.app.TylerBank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Account entity class representing a bank account
 * @since v1.0
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;

    @Column(nullable = false, unique = true)
    private long accountNumber;
    private double balance;
    private String accountName;
    private String currency;
    private String code;
    private String label;
    private char symbol;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relationship with Transaction entity
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    //Relation with User Entity
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}