package io.github.wxrlock.conversationframework.model.steps;

import io.github.wxrlock.conversationframework.manager.ConversationManager;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class StepResponse {

    private final Player player;
    private final String response;
    private final ConversationManager manager;
    private final StandardStep step;

    public StepResponse(Player player, String response, ConversationManager manager, StandardStep step) {
        this.player = player;
        this.response = response;
        this.manager = manager;
        this.step = step;
    }

    public void endConversation() {
        step.setEndConversation(true);
    }

}