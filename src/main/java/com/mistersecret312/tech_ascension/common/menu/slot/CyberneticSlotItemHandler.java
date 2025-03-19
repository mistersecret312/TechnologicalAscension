package com.mistersecret312.tech_ascension.common.menu.slot;

import com.mistersecret312.tech_ascension.common.items.CyberneticItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CyberneticSlotItemHandler extends SlotItemHandler
{
    public CyberneticSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack)
    {
        return !stack.isEmpty() && stack.getItem() instanceof CyberneticItem;
    }
}
