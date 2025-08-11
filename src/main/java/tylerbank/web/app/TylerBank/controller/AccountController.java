package tylerbank.web.app.TylerBank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tylerbank.web.app.TylerBank.dto.AccountDto;
import tylerbank.web.app.TylerBank.dto.TransferDto;
import tylerbank.web.app.TylerBank.entity.Account;
import tylerbank.web.app.TylerBank.entity.Transaction;
import tylerbank.web.app.TylerBank.entity.User;
import tylerbank.web.app.TylerBank.service.AccountService;

import java.util.List;

/**
 * Control layer that exposes endpoints for Account management.
 * @since v2.1
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Endpoint to create a new account.
     * @param accountDto
     * @param authentication
     * @throws Exception
     * @since v2.1
     */
    @PostMapping ("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.createAccount(accountDto, user));
    }

    /**
     * Endpoint to retrieve all accounts associated with a user.
     * @param authentication
     * @since v2.1
     */
    @GetMapping ("/list")
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
    }

    /**
     * Endpoint to transfer funds between accounts.
     * @param transferDto
     * @param authentication
     * @return
     * @throws Exception
     * @since v2.2
     */
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(transferDto, user));
    }
}
