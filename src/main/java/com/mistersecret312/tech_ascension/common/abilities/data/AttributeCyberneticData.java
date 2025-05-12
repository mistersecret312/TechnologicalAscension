package com.mistersecret312.tech_ascension.common.abilities.data;

import com.mistersecret312.tech_ascension.common.abilities.AttributeAbility;
import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.util.Quality;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttributeCyberneticData extends BaseCyberneticData
{
    public static final String ATTRIBUTE = "attribute";
    public static final String VALUE = "value";
    public static final String VALUES = "values";
    public static final String OPERATION = "operation";

    public ResourceKey<Attribute> attribute;
    public Either<Double, List<Double>> values;
    public AttributeAbility.Operation operation;

    public AttributeCyberneticData()
    {

    }

    public AttributeCyberneticData(java.util.UUID uuid, ResourceKey<Cybernetics> key, Quality quality,
                                   ResourceKey<Attribute> attribute, Either<Double, List<Double>> values,
                                   AttributeAbility.Operation operation)
    {
        super(uuid, key, quality);
        this.attribute = attribute;
        this.values = values;
        this.operation = operation;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        return super.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag tag)
    {
        super.deserializeNBT(tag);
        this.attribute = ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.parse(tag.getString(ATTRIBUTE)));

        if(tag.contains(VALUES))
        {
            ListTag listTag = tag.getList(VALUES, Tag.TAG_DOUBLE);
            if(listTag.size() != Quality.values().length)
                this.values = Either.left(listTag.getDouble(0));
            else
            {
                List<Double> values = new ArrayList<>();
                for (int i = 0; i < listTag.size(); i++)
                {
                    values.add(listTag.getDouble(i));
                }
                this.values = Either.right(values);
            }
        }
        else this.values = Either.left(tag.getDouble(VALUE));

        this.operation = AttributeAbility.Operation.valueOf(tag.getString(OPERATION));
    }

    public ResourceKey<Attribute> getAttribute()
    {
        return attribute;
    }

    public Either<Double, List<Double>> getValues()
    {
        return values;
    }

    public AttributeAbility.Operation getOperation()
    {
        return operation;
    }

    public void setAttribute(ResourceKey<Attribute> attribute)
    {
        this.attribute = attribute;
    }

    public void setValues(Either<Double, List<Double>> values)
    {
        this.values = values;
    }

    public void setOperation(AttributeAbility.Operation operation)
    {
        this.operation = operation;
    }
}
