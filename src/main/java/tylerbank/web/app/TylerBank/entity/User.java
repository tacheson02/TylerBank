package tylerbank.web.app.TylerBank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstname;
    private String lastname;
    private String password;
    private Date dob;
    private long tel;
    private String tag;
    private String gender;
    private List<String> roles;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Relationship with Card entity
    @OneToOne(mappedBy = "owner")
    private Card card;

    //Relationship with Transaction entity
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    //Relationship with Account entity
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;
}