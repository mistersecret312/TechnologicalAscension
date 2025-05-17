package com.mistersecret312.tech_ascension.common.datapack;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.abilities.AbilityType;
import com.mistersecret312.tech_ascension.common.init.AbilityInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class Cybernetics
{
    public static final ResourceLocation CYBERNETICS_LOCATION = new ResourceLocation(TechAscensionMod.MODID, "cybernetics");
    public static final ResourceKey<Registry<Cybernetics>> REGISTRY_KEY = ResourceKey.createRegistryKey(CYBERNETICS_LOCATION);
    public static final Codec<ResourceKey<Cybernetics>> RESOURCE_KEY_CODEC = ResourceKey.codec(REGISTRY_KEY);

    public static final Codec<Cybernetics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbilityInit.DISPATCHER.dispatchedCodec().listOf().fieldOf("abilities").forGetter(Cybernetics::getAbilities)
    ).apply(instance, Cybernetics::new));

    public List<AbilityType> abilities;
    public Cybernetics(List<AbilityType> abilities)
    {
        this.abilities = abilities;
    }

    public List<AbilityType> getAbilities()
    {
        return this.abilities;
    }
}
