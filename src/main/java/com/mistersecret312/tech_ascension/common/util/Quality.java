package com.mistersecret312.tech_ascension.common.util;

import net.minecraft.ChatFormatting;

public enum Quality
{
    COMMON(ChatFormatting.WHITE),
    UNCOMMON(ChatFormatting.GREEN),
    RARE(ChatFormatting.BLUE),
    EPIC(ChatFormatting.DARK_PURPLE),
    LEGENDARY(ChatFormatting.GOLD),
    ICONIC(ChatFormatting.GOLD);

    public final ChatFormatting format;
    Quality(ChatFormatting format)
    {
        this.format = format;
    }
}
