package com.example.service;

import com.example.repository.*;
import com.example.entity.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public Message createMessage(Message message) {
        // Check if the message text is not blank and doesn't exceed 255 characters
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }

        // Set the time when the message is posted
        message.setTime_posted_epoch(System.currentTimeMillis());

        // Save the message to the database
        return messageRepository.save(message);
    }

    @Transactional
    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    @Transactional
    public boolean deleteMessage(Integer messageId) {
        Optional<Message> existingMessage = messageRepository.findById(messageId);
    
        if (existingMessage.isPresent()) {
            messageRepository.deleteById(messageId);
            return true; // Message deletion was successful
        } else {
            return false; // Message with the given message_id does not exist
        }
    }
    

    @Transactional
    public Message updateMessageText(Integer messageId, String newMessageText) {
        // Check if the new message text is not blank and doesn't exceed 255 characters
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }

        Message existingMessage = getMessageById(messageId);
        if (existingMessage != null) {
            existingMessage.setMessage_text(newMessageText);
            return messageRepository.save(existingMessage);
        }

        return null;
    }
    @Transactional
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }


}
