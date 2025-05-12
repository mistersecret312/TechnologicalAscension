package com.mistersecret312.tech_ascension;

import com.mistersecret312.tech_ascension.common.abilities.data.AttributeCyberneticData;
import com.mistersecret312.tech_ascension.common.abilities.data.BaseCyberneticData;
import com.mistersecret312.tech_ascension.common.abilities.data.CyberneticData;
import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.init.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TechAscensionMod.MODID)
public class TechAscensionMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tech_ascension";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public TechAscensionMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();


        ItemInit.register(modEventBus);
        BlockInit.register(modEventBus);
        MenuInit.register(modEventBus);
        ItemTabInit.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener((DataPackRegistryEvent.NewRegistry event) ->
        {
            event.dataPackRegistry(Cybernetics.REGISTRY_KEY, Cybernetics.CODEC, Cybernetics.CODEC);
        });

        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            CyberneticDataInit.register(new ResourceLocation(MODID, "empty"), BaseCyberneticData.class, BaseCyberneticData::new);
            CyberneticDataInit.register(new ResourceLocation(MODID, "attribute"), AttributeCyberneticData.class, AttributeCyberneticData::new);
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
