package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.abilities.data.CyberneticData;
import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import net.minecraft.world.entity.LivingEntity;

public interface ITickAbility
{
    void tick(LivingEntity living, CyberneticData data);
}
