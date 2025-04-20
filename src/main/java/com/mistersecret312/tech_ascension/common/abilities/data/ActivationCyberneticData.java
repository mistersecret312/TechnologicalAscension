package com.mistersecret312.tech_ascension.common.abilities.data;

import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;

import java.util.UUID;

public class ActivationCyberneticData extends CyberneticCapability.CyberneticData
{
    public static final String ACTIVATED = "activated";

    public boolean activated;

    public ActivationCyberneticData(UUID uuid, ResourceKey<Cybernetics> key, Quality quality, CompoundTag tag)
    {
        super(uuid, key, quality, tag);
    }

    public boolean isActive()
    {
        return activated;
    }

    public void setActive(boolean activated)
    {
        this.activated = activated;
        CompoundTag tag = this.getExtraData();
        tag.putBoolean(ACTIVATED, activated);
    }

    @Override
    public void loadData(CompoundTag tag)
    {
        super.loadData(tag);
        this.setActive(tag.getBoolean(ACTIVATED));
    }
}
