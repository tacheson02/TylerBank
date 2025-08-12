package tylerbank.web.app.TylerBank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tylerbank.web.app.TylerBank.dto.CardDto;
import tylerbank.web.app.TylerBank.entity.Card;
import tylerbank.web.app.TylerBank.entity.Transaction;
import tylerbank.web.app.TylerBank.entity.User;
import tylerbank.web.app.TylerBank.service.CardService;

/**
 * Control layer that exposes endpoints for car management.
 * @since v2.4
 */
@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * Endpoint for retrieving a card's balance
     * @param authentication
     * @return
     * @throws Exception
     * @since v2.4
     */
    @GetMapping("/balance")
    public ResponseEntity<Card> getCard(Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.getCard(user));
    }

    /**
     * Endpoint for creating a new card with a specified balance
     * @param card
     * @param authentication
     * @return
     * @throws Exception
     * @since v2.4
     */

    @PostMapping("/create")
    public ResponseEntity<Card> createCard(@RequestBody CardDto card, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.createCard(card, user));
    }

    /**
     * Endpoint for crediting money to a card
     * @param card
     * @param authentication
     * @return
     * @throws Exception
     * @since v2.4
     */
    @PostMapping("/credit")
    public ResponseEntity<Transaction> creditCard(@RequestBody CardDto card, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.creditCard(card, user));
    }

    /**
     * Endpoint for debiting money from a card
     * @param card
     * @param authentication
     * @return
     * @since v2.4
     */
    @PostMapping("/debit")
    public ResponseEntity<Transaction> debitCard(@RequestBody CardDto card, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.debitCard(card, user));
    }
}
