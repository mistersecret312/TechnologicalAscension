package com.mistersecret312.tech_ascension.common.abilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IActivateAbility
{
    void onActivated(LivingEntity living, ItemStack stack);
}
