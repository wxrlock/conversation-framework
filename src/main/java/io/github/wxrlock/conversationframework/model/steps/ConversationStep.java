package io.github.wxrlock.conversationframework.model.steps;

import io.github.wxrlock.conversationframework.manager.ConversationManager;
import org.bukkit.entity.Player;

public interface ConversationStep {

    void ask(Player player, ConversationManager manager);

    void handleResponse(Player player, String input, ConversationManager manager);

    boolean shouldEndConversation();

}