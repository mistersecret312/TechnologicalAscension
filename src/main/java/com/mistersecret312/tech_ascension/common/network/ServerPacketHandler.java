package com.mistersecret312.tech_ascension.common.network;

import com.mistersecret312.tech_ascension.common.capabilities.CyberneticCapability;
import com.mistersecret312.tech_ascension.common.init.CapabilityInit;
import com.mistersecret312.tech_ascension.common.network.packets.ActivateCyberneticsPacket;
import com.mistersecret312.tech_ascension.common.network.packets.SelectCyberneticsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ServerPacketHandler
{
    public static void handleActivationCyberneticsPacket(ServerPlayer player)
    {
        player.getCapability(CapabilityInit.CYBERNETICS).ifPresent(cap -> {
            cap.activateSelected(player);
        });
    }

    public static void handleSelectCyberneticsPacket(ServerPlayer player)
    {
        player.getCapability(CapabilityInit.CYBERNETICS).ifPresent(CyberneticCapability::advanceSelection);
    }
}
