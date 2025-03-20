package io.github.wxrlock.conversationframework.task;

import io.github.wxrlock.conversationframework.manager.ConversationManager;
import io.github.wxrlock.conversationframework.enums.ConversationMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map.Entry;

public class TimeoutTask extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final ConversationManager manager;

    public TimeoutTask(JavaPlugin plugin, ConversationManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        this.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        final long currentTime = System.currentTimeMillis();

        final Iterator<Entry<String, Long>> iterator = manager.getConversationTimeouts().entrySet().iterator();
        while (iterator.hasNext()) {
            final Entry<String, Long> entry = iterator.next();

            final String playerName = entry.getKey();

            if (currentTime >= entry.getValue()) {
                final Player player = Bukkit.getPlayer(playerName);

                if (player != null) {
                    player.sendMessage(ConversationMessages.TIMEOUT.getMessage());

                    manager.endConversation(player);
                } else {
                    iterator.remove();

                    manager.getActiveConversations().remove(playerName);
                    manager.getCurrentStepIds().remove(playerName);
                }
            }
        }
    }

}
