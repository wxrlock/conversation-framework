package io.github.wxrlock.conversationframework.model;

import io.github.wxrlock.conversationframework.model.steps.ConversationStep;

import java.util.List;

public interface Conversation {

    String getId();

    List<ConversationStep> getSteps();

    default void addStep(ConversationStep step) {
        getSteps().add(step);
    }

}