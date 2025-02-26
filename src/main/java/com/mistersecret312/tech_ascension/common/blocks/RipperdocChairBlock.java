package com.mistersecret312.tech_ascension.common.blocks;

import com.mistersecret312.tech_ascension.common.menu.RipperdocChairMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class RipperdocChairBlock extends Block
{
    public RipperdocChairBlock(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit)
    {

        if(!level.isClientSide())
            player.openMenu(new MenuProvider()
            {
                @Override
                public Component getDisplayName()
                {
                    return Component.translatable("screen.tech_ascension.ripperdoc_chair");
                }

                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
                {
                    return new RipperdocChairMenu(windowId, inventory, player, pos);
                }
            });

        return InteractionResult.SUCCESS;
    }

}
