package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import net.minecraft.world.entity.LivingEntity;

public interface ITickAbility<T extends CyberneticCapability.CyberneticData>
{
    void tick(LivingEntity living, T data);
}
