package com.github.xiavic.essentials.Utils.messages;

import de.leonhard.storage.Config;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents a base utility class for configurable messages.
 */
public abstract class AbstractMessageProvider implements MessageProviderInstance {

    private final Map<String, String> configuredMessages = new HashMap<>();
    private final Collection<Message> messages = new LinkedList<>();

    public AbstractMessageProvider() {
    }

    @Override @NotNull public Map<String, String> getConfiguredMessages() {
        return configuredMessages;
    }

    @Override @NotNull public Map<Message, String> getMessages() {
        final Map<Message, String> map = new HashMap<>(messages.size());
        for (final Message message : messages) {
            map.put(message, message.toString());
        }
        return map;
    }

    @Override @NotNull
    public Message createMessage(@NotNull final String key, @NotNull final String defaultValue) {
        configuredMessages.put(key, defaultValue);
        final Message message =
            new Message(Objects.requireNonNull(key), Objects.requireNonNull(defaultValue));
        messages.add(message);
        return message;
    }

    @Override @NotNull public String getConfigured(@NotNull final Message message) {
        return configuredMessages.getOrDefault(message.getKey(), message.getDefaultValue());
    }

    public void load(Config configuration) {
        for (String key : configuration.singleLayerKeySet()) {
            final String defaultValue = configuration.getString(key);
            if (defaultValue != null) {
                messages.add(new Message(key, defaultValue));
            }
        }
    }
}
