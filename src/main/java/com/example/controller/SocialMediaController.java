package com.example.controller;
import com.example.entity.Message;
import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * TO DO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<String> registerAccount(@RequestBody Account account) {
        Account registeredAccount = accountService.registerAccount(account);
        if (registeredAccount != null) {
            // Registration successful, return the registered account with a 200 status code
            return ResponseEntity.ok("Registration successful");
        } else {
            // Handle registration failure for duplicate username
            // Return a 409 Conflict status code with a custom error message
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
        }
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Account> loginAccount(@RequestBody Account account) {
        Account authenticatedAccount = accountService.authenticateAccount(account);
        if (authenticatedAccount != null) {
            // Authentication successful, return the authenticated account with a 200 status code
            return ResponseEntity.ok(authenticatedAccount);
        } else {
            // Authentication failed, return a 401 Unauthorized status code
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    } 
    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Account postedByAccount = accountService.getAccountById(message.getPosted_by());
        
        if (postedByAccount == null) {
            // If the postedBy user doesn't exist, return a 404 Not Found status
            return ResponseEntity.badRequest().build();
        }
        
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            // If the message text is blank, return a 400 Bad Request status
            return ResponseEntity.badRequest().build();
        }
        
        // Create and save the message
        message.setTime_posted_epoch(System.currentTimeMillis()); 
        Message createdMessage = messageService.createMessage(message);
        
        
        if (createdMessage != null) {
            // Message creation successful, return the created message with a 200 status code
            return ResponseEntity.ok(createdMessage);
        } else {
            // Handle message creation failure, e.g., return null or a custom error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        return ResponseEntity.ok(messages);
    }
    @GetMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        Message message = messageService.getMessageById(message_id);
    
        if (message != null) {
           return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.ok().build();
        }
    }
    @DeleteMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessage(@PathVariable Integer message_id) {
        // Attempt to delete the message with the given message_id
        boolean messageDeleted = messageService.deleteMessage(message_id);

        if (messageDeleted) {
            // Message deletion was successful, return 1 with a 200 status code
            return ResponseEntity.ok(1);
        } else {
            // Message with the given message_id does not exist, return 200 with an empty response body
            return ResponseEntity.ok().body(null);
        }
    }
    @PatchMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Object> updateMessageText(@PathVariable Integer message_id, @RequestBody Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            // If the new message_text is blank or too long, return 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
        }
    
        // Call the service to update the message text
        Message updatedMessage = messageService.updateMessageText(message_id, message.getMessage_text());
    
        if (updatedMessage != null) {
            // Update successful, return 200 status code and the updated message
            return ResponseEntity.ok(updatedMessage);
        } else {
            // Update failed, return 400 status code for Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
        }
    }
        


}
