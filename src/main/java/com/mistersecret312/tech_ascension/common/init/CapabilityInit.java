package com.mistersecret312.tech_ascension.common.init;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechAscensionMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityInit
{
    public static final Capability<CyberneticCapability> CYBERNETICS = CapabilityManager.get(new CapabilityToken<CyberneticCapability>() {});

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(CyberneticCapability.class);
    }
}
