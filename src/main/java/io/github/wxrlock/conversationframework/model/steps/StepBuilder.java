package io.github.wxrlock.conversationframework.model.steps;

import io.github.wxrlock.conversationframework.enums.NumberType;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
public class StepBuilder {

    private String[] prompt = new String[]{""};
    private boolean spacing = true;
    private boolean showTip = false;
    private boolean showCancellationTip = true;
    private long timeoutMillis = TimeUnit.SECONDS.toMillis(20);
    private boolean multipleArguments = false;
    private NumberType numberType = NumberType.NONE;
    private boolean allowNegative = true;
    private Double minValue = null;
    private Double maxValue = null;
    private boolean repeatOnFailure = true;
    private ResponseHandler responseHandler;
    private final Map<String, String> options = new LinkedHashMap<>();

    public StepBuilder prompt(String... prompt) {
        this.prompt = prompt.length > 0 ? prompt : new String[]{""};
        return this;
    }

    public StepBuilder spacing(boolean spacing) {
        this.spacing = spacing;
        return this;
    }

    public StepBuilder showTip(boolean show) {
        this.showTip = show;
        return this;
    }

    public StepBuilder showCancellationTip(boolean show) {
        this.showCancellationTip = show;
        return this;
    }

    public StepBuilder timeout(int value, TimeUnit unit) {
        this.timeoutMillis = unit.toMillis(value);
        return this;
    }

    public StepBuilder option(String option, String color) {
        this.options.put(option, color);
        return this;
    }

    public StepBuilder options(Map<String, String> options) {
        this.options.putAll(options);
        return this;
    }

    public StepBuilder multipleArguments(boolean multipleArguments) {
        this.multipleArguments = multipleArguments;
        return this;
    }

    public StepBuilder requireNumber(NumberType type) {
        this.numberType = type;
        return this;
    }

    public StepBuilder allowNegative(boolean allow) {
        this.allowNegative = allow;
        return this;
    }

    public StepBuilder minValue(double min) {
        this.minValue = min;
        return this;
    }

    public StepBuilder maxValue(double max) {
        this.maxValue = max;
        return this;
    }

    public StepBuilder repeatOnFailure(boolean repeat) {
        this.repeatOnFailure = repeat;
        return this;
    }

    public StepBuilder onResponse(ResponseHandler handler) {
        this.responseHandler = handler;
        return this;
    }

    public StandardStep build() {
        return new StandardStep(this);
    }

}