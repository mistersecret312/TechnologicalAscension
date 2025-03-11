package com.mistersecret312.tech_ascension.common.events;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import com.mistersecret312.tech_ascension.common.capabilities.GenericProvider;
import com.mistersecret312.tech_ascension.common.init.CapabilityInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechAscensionMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents
{

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof Player)
            event.addCapability(new ResourceLocation(TechAscensionMod.MODID, "cybernetics"), new GenericProvider<>(CapabilityInit.CYBERNETICS, new CyberneticCapability()));
    }

    @SubscribeEvent
    public static void entityHurt(LivingAttackEvent event)
    {

    }

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event)
    {
        event.getEntity().getCapability(CapabilityInit.CYBERNETICS).ifPresent(cap ->
        {
            cap.tick(event.getEntity().level(), event.getEntity());
        });
    }

}
