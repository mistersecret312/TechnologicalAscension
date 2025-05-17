package com.mistersecret312.tech_ascension.common.events;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.init.NetworkInit;
import com.mistersecret312.tech_ascension.common.network.packets.ActivateCyberneticsPacket;
import com.mistersecret312.tech_ascension.common.network.packets.SelectCyberneticsPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechAscensionMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientModEvents
{
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            while(TechAscensionMod.ClientModEvents.ACTIVATE_ABILITY.get().consumeClick())
            {
                NetworkInit.sendToServer(new ActivateCyberneticsPacket());
            }
            while(TechAscensionMod.ClientModEvents.NEXT_ABILITY.get().consumeClick())
            {
                NetworkInit.sendToServer(new SelectCyberneticsPacket());
            }
        }
    }
}
