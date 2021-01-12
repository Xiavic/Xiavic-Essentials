package com.github.xiavic.essentials.Utils.messages;

// Hyperverse - A Minecraft world management plugin
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.
//


import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Configurable messages
 */
public final class Message implements MessageKeyProvider {

    private final MessageKey messageKey;
    private final String key;
    private final String defaultValue;

    /**
     * Construct a new message
     *
     * @param key          The message key, used as a path in the configuration file
     * @param defaultValue The default message
     */
    public Message(@NotNull final String key, @NotNull final String defaultValue) {
        this.key = Objects.requireNonNull(key);
        this.defaultValue = Objects.requireNonNull(defaultValue);
        this.messageKey = MessageKey.of(key);
    }

    /**
     * Get the configuration key
     *
     * @return Configuration key
     */
    @NotNull
    public String getKey() {
        return this.key;
    }

    /**
     * Get the default message
     *
     * @return Default message
     */
    @NotNull
    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public String toString() {
        return defaultValue;
    }

    public String withoutColorCodes() {
        return this.toString().replaceAll("&[A-Za-z0-9]", "");
    }

    @Override
    public MessageKey getMessageKey() {
        return this.messageKey;
    }

}

