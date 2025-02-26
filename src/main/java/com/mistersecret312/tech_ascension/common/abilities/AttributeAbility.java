package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.init.AbilityInit;
import com.mistersecret312.tech_ascension.common.items.CyberneticItem;
import com.mistersecret312.tech_ascension.common.util.Quality;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AttributeAbility implements AbilityType
{
    public static final Codec<AttributeAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.ATTRIBUTE).fieldOf("attribute").forGetter(AttributeAbility::getAttribute),
            StringRepresentable.fromEnum(Operation::values).fieldOf("operation").forGetter(AttributeAbility::getOperation),
            Codec.mapEither(Codec.DOUBLE.fieldOf("value"), Codec.DOUBLE.listOf().fieldOf("quality_values")).forGetter(AttributeAbility::getValues)
    ).apply(instance, AttributeAbility::new));

    public ResourceKey<Attribute> attribute;
    public Either<Double, List<Double>> values;
    public Operation operation;
    public AttributeAbility(ResourceKey<Attribute> attribute, Operation operation, Either<Double, List<Double>> values)
    {
        this.attribute = attribute;
        this.values = values;
        this.operation = operation;
    }

    public AttributeAbility()
    {
        this.attribute = ResourceKey.create(Registries.ATTRIBUTE, ForgeRegistries.ATTRIBUTES.getKey(Attributes.MAX_HEALTH));
        this.values = Either.left(1d);
        this.operation = Operation.MULTIPLY_BASE;
    }

    public ResourceKey<Attribute> getAttribute()
    {
        return attribute;
    }

    public Either<Double, List<Double>> getValues()
    {
        return values;
    }

    public Operation getOperation()
    {
        return operation;
    }

    @Override
    public Codec<? extends AbilityType> getType()
    {
        return AbilityInit.ATTRIBUTE.get();
    }

    @Override
    public void onAdded(LivingEntity entity, ItemStack stack)
    {
        Quality quality = ((CyberneticItem) stack.getItem()).getQuality(stack);
        UUID uuid = ((CyberneticItem) stack.getItem()).getUUID(stack);

        Attribute attributeTarget = ForgeRegistries.ATTRIBUTES.getValue(this.attribute.location());
        Double value = null;

        if(this.values.left().isPresent())
            value = values.left().get();

        if(this.values.right().isPresent())
            value = values.right().get().get(quality.ordinal());


        if(attributeTarget != null && value != null)
        {
            AttributeModifier modifier = new AttributeModifier(uuid, uuid.toString(), value, operation.toModifier());
            AttributeInstance instance = entity.getAttributes().getInstance(attributeTarget);

            if(instance != null)
                instance.addPermanentModifier(modifier);

        }
    }

    @Override
    public void onRemoved(LivingEntity entity, ItemStack stack)
    {
        UUID uuid = ((CyberneticItem) stack.getItem()).getUUID(stack);
        Attribute attributeTarget = ForgeRegistries.ATTRIBUTES.getValue(this.attribute.location());
        if(attributeTarget != null)
        {
            AttributeInstance instance = entity.getAttributes().getInstance(attributeTarget);
            if(instance != null)
                instance.removePermanentModifier(uuid);
        }
    }

    @Override
    public MutableComponent getDescription(ItemStack stack)
    {
        Quality quality = ((CyberneticItem) stack.getItem()).getQuality(stack);

        Optional<Double> baseValue = getValues().left();
        Optional<List<Double>> qualityValues = getValues().right();

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

            if(this.getOperation().equals(Operation.ADDITION))
            {
                String displayValue = fraction.format(value);
                if(value > 0)
                {
                    MutableComponent desc = Component.literal("+" + displayValue + " ");
                    return desc.append(Component.translatable("description.cybernetics."+operation.id+"."+attribute.location().getPath())).withStyle(ChatFormatting.AQUA);
                }
                if(value < 0)
                {
                    MutableComponent desc = Component.literal("-" + displayValue + " ");
                    return desc.append(Component.translatable("description.cybernetics."+operation.id+"."+attribute.location().getPath())).withStyle(ChatFormatting.RED);
                }

            }
            else
            {
                if(value < 0)
                    value = 1.0;

                value *= 100;

                if(value > 100)
                {
                    value -= 100;
                    String displayValue = fraction.format(value);

                    MutableComponent desc = Component.literal("+" + displayValue + "% ");
                    return desc.append(Component.translatable("description.cybernetics."+operation.id+"."+attribute.location().getPath())).withStyle(ChatFormatting.AQUA);
                }
                if(value > 0 && value < 100)
                {
                    String displayValue = fraction.format((100-value));

                    MutableComponent desc = Component.literal("-" + displayValue + "% ");
                    return desc.append(Component.translatable("description.cybernetics."+operation.id+"."+attribute.location().getPath())).withStyle(ChatFormatting.RED);

                }
            }
        }

        return Component.translatable("description.cybernetics.error").withStyle(ChatFormatting.DARK_RED);
    }

    public enum Operation implements StringRepresentable
    {
        ADDITION("addition"),
        MULTIPLY_TOTAL("multiply_total"),
        MULTIPLY_BASE("multiply_base");

        public final String id;
        Operation(String id)
        {
            this.id = id;
        }

        public AttributeModifier.Operation toModifier()
        {
            if(this.equals(ADDITION)) return AttributeModifier.Operation.ADDITION;
            if(this.equals(MULTIPLY_TOTAL)) return AttributeModifier.Operation.MULTIPLY_TOTAL;
            if(this.equals(MULTIPLY_BASE)) return AttributeModifier.Operation.MULTIPLY_BASE;

            return AttributeModifier.Operation.ADDITION;
        }

        @Override
        public String getSerializedName()
        {
            return id;
        }
    }
}
