package com.mistersecret312.tech_ascension.common.blocks;

import com.mistersecret312.tech_ascension.common.menu.RipperdocChairMenu;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class RipperdocChairBlock extends Block implements EntityBlock
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
            NetworkHooks.openScreen(((ServerPlayer) player), new MenuProvider()
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
            }, pos);

        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return null;
    }
}
