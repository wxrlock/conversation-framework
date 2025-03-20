package io.github.wxrlock.conversationframework.manager;

import com.google.common.collect.Maps;
import io.github.wxrlock.conversationframework.model.ActiveConversation;
import io.github.wxrlock.conversationframework.model.steps.ConversationStep;
import io.github.wxrlock.conversationframework.enums.ConversationMessages;
import io.github.wxrlock.conversationframework.task.TimeoutTask;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

@Getter
public class ConversationManager {

    private final JavaPlugin plugin;
    private final int spacing;

    private final Map<String, Integer> currentStepIds = Maps.newLinkedHashMap();
    private final Map<String, Long> conversationTimeouts = Maps.newConcurrentMap();
    private final Map<String, ActiveConversation> activeConversations = Maps.newConcurrentMap();
    private final Map<String, List<ConversationStep>> registeredQuestionnaires = Maps.newLinkedHashMap();

    public ConversationManager(JavaPlugin plugin, int spacing) {
        this.plugin = plugin;
        this.spacing = spacing;

        new TimeoutTask(plugin, this);
    }

    public void registerQuestionnaire(String id, List<ConversationStep> steps) {
        registeredQuestionnaires.put(id, steps);
    }

    public void startConversation(Player player, String questionnaireId, long timeoutMillis) {
        List<ConversationStep> steps = registeredQuestionnaires.get(questionnaireId);
        if (steps == null) {
            player.sendMessage("§cQuestionário não encontrado: " + questionnaireId);
            return;
        }

        final String playerName = player.getName();
        if (isInConversation(player)) {
            player.sendMessage(ConversationMessages.ALREADY_IN_CONVERSATION.getMessage());
            return;
        }

        conversationTimeouts.put(playerName, System.currentTimeMillis() + timeoutMillis);

        final ActiveConversation conversation = new ActiveConversation(this, steps);
        activeConversations.put(playerName, conversation);

        conversation.start(player);
    }

    public void endConversation(Player player) {
        final String playerName = player.getName();

        activeConversations.remove(playerName);
        conversationTimeouts.remove(playerName);
        currentStepIds.remove(playerName);
    }

    public void updateTimeout(Player player, long timeoutMillis) {
        if (isInConversation(player)) {
            conversationTimeouts.put(player.getName(), System.currentTimeMillis() + timeoutMillis);
        }
    }

    public void setStepId(Player player, int stepId) {
        currentStepIds.put(player.getName(), stepId);
    }

    public boolean isInConversation(Player player) {
        return activeConversations.containsKey(player.getName());
    }

    public int getCurrentStep(Player player) {
        ActiveConversation conv = activeConversations.get(player.getName());
        return conv != null ? conv.getCurrentStep() : -1;
    }

    public void setCurrentStep(Player player, int step) {
        ActiveConversation conv = activeConversations.get(player.getName());
        if (conv != null) conv.setCurrentStep(step);
    }

    public Integer getCurrentStepId(Player player) {
        return currentStepIds.getOrDefault(player.getName(), -1);
    }

    public ActiveConversation getConversation(Player player) {
        return activeConversations.get(player.getName());
    }

}