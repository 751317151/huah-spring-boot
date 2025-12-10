package com.huah.huahspringbootspringai.aiAlibaba.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Msg {
    MessageType messageType;
    String text;
    Map<String, Object> metadata;

    public Msg(Message message) {
        this.messageType = message.getMessageType();
        this.text = message.getText();
        this.metadata = message.getMetadata();
    }

    public Message toMessage() {
        return switch (messageType) {
            case SYSTEM -> new SystemMessage(text);
            case USER -> new UserMessage(text);
            case ASSISTANT -> new AssistantMessage(text);
            default -> throw new IllegalArgumentException("Unsupported message type: " + messageType);
        };
    }
}
