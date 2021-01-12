package com.github.xiavic.essentials.Utils.messages;

import org.jetbrains.annotations.NotNull;

public class LinkMessages extends AbstractMessageProvider {

    public static final LinkMessages INSTANCE = new LinkMessages();
    @NotNull
    public Message messageDiscord = createMessage("discord",
            "&bCome &3join &bour &3discord &bat &3https://discord.com/invite/nY5yFhs");
    @NotNull
    public Message messageForums =
            createMessage("forums", "&aVisit our forums at: &bhttps://forums.xiav.icu");
    @NotNull
    public Message messageTwitter =
            createMessage("twitter", "Check out our twitter at: https://twitter.com/XiavicNetwork");
    @NotNull
    public Message messageWebsite =
            createMessage("website", "&eOur Website&c: &bhttps://xiav.icu");
    @NotNull
    public Message messageYoutube = createMessage("youtube",
            "Our amazing Youtube Channel: https://www.youtube.com/channel/UCzl5jhb9JRDnwjIylvo92ww");

    private LinkMessages() {

    }

    @Override
    public @NotNull Message createMessage(final @NotNull String key,
                                          final @NotNull String defaultValue) {
        return super.createMessage("links." + key, defaultValue);
    }
}
