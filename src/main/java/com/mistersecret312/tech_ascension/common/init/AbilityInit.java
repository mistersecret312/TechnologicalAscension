package com.mistersecret312.tech_ascension.common.init;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.abilities.AbilityType;
import com.mistersecret312.tech_ascension.common.abilities.AttributeAbility;
import com.mistersecret312.tech_ascension.common.abilities.EmptyAbility;
import com.mistersecret312.tech_ascension.common.abilities.data.AttributeCyberneticData;
import com.mistersecret312.tech_ascension.common.abilities.data.BaseCyberneticData;
import com.mistersecret312.tech_ascension.common.abilities.data.CyberneticData;
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
    public static final HashMap<AbilityType<? extends CyberneticData>, CyberneticDataInit.CyberneticDataConstructor<? extends CyberneticData>> ID_SET = new HashMap<>();
    public static final HashMap<Class<? extends AbilityType<?>>, String> CONSTRUCTOR_SET = new HashMap<>();
    public static final RegistryDispatcher<AbilityType<?>> DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
            FMLJavaModLoadingContext.get().getModEventBus(),
            new ResourceLocation(TechAscensionMod.MODID, "ability_type"),
            AbilityType::getType, // using a method reference here seems to confuse eclipse
            builder -> {}
    );

    public static final RegistryObject<Codec<AttributeAbility>> ATTRIBUTE = register("attribute",
            () -> AttributeAbility.CODEC, new AttributeAbility(), AttributeAbility.class, AttributeCyberneticData::new);
    public static final RegistryObject<Codec<EmptyAbility>> EMPTY = register("empty",
            () -> Codec.unit(new EmptyAbility()), new EmptyAbility(), EmptyAbility.class, BaseCyberneticData::new);

    public static <T extends CyberneticData, S extends AbilityType<T>> RegistryObject<Codec<S>> register(String name, Supplier<Codec<S>> supplier, S type, Class<S> typeClass, CyberneticDataInit.CyberneticDataConstructor<T> data)
    {
        CONSTRUCTOR_SET.put(typeClass, name);
        ID_SET.put(type, data);
        return DISPATCHER.registry().register(name, supplier);
    }
}
