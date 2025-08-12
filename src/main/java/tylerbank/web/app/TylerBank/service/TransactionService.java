package tylerbank.web.app.TylerBank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tylerbank.web.app.TylerBank.entity.*;
import tylerbank.web.app.TylerBank.repository.TransactionRepository;

import java.util.List;

/**
 * Service class for managing transactions.
 * @since v3.1
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Retrieves all transactions for the given user, sorted by creation date in ascending order.
     * @param page
     * @param user
     * @return
     * @since v3.1
     */
    public List<Transaction> getAllTransactions(String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByOwnerUid(user.getUid(), pageable).getContent();
    }

    /**
     * Retrieves all transactions for the given user, filtered by card ID, sorted by creation date in ascending order.
     * @param cardId
     * @param page
     * @param user
     * @return
     * @since v3.1
     */
    public List<Transaction> getTransactionByCardId(String cardId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByCardCardIdAndOwnerUid(cardId, user.getUid(), pageable).getContent();
    }

    /**
     * Retrieves all transactions for the given user, filtered by account ID, sorted by creation date in ascending
     * @param accountId
     * @param page
     * @param user
     * @return
     * @since v3.1
     */
    public List<Transaction> getTransactionByAccountId(String accountId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByAccountAccountIdAndOwnerUid(accountId, user.getUid(), pageable).getContent();
    }

    /**
     * Creates a new account transaction with the given parameters
     * @param amount
     * @param type
     * @param txFee
     * @param account
     * @param owner
     * @return
     * @since v2.4
     */
    public Transaction createAccountTransaction(double amount, Type type, double txFee, Account account, User owner) {
        var transaction = Transaction.builder()
                .txFee(txFee)
                .amount(amount)
                .type(type)
                .status(Status.COMPLETED)
                .owner(owner)
                .build();
        return transactionRepository.save(transaction);
    }

    /**
     * Creates a new card transaction with the given parameters
     * @param amount
     * @param type
     * @param txFee
     * @param card
     * @param owner
     * @return
     * @since v2.4
     */
    public Transaction createCardTransaction(double amount, Type type, double txFee, Card card, User owner) {
        var transaction = Transaction.builder()
                .txFee(txFee)
                .amount(amount)
                .type(type)
                .status(Status.COMPLETED)
                .owner(owner)
                .card(card)
                .build();
        return transactionRepository.save(transaction);
    }
}
