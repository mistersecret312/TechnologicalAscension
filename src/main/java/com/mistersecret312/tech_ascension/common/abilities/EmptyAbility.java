package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.abilities.data.BaseCyberneticData;
import com.mistersecret312.tech_ascension.common.abilities.data.CyberneticData;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EmptyAbility implements AbilityType<BaseCyberneticData>
{
    public EmptyAbility()
    {

    }

    public EmptyAbility(BaseCyberneticData data)
    {

    }

    @Override
    public Codec<? extends AbilityType<BaseCyberneticData>> getType()
    {
        return Codec.unit(new EmptyAbility());
    }

    @Override
    public void onAdded(LivingEntity entity, BaseCyberneticData data)
    {

    }

    @Override
    public void onRemoved(LivingEntity entity, BaseCyberneticData data)
    {

    }

    @Override
    public Component getDescription(ItemStack stack)
    {
        return Component.empty();
    }
}
