package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface AbilityType
{
    Codec<? extends AbilityType> getType();

    void onAdded(LivingEntity entity, CyberneticCapability.CyberneticData data);
    void onRemoved(LivingEntity entity, CyberneticCapability.CyberneticData data);

    Component getDescription(ItemStack stack);
}
