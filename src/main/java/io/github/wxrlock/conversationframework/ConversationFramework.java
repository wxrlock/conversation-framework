package io.github.wxrlock.conversationframework;

import io.github.wxrlock.conversationframework.listener.ConversationListeners;
import io.github.wxrlock.conversationframework.manager.ConversationManager;
import io.github.wxrlock.conversationframework.model.Conversation;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

@Getter
public class ConversationFramework {

    private final ConversationManager manager;

    public ConversationFramework(JavaPlugin plugin, int spacing) {
        this.manager = new ConversationManager(plugin, spacing);

        new ConversationListeners(plugin, manager);
    }

    public void register(Conversation... conversations) {
        for (Conversation conversation : conversations) {
            manager.registerQuestionnaire(conversation.getId(), conversation.getSteps());
        }
    }

    public void start(Player player, String conversationId) {
        manager.startConversation(player, conversationId, TimeUnit.SECONDS.toMillis(20));
    }

    public void start(Player player, String conversationId, long value, TimeUnit unit) {
        manager.startConversation(player, conversationId, unit.toMillis(value));
    }

    public void end(Player player) {
        manager.endConversation(player);
    }

}