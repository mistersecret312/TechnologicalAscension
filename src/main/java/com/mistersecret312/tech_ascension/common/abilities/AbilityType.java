package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.util.Quality;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public interface AbilityType
{
    Codec<? extends AbilityType> getType();

    Component getDescription(Quality quality);
}
