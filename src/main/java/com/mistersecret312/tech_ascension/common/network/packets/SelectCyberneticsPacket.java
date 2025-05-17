package com.mistersecret312.tech_ascension.common.network.packets;

import com.mistersecret312.tech_ascension.common.network.ServerPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SelectCyberneticsPacket
{
    public SelectCyberneticsPacket()
    {

    }

    public static void write(SelectCyberneticsPacket packet, FriendlyByteBuf buffer)
    {

    }

    public static SelectCyberneticsPacket read(FriendlyByteBuf buffer)
    {
        return new SelectCyberneticsPacket();
    }

    public static void handle(SelectCyberneticsPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> ServerPacketHandler.handleSelectCyberneticsPacket(context.get().getSender()));
        context.get().setPacketHandled(true);
    }
}
