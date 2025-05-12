package com.mistersecret312.tech_ascension.common.abilities.data;

import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.items.CyberneticItem;
import com.mistersecret312.tech_ascension.common.items.QualityItem;
import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;

import java.util.UUID;

public class ActivationCyberneticData extends CyberneticData
{
    public static final String ACTIVATED = "activated";

    public boolean activated;

    public ActivationCyberneticData(UUID uuid, ResourceKey<Cybernetics> key, Quality quality, boolean active)
    {
        super(uuid, key, quality);
        this.activated = active;
    }

    public boolean isActive()
    {
        return activated;
    }

    public void setActive(boolean activated)
    {
        this.activated = activated;
    }

    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();

        tag.putUUID(UUID, this.getUUID());
        tag.putString(CyberneticItem.CYBERNETICS, this.getKey().location().toString());
        tag.putString(QualityItem.QUALITY, this.getQuality().toString());
        tag.putBoolean(ACTIVATED, this.isActive());

        return tag;
    }

    public void deserializeNBT(CompoundTag tag)
    {
        this.setUUID(tag.getUUID(UUID));
        this.setKey(CyberneticItem.getCyberneticTag(tag));
        this.setQuality(QualityItem.getQualityTag(tag));
        this.setActive(tag.getBoolean(ACTIVATED));
    }
}
