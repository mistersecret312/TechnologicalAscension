package com.mistersecret312.tech_ascension.common.init;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.abilities.AbilityType;
import com.mistersecret312.tech_ascension.common.abilities.data.BaseCyberneticData;
import com.mistersecret312.tech_ascension.common.abilities.data.CyberneticData;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;

public class CyberneticDataInit
{
    public static final String DATA_TYPE = "data_type";

    public static final ResourceLocation BASIC_LOCATION = new ResourceLocation(TechAscensionMod.MODID, "empty");

    public static final ResourceKey<Registry<BaseCyberneticData>> EMPTY_CYBERNETIC_DATA = ResourceKey.createRegistryKey(BASIC_LOCATION);

    private static final HashMap<ResourceLocation, CyberneticDataConstructor> DATA_TYPES = new HashMap<>();
    private static final HashMap<Class<? extends CyberneticData>, ResourceLocation> LOCATIONS = new HashMap<>();

    public static <T extends CyberneticData, U extends AbilityType<T>> void register(ResourceLocation resourceLocation, Class<T> objectClass, CyberneticDataConstructor<T> constructor)
    {
        if(DATA_TYPES.containsKey(resourceLocation))
            throw new IllegalStateException("Duplicate registration for " + resourceLocation.toString());
        if(LOCATIONS.containsKey(objectClass))
            throw new IllegalStateException("Duplicate registration for " + objectClass.getName());

        DATA_TYPES.put(resourceLocation, constructor);
        LOCATIONS.put(objectClass, resourceLocation);
    }

    @Nullable
    public static CyberneticData constructObject(ResourceLocation resourceLocation)
    {
        if(DATA_TYPES.containsKey(resourceLocation))
            return DATA_TYPES.get(resourceLocation).create();

        return null;
    }

    @Nullable
    public static ResourceLocation getResourceLocation(CyberneticData object)
    {
        if(object != null && LOCATIONS.containsKey(object.getClass()))
            return LOCATIONS.get(object.getClass());

        return null;
    }

    public interface CyberneticDataConstructor<T extends CyberneticData>
    {
        T create();
    }

}
