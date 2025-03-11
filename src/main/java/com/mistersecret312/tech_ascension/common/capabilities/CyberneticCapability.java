package com.mistersecret312.tech_ascension.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

public class CyberneticCapability implements INBTSerializable<CompoundTag>
{
    public static final String CYBERNETICS = "cybernetics";

    private ItemStackHandler handler = new ItemStackHandler(55);

    public void tick(Level level, LivingEntity living)
    {
        if(level.isClientSide() || level.getServer() == null)
            return;

    }

    public ItemStackHandler getHandler()
    {
        return handler;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();

        tag.put(CYBERNETICS, handler.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        handler.deserializeNBT(nbt.getCompound(CYBERNETICS));
    }
}
