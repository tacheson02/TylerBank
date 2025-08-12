package tylerbank.web.app.TylerBank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tylerbank.web.app.TylerBank.dto.CardDto;
import tylerbank.web.app.TylerBank.entity.*;
import tylerbank.web.app.TylerBank.repository.AccountRepository;
import tylerbank.web.app.TylerBank.repository.CardRepository;
import tylerbank.web.app.TylerBank.repository.TransactionRepository;
import tylerbank.web.app.TylerBank.service.helper.AccountHelper;
import tylerbank.web.app.TylerBank.util.RandomUtil;

import java.time.LocalDateTime;

/**
 * Service class for managing card operations.
 * @since v2.4
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final AccountHelper accountHelper;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Get the card for the given user.
     * @param user
     * @return
     * @throws Exception
     * @since v2.4
     */
    public Card getCard(User user) throws Exception {
        return cardRepository.findByOwnerUid(user.getUid())
                .orElseThrow(() -> new Exception("No card found for this user"));
    }

    /**
     * Create a new card for the given user with the specified amount.
     * @param cardDto
     * @param user
     * @return
     * @throws Exception
     * @since v2.4
     */
    public Card createCard(CardDto cardDto, User user) throws Exception {
        double amount = cardDto.getAmount();

        // Finds USDAccount for user and throws exception if not found
        var usdAccount = accountRepository.findByCodeAndOwnerUid("USD", user.getUid())
                .orElseThrow(() -> new Exception("No USD account found for this user"));

        // Check if the user has sufficient funds in their USD account
        accountHelper.validateSufficientFunds(usdAccount, amount);

        // Withdraw the amount from the USD account and save account
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        accountRepository.save(usdAccount);

        // Generate a unique card number.
        long cardNumber;
        do{
            cardNumber = new RandomUtil().generateRandom(16);
        } while (cardRepository.existsByCardNumber(cardNumber));

        //Create card
        Card card = Card.builder()
                .cardNumber(cardNumber)
                .exp(LocalDateTime.now().plusYears(3))
                .cardHolder(user.getFirstname() + " " + user.getLastname())
                .cvv(String.valueOf(new RandomUtil().generateRandom(3)))
                .balance(amount)
                .owner(user)
                .build();

        //Create transaction for account withdraw and card creation
        accountHelper.createAccountTransaction(amount, Type.WITHDRAWAL, 0.0, usdAccount, user);
        accountHelper.createCardTransaction(amount, Type.DEPOSIT, 0.0, card, user);

        //Save and return the card
        return cardRepository.save(card);
    }

    /**
     * Credit a card with the given amount.
     * @param cardDto
     * @param user
     * @return
     * @since v2.4
     */
    public Transaction creditCard(CardDto cardDto, User user) throws Exception {
        double amount = cardDto.getAmount();

        //Find USD account for user and throws exception if not found
        var usdAccount = accountRepository.findByCodeAndOwnerUid("USD", user.getUid())
                .orElseThrow(() -> new Exception("No USD account found for this user"));

        // Check if the user has sufficient funds in their USD account
        accountHelper.validateSufficientFunds(usdAccount, amount);

        // Credit the card
        var card = user.getCard();
        card.setBalance(card.getBalance() + amount);
        cardRepository.save(card);

        // Withdraw the amount from the USD account and save account
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        accountRepository.save(usdAccount);

        //Create transaction for account and card, return card transaction
        accountHelper.createAccountTransaction(amount, Type.WITHDRAWAL, 0.0, usdAccount, user);
        return accountHelper.createCardTransaction(amount, Type.CREDIT, 0.0, card, user);
    }

    /**
     * Debit a card with the given amount.
     * @param cardDto
     * @param user
     * @return
     * @since v2.4
     */
    public Transaction debitCard(CardDto cardDto, User user) throws Exception {
        double amount = cardDto.getAmount();

        //Find USD account for user and throws exception if not found
        var usdAccount = accountRepository.findByCodeAndOwnerUid("USD", user.getUid())
                .orElseThrow(() -> new Exception("No USD account found for this user"));

        // Check if the user has sufficient funds on their card and debit the card
        var card = user.getCard();
        if (card.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance on card.");
        }
        card.setBalance(card.getBalance() - amount);
        cardRepository.save(card);

        // Deposit the amount from the USD account and save account
        usdAccount.setBalance(usdAccount.getBalance() + amount);
        accountRepository.save(usdAccount);

        //Create transaction for account and card, return card transaction
        accountHelper.createAccountTransaction(amount, Type.DEPOSIT, 0.0, usdAccount, user);
        return accountHelper.createCardTransaction(amount, Type.DEBIT, 0.0, card, user);
    }
}
