package io.github.wxrlock.conversationframework.listener;

import io.github.wxrlock.conversationframework.manager.ConversationManager;
import io.github.wxrlock.conversationframework.enums.ConversationMessages;
import io.github.wxrlock.conversationframework.model.ActiveConversation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class ConversationListeners implements Listener {

    private final ConversationManager manager;

    public ConversationListeners(Plugin plugin, ConversationManager manager) {
        this.manager = manager;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        final Player player = event.getPlayer();

        final ActiveConversation conversation = manager.getConversation(player);
        if (conversation == null) return;

        event.setCancelled(true);

        final String input = event.getMessage();

        if (input.startsWith("cancel")) {
            player.sendMessage(ConversationMessages.CONVERSATION_CANCELLED.getMessage());

            manager.endConversation(player);
            return;
        }

        conversation.handleInput(player, input);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (manager.isInConversation(player)) {
            manager.endConversation(player);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();
        if (manager.isInConversation(player)) {
            manager.endConversation(player);

            player.sendMessage(ConversationMessages.CONVERSATION_ENDED.getMessage());
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final String message = event.getMessage();

        final Player player = event.getPlayer();

        if (!message.toLowerCase().startsWith("/talk")) {
            if (manager.isInConversation(player)) {
                event.setCancelled(true);

                manager.endConversation(player);

                player.sendMessage(ConversationMessages.CONVERSATION_ENDED.getMessage());
                return;
            }
            return;
        }

        event.setCancelled(true);

        if (!manager.isInConversation(player)) {
            player.sendMessage(ConversationMessages.NOT_IN_CONVERSATION.getMessage());
            return;
        }

        final String[] parts = message.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            player.sendMessage(ConversationMessages.INVALID_OPTION.getMessage());
            return;
        }

        final String[] args = parts[1].split(" ", 2);
        if (args.length < 2) {
            player.sendMessage(ConversationMessages.INVALID_OPTION.getMessage());
            return;
        }

        int stepId;
        try {
            stepId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ConversationMessages.INVALID_OPTION.getMessage());
            return;
        }

        if (manager.getCurrentStepId(player) != stepId) {
            player.sendMessage(ConversationMessages.INVALID_OPTION.getMessage());
            return;
        }

        final String input = args[1];

        final ActiveConversation conversation = manager.getConversation(player);
        if (conversation != null) conversation.handleInput(player, input);
    }

}