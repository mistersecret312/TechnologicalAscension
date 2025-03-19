package com.mistersecret312.tech_ascension.common.items;

import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QualityItem extends Item
{
    public static final String QUALITY = "quality";

    public QualityItem(Properties pProperties)
    {
        super(pProperties);
    }

    public static ItemStack create(QualityItem item, Quality quality)
    {
        ItemStack stack = new ItemStack(item);
        item.setQuality(stack, quality);

        return stack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("quality."+this.getQuality(pStack).toString().toLowerCase()).withStyle(this.getQuality(pStack).format));
    }

    public Quality getQuality(ItemStack stack)
    {
        if(stack.getTag() != null && stack.getTag().contains(QUALITY))
            try
            {
                return Quality.valueOf(stack.getTag().getString(QUALITY));
            } catch (IllegalArgumentException e) {
                this.setQuality(stack, Quality.COMMON);
                return Quality.COMMON;
            }
        else return Quality.COMMON;
    }

    public void setQuality(ItemStack stack, Quality quality)
    {
        stack.getOrCreateTag().putString(QUALITY, quality.toString());
    }

    public static Quality getQualityTag(CompoundTag tag)
    {
        if(tag != null && tag.contains(QUALITY))
            try
            {
                return Quality.valueOf(tag.getString(QUALITY));
            } catch (IllegalArgumentException e) {
                return Quality.COMMON;
            }
        else return Quality.COMMON;
    }
}
