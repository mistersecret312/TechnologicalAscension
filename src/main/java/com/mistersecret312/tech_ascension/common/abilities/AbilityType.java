package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.util.Quality;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface AbilityType
{
    Codec<? extends AbilityType> getType();

    void onAdded(LivingEntity entity, Quality quality);
    void onRemoved(LivingEntity entity, Quality quality);

    Component getDescription(Quality quality);
}
