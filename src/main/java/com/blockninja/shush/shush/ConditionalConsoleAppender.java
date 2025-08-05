package com.blockninja.shush.shush;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;

@Plugin(name = "ConditionalConsole", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class ConditionalConsoleAppender extends AbstractAppender {
    private final Map<String, Appender> originalAppenders;

    public ConditionalConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                                      Map<String, Appender> originalAppenders) {
        super(name, filter, layout);
        this.originalAppenders = originalAppenders;
    }

    @Override
    public void append(LogEvent event) {
        if (shouldSuppress(event)) return;

        for (Appender appender : originalAppenders.values()) {
            appender.append(event);
        }
    }

    private boolean shouldSuppress(LogEvent event) {
        String fullLog = event.getMessage().getFormattedMessage();
        if (this.getLayout() != null) {
            fullLog = this.getLayout().toSerializable(event).toString();
        }
        return Shush.shouldSuppress(fullLog);
    }
}
