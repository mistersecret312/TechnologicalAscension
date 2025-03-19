package com.mistersecret312.tech_ascension.common.menu;

import com.mistersecret312.tech_ascension.client.screens.RipperdocChairScreen;
import com.mistersecret312.tech_ascension.common.init.BlockInit;
import com.mistersecret312.tech_ascension.common.init.CapabilityInit;
import com.mistersecret312.tech_ascension.common.init.MenuInit;
import com.mistersecret312.tech_ascension.common.menu.slot.CyberneticSlotItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class RipperdocChairMenu extends AbstractContainerMenu
{
    public final Level level;
    public final BlockPos pos;

    public RipperdocChairMenu(int id, Inventory inv, FriendlyByteBuf buf)
    {
        this(id, inv, inv.player, buf.readBlockPos());
    }

    public RipperdocChairMenu(int pContainerId, Inventory inv, Player player, BlockPos pos)
    {
        super(MenuInit.RIPPERDOC_CHAIR.get(), pContainerId);
        this.level = inv.player.level();
        this.pos = pos;

        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);

        player.getCapability(CapabilityInit.CYBERNETICS).ifPresent(cap ->
        {
            int id = 0;
            for (int j = 0; j < 6; j++)
            {
                for (int i = 0; i < 9; i++)
                {
                    this.addSlot(new CyberneticSlotItemHandler(cap.getHandler(), id, 8+(i*18), 18 + j * 18));
                    id++;
                }
            }
        });
    }

    private void addPlayerInventory(Inventory playerInventory)
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int l = 0; l < 9; ++l)
            {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 140 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory)
    {
        for (int i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 54;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT)
        {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false))
            {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        }
        else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT)
        {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false))
            {
                return ItemStack.EMPTY;
            }
        } else
        {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0)
        {
            sourceSlot.set(ItemStack.EMPTY);
        } else
        {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(ContainerLevelAccess.create(level, pos), player, BlockInit.RIPPERDOC_CHAIR.get());
    }



    @Override
    public void removed(Player player)
    {
        super.removed(player);
        if(!player.level().isClientSide() && this instanceof RipperdocChairMenu)
            player.getCapability(CapabilityInit.CYBERNETICS).ifPresent(cap -> cap.handleCybernetics(player));
    }
}
