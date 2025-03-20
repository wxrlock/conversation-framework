package io.github.wxrlock.conversationframework.model.steps;

import io.github.wxrlock.conversationframework.manager.ConversationManager;
import io.github.wxrlock.conversationframework.enums.NumberType;
import io.github.wxrlock.conversationframework.enums.ConversationMessages;
import io.github.wxrlock.conversationframework.utils.NumberFormatter;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import java.util.Map;

import static net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND;
import static net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT;

@Getter
public class StandardStep implements ConversationStep {

    private final String[] prompt;
    private final boolean spacing;
    private final boolean showTip;
    private final boolean showCancellationTip;
    private final long timeoutMillis;
    private final Map<String, String> options;
    private final boolean multipleArguments;
    private final NumberType numberType;
    private final boolean allowNegative;
    private final Double minValue;
    private final Double maxValue;
    private final boolean repeatOnFailure;
    private final ResponseHandler responseHandler;

    @Setter
    private boolean endConversation;

    public StandardStep(StepBuilder builder) {
        this.prompt = builder.getPrompt();
        this.spacing = builder.isSpacing();
        this.showTip = builder.isShowTip();
        this.showCancellationTip = builder.isShowCancellationTip();
        this.timeoutMillis = builder.getTimeoutMillis();
        this.options = builder.getOptions();
        this.multipleArguments = builder.isMultipleArguments();
        this.numberType = builder.getNumberType();
        this.allowNegative = builder.isAllowNegative();
        this.minValue = builder.getMinValue();
        this.maxValue = builder.getMaxValue();
        this.repeatOnFailure = builder.isRepeatOnFailure();
        this.responseHandler = builder.getResponseHandler();
        this.endConversation = false;
    }

    @Override
    public void ask(Player player, ConversationManager manager) {
        if (spacing) player.sendMessage("");

        player.sendMessage(prompt);

        if (options.isEmpty()) {
            if (numberType != NumberType.NONE && showTip) {
                player.sendMessage(getNumberHint(manager.getSpacing()));
            }

            if (showCancellationTip) {
                player.sendMessage(String.format(
                        "%s%s",
                        StringUtils.repeat(" ", manager.getSpacing()),
                        ConversationMessages.CANCELLATION_TIP.getMessage()
                ));
            }
        } else {
            player.spigot().sendMessage(buildClickableOptions(
                    manager.getCurrentStepId(player),
                    manager.getSpacing()
            ));
        }

        if (spacing) player.sendMessage("");

        manager.updateTimeout(player, timeoutMillis);
    }

    @Override
    public void handleResponse(Player player, String input, ConversationManager manager) {
        final String processedInput = multipleArguments ? input : input.split(" ")[0];

        if (numberType != NumberType.NONE &&
                !validateNumber(player, processedInput, manager)) {
            return;
        }

        if (responseHandler != null) {
            responseHandler.handle(new StepResponse(
                    player,
                    processedInput,
                    manager,
                    this
            ));
        }
    }

    @Override
    public boolean shouldEndConversation() {
        return endConversation;
    }

    private TextComponent[] buildClickableOptions(int stepId, int spacing) {
        final TextComponent[] components = new TextComponent[options.size() + 1];

        components[0] = new TextComponent(StringUtils.repeat(" ", spacing));

        int index = 1;
        for (Map.Entry<String, String> entry : options.entrySet()) {
            final String option = entry.getKey();
            final String color = entry.getValue();

            final TextComponent component = new TextComponent(color + "[" + option + "] ");
            component.setClickEvent(new ClickEvent(RUN_COMMAND, String.format("/talk %d %s", stepId, option.toLowerCase())));
            component.setHoverEvent(new HoverEvent(SHOW_TEXT, new TextComponent[]{new TextComponent(String.format("%sClique para escolher %s.", color, option))}));

            components[index++] = component;
        }
        return components;
    }

    private boolean validateNumber(Player player, String input, ConversationManager manager) {
        try {
            double value;
            switch (numberType) {
                case INTEGER:
                    value = Integer.parseInt(input);
                    break;
                case FLOAT:
                    value = Float.parseFloat(input);
                    break;
                case DOUBLE:
                    value = Double.parseDouble(input);
                    break;
                default:
                    return true;
            }

            if (!allowNegative && value < 0) {
                player.sendMessage(ConversationMessages.NEGATIVE_NOT_ALLOWED.getMessage());

                handleFailure(player, manager);
                return false;
            }
            if (minValue != null && value < minValue) {
                player.sendMessage(ConversationMessages.BELOW_MINIMUM.getMessage(minValue));

                handleFailure(player, manager);
                return false;
            }
            if (maxValue != null && value > maxValue) {
                player.sendMessage(ConversationMessages.ABOVE_MAXIMUM.getMessage(maxValue));

                handleFailure(player, manager);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(ConversationMessages.INVALID_NUMBER.getMessage(numberType.getDescription()));

            handleFailure(player, manager);
            return false;
        }
    }

    private void handleFailure(Player player, ConversationManager manager) {
        if (repeatOnFailure) {
            manager.updateTimeout(player, timeoutMillis);
            manager.setCurrentStep(player, manager.getCurrentStep(player) - 1);
        } else {
            setEndConversation(true);
        }
    }

    private String getNumberHint(int spacing) {
        final StringBuilder hint = new StringBuilder(String.format(
                "%s§7(Digite um %s",
                StringUtils.repeat(" ", spacing),
                numberType.getDescription()
        ));

        if (!allowNegative) hint.append(", não negativo");
        if (minValue != null) hint.append(", mínimo ").append(NumberFormatter.applySuffix(minValue));
        if (maxValue != null) hint.append(", máximo ").append(NumberFormatter.applySuffix(maxValue));

        hint.append(")");
        return hint.toString();
    }

}