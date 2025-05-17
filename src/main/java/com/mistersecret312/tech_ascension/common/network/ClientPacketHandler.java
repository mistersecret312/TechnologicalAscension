package com.mistersecret312.tech_ascension.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ClientPacketHandler
{


    @SuppressWarnings("unchecked")
    public static <T extends Entity> T getEntity(int entityId) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return null;
        Entity entity = level.getEntity(entityId);
        return (T) entity;
    }

    public static <T extends BlockEntity> T getBlockEntity(BlockPos pos)
    {
        ClientLevel level = Minecraft.getInstance().level;
        if(level == null)
            return null;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return (T) blockEntity;
    }

    public static LocalPlayer getPlayer()
    {
        return Minecraft.getInstance().player;
    }
}
