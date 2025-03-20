# 📖 Conversation Examples

This guide demonstrates how to create and register conversations using `ConversationFramework`.

## ✨ Example
```java
@Getter // Lombok
public class PersonalConversation implements Conversation {
    private final String id;
    private final List<ConversationStep> steps = Lists.newLinkedList();

    public PersonalConversation(String id) {
        this.id = id;

        addStep(new StepBuilder()
            .prompt("§eFavorite food?") // Initial prompt
            .onResponse(response -> response.getPlayer().sendMessage("§aYum: " + response.getResponse()))
            .build());
        
        addStep(new StepBuilder()
            .prompt("§eFavorite animal?")
            // Create infinites clickable options
            .option("Dog", "§6")
            .option("Cat", "§7")
            .onResponse(response -> response.getPlayer().sendMessage("§aYou chose: " + response.getResponse()))
            .build());
    }
}
```

##  🛠️ Registering and Starting Conversations
```java
ConversationFramework conversationFramework = new ConversationFramework(plugin, spacing);

// Registering conversations
conversationFramework.register(new PersonalConversation("personal_conversation"));

// Setting timeout message
ConversationMessages.TIMEOUT.setMessage("§cResponse time expired!");

// Starting a conversation for a player
conversationFramework.start(player, "personal_conversation");
```
