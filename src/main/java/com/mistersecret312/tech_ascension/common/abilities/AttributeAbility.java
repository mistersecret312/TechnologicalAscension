package com.mistersecret312.tech_ascension.common.abilities;

import com.mistersecret312.tech_ascension.common.init.AbilityInit;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

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
    public MutableComponent getDescription(Quality quality)
    {
        Optional<Double> baseValue = getValues().left();
        Optional<List<Double>> qualityValues = getValues().right();

        Double value = null;
        if(baseValue.isPresent())
            value = baseValue.get();
        if(qualityValues.isPresent())
            value = qualityValues.get().get(quality.ordinal());

        if(value != null)
        {
            if(this.getOperation().equals(Operation.ADDITION))
            {
                MutableComponent desc = Component.empty();
                if(value > 0)
                    desc = Component.literal("+" + value + " ");
                if(value < 0)
                    desc = Component.literal("-" + value + " ");
                return desc.append(Component.translatable("description.cybernetics."+operation.id+"."+attribute.location().getPath())).withStyle(ChatFormatting.AQUA);

            }
            else
            {
                MutableComponent desc = Component.empty();
                if(value < 0)
                    value = 1.0;

                if(value > 1)
                    desc = Component.literal("+" + (value-1)*100 + "% ");
                if(value > 0 && value < 1)
                    desc = Component.literal("-" + (1-value)*100 + "% ");
                return desc.append(Component.translatable("description.cybernetics."+operation.id+"."+attribute.location().getPath())).withStyle(ChatFormatting.AQUA);
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
