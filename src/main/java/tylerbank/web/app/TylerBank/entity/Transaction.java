package tylerbank.web.app.TylerBank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Transaction entity represents a single transaction made on a card
 * @since v1.0
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String txId;

    private Double amount;
    private Double txFee;
    private String sender;
    private String receiver;
    private Status status;
    private Type type;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relationship with Card entity
    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    //Relationship with User entity
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    //Relationship with Account entity
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}