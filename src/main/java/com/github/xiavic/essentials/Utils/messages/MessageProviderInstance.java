package com.github.xiavic.essentials.Utils.messages;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface MessageProviderInstance {
    @NotNull Map<String, String> getConfiguredMessages();

    @NotNull Map<Message, String> getMessages();

    @NotNull Message createMessage(@NotNull String key, @NotNull String defaultValue);

    @NotNull String getConfigured(@NotNull Message message);
}
