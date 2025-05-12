package com.mistersecret312.tech_ascension.common.abilities.data;

import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.items.CyberneticItem;
import com.mistersecret312.tech_ascension.common.items.QualityItem;
import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;

import java.util.UUID;

public abstract class CyberneticData
{
    public static final String UUID = "uuid";

    private java.util.UUID uuid;
    private ResourceKey<Cybernetics> key;
    private Quality quality;

    public CyberneticData()
    {

    }

    public CyberneticData(UUID uuid, ResourceKey<Cybernetics> key, Quality quality)
    {
        this.uuid = uuid;
        this.key = key;
        this.quality = quality;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public void setUUID(UUID uuid)
    {
        this.uuid = uuid;
    }

    public ResourceKey<Cybernetics> getKey()
    {
        return key;
    }

    public void setKey(ResourceKey<Cybernetics> key)
    {
        this.key = key;
    }

    public Quality getQuality()
    {
        return quality;
    }

    public void setQuality(Quality quality)
    {
        this.quality = quality;
    }

    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();

        tag.putUUID(UUID, this.getUUID());
        tag.putString(CyberneticItem.CYBERNETICS, this.getKey().location().toString());
        tag.putString(QualityItem.QUALITY, this.getQuality().toString());

        return tag;
    }

    public void deserializeNBT(CompoundTag tag)
    {
        this.uuid = tag.getUUID(UUID);
        this.key = CyberneticItem.getCyberneticTag(tag);
        this.quality = QualityItem.getQualityTag(tag);
    }

}
