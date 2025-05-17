package com.mistersecret312.tech_ascension.common.network.packets;

import com.mistersecret312.tech_ascension.common.network.ServerPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ActivateCyberneticsPacket
{
    public ActivateCyberneticsPacket()
    {

    }

    public static void write(ActivateCyberneticsPacket packet, FriendlyByteBuf buffer)
    {
    }

    public static ActivateCyberneticsPacket read(FriendlyByteBuf buffer)
    {
        return new ActivateCyberneticsPacket();
    }

    public static void handle(ActivateCyberneticsPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> ServerPacketHandler.handleActivationCyberneticsPacket(context.get().getSender()));
        context.get().setPacketHandled(true);
    }
}
