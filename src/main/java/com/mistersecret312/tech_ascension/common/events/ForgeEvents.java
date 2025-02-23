package com.mistersecret312.tech_ascension.common.events;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import com.mistersecret312.tech_ascension.common.capabilities.GenericProvider;
import com.mistersecret312.tech_ascension.common.init.CapabilityInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechAscensionMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents
{

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<LivingEntity> event)
    {
        if(event.getObject() instanceof Player)
            event.addCapability(new ResourceLocation(TechAscensionMod.MODID, "cybernetics"), new GenericProvider<>(CapabilityInit.CYBERNETICS, new CyberneticCapability()));
    }

}
