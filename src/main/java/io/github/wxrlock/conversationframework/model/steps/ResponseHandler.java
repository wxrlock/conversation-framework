package io.github.wxrlock.conversationframework.model.steps;

@FunctionalInterface
public interface ResponseHandler {

    void handle(StepResponse response);

}