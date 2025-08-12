package tylerbank.web.app.TylerBank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tylerbank.web.app.TylerBank.entity.Transaction;
import tylerbank.web.app.TylerBank.entity.User;
import tylerbank.web.app.TylerBank.service.TransactionService;

import java.util.List;

/**
 * Control layer that exposes endpoints for transaction management.
 * @since v3.1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Endpoint to get all transactions for the authenticated user.
     * @param page
     * @param auth
     * @return
     * @since v3.1
     */
    @GetMapping("/list")
    public List<Transaction> getAllTransactions(@RequestParam String page, Authentication auth) {
        return transactionService.getAllTransactions(page, (User) auth.getPrincipal());
    }

    /**
     * Endpoint to get transactions for a specific card.
     * @param cardId
     * @param page
     * @param auth
     * @return
     * @since v3.1
     */
    @GetMapping("/card/{cardId}")
    public List<Transaction> getTransactionByCardId(@PathVariable String cardId, @RequestParam String page, Authentication auth) {
        return transactionService.getTransactionByCardId(cardId, page, (User) auth.getPrincipal());
    }

    /**
     * Endpoint to get transactions for a specific account.
     * @param accountId
     * @param page
     * @param auth
     * @return
     * @since v3.1
     */
    @GetMapping("/account/{accountId}")
    public List<Transaction> getTransactionByAccountId(@PathVariable String accountId, @RequestParam String page, Authentication auth) {
        return transactionService.getTransactionByAccountId(accountId, page, (User) auth.getPrincipal());
    }
}
