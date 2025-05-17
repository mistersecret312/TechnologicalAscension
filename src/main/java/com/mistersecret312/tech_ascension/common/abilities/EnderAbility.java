package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.init.AbilityInit;
import com.mistersecret312.tech_ascension.common.items.CyberneticItem;
import com.mistersecret312.tech_ascension.common.util.Quality;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;

public class EnderAbility implements AbilityType, IActivateAbility
{
    public static final Codec<EnderAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.mapEither(Codec.DOUBLE.fieldOf("range"), Codec.DOUBLE.listOf().fieldOf("quality_ranges")).forGetter(EnderAbility::getRange)
            ).apply(instance, EnderAbility::new));

    public Either<Double, List<Double>> range;
    public EnderAbility(Either<Double, List<Double>> range)
    {
        this.range = range;
    }

    public EnderAbility()
    {
        this.range = Either.left(0D);
    }

    public Either<Double, List<Double>> getRange()
    {
        return range;
    }

    @Override
    public Codec<? extends AbilityType> getType()
    {
        return AbilityInit.ENDER.get();
    }

    @Override
    public void onAdded(LivingEntity entity, ItemStack stack)
    {

    }

    @Override
    public void onRemoved(LivingEntity entity, ItemStack stack)
    {

    }

    @Override
    public Component getDescription(ItemStack stack)
    {
        if(stack.getItem() instanceof CyberneticItem cyberneticItem)
        {
            Quality quality = cyberneticItem.getQuality(stack);

            Optional<Double> baseValue = range.left();
            Optional<List<Double>> qualityValues = range.right();

            Double value = null;
            if(baseValue.isPresent())
                value = baseValue.get();
            if(qualityValues.isPresent())
                value = qualityValues.get().get(quality.ordinal());

            if(value != null)
            {
                NumberFormat fraction = NumberFormat.getNumberInstance();
                fraction.setParseIntegerOnly(false);
                fraction.setMaximumFractionDigits(1);
                fraction.setMinimumFractionDigits(0);

                String number = fraction.format(value);

                return Component.translatable("ability.ender.description", Component.literal(number).withStyle(ChatFormatting.LIGHT_PURPLE),
                        Component.keybind("tech_ascension.cybernetics.activate").withStyle(ChatFormatting.LIGHT_PURPLE))
                        .withStyle(ChatFormatting.DARK_PURPLE);
            }
        }
        return Component.empty();
    }

    @Override
    public void onActivated(LivingEntity living, ItemStack stack)
    {
        if(stack.getItem() instanceof CyberneticItem cyberneticItem)
        {
            if (living instanceof Player player)
            {
                Quality quality = cyberneticItem.getQuality(stack);

                Optional<Double> baseValue = range.left();
                Optional<List<Double>> qualityValues = range.right();

                Double value = null;
                if (baseValue.isPresent()) value = baseValue.get();
                if (qualityValues.isPresent()) value = qualityValues.get().get(quality.ordinal());

                if (value != null)
                {
                    BlockHitResult rayTrace = player.level().clip(new ClipContext(player.getEyePosition(1F), player.getEyePosition(1F).add(player.getLookAngle().scale(5F)),
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));

                    Vec3 pos = rayTrace.getBlockPos().getCenter().add(0, -0.5, 0);
                    Vec3 tpPos = pos.relative(rayTrace.getDirection(), 1);

                    living.teleportTo(tpPos.x, tpPos.y, tpPos.z);
                }
            }
        }

    }
}
