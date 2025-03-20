package io.github.wxrlock.conversationframework.model;

import com.google.common.collect.Lists;
import io.github.wxrlock.conversationframework.manager.ConversationManager;
import io.github.wxrlock.conversationframework.model.steps.ConversationStep;
import io.github.wxrlock.conversationframework.model.steps.StandardStep;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class ActiveConversation {

    @Setter
    private int currentStep = 0;

    private final ConversationManager manager;
    private final List<ConversationStep> steps;

    public ActiveConversation(ConversationManager manager, List<ConversationStep> list) {
        this.manager = manager;
        this.steps = Lists.newLinkedList();

        this.steps.addAll(list);
    }

    public void start(Player player) {
        if (!steps.isEmpty()) {
            resetStepStates();

            manager.setStepId(player, 0);

            steps.get(0).ask(player, manager);
        }
    }

    public void handleInput(Player player, String input) {
        if (currentStep >= steps.size()) {
            manager.endConversation(player);
            return;
        }

        final ConversationStep _currentStep = steps.get(currentStep);

        _currentStep.handleResponse(player, input, manager);

        if (_currentStep.shouldEndConversation()) {
            manager.endConversation(player);
            return;
        }

        currentStep++;

        if (currentStep < steps.size()) {
            manager.setStepId(player, currentStep);
            steps.get(currentStep).ask(player, manager);
        } else {
            manager.endConversation(player);
        }
    }

    private void resetStepStates() {
        for (ConversationStep step : steps) {
            if (step instanceof StandardStep) {
                ((StandardStep) step).setEndConversation(false);
            }
        }
    }

}