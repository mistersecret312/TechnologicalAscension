package com.mistersecret312.tech_ascension.common.abilities.data;

import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.resources.ResourceKey;
import java.util.UUID;

public class BaseCyberneticData extends CyberneticData
{
    public BaseCyberneticData()
    {

    }

    public BaseCyberneticData(UUID uuid, ResourceKey<Cybernetics> key, Quality quality)
    {
        super(uuid, key, quality);
    }
}
