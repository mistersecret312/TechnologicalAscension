package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface AbilityType<T extends CyberneticCapability.CyberneticData>
{
    Codec<? extends AbilityType> getType();

    void onAdded(LivingEntity entity, T data);
    void onRemoved(LivingEntity entity, T data);

    Component getDescription(ItemStack stack);
}
