package com.mistersecret312.tech_ascension.common.abilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ITickAbility
{
    void tick(LivingEntity living, ItemStack stack);
}
