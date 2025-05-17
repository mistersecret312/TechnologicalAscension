package com.mistersecret312.tech_ascension.common.init;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.abilities.AbilityType;
import com.mistersecret312.tech_ascension.common.abilities.AttributeAbility;
import com.mistersecret312.tech_ascension.common.abilities.EnderAbility;
import com.mistersecret312.tech_ascension.common.util.RegistryDispatcher;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = TechAscensionMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AbilityInit
{
    public static final HashMap<String, AbilityType> TYPE_SET = new HashMap<>();
    public static final RegistryDispatcher<AbilityType> DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
            FMLJavaModLoadingContext.get().getModEventBus(),
            new ResourceLocation(TechAscensionMod.MODID, "ability_type"),
            AbilityType::getType, // using a method reference here seems to confuse eclipse
            builder -> {}
    );

    public static final RegistryObject<Codec<AttributeAbility>> ATTRIBUTE = register("attribute",
            () -> AttributeAbility.CODEC, new AttributeAbility());
    public static final RegistryObject<Codec<EnderAbility>> ENDER = register("ender",
            () -> EnderAbility.CODEC, new EnderAbility());

    public static <S extends AbilityType> RegistryObject<Codec<S>> register(String name, Supplier<Codec<S>> supplier, S type)
    {
        TYPE_SET.put(name, type);
        return DISPATCHER.registry().register(name, supplier);
    }
}
