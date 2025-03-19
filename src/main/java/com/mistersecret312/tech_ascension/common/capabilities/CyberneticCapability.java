package com.mistersecret312.tech_ascension.common.capabilities;

import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.items.CyberneticItem;
import com.mistersecret312.tech_ascension.common.items.QualityItem;
import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class CyberneticCapability implements INBTSerializable<CompoundTag>
{
    public static final String CYBERNETICSTORAGE = "cybernetic_storage";
    public static final String CYBERNETICS = "cybernetic_implants";

    private ItemStackHandler handler = new ItemStackHandler(54);
    private List<CyberneticData> appliedCybernetics = new ArrayList<>();

    public void tick(Level level, LivingEntity living)
    {
        if(level.isClientSide() || level.getServer() == null)
            return;

    }

    public ItemStackHandler getHandler()
    {
        return handler;
    }

    public void handleCybernetics(Player player)
    {
        if(!appliedCybernetics.isEmpty())
            handlePresentCybernetics(player);
        for (int i = 0; i < handler.getSlots(); i++)
        {
            ItemStack stack = handler.getStackInSlot(i);
            if(stack.getItem() instanceof CyberneticItem item)
            {
                UUID uuid = item.getUUID(stack);
                ResourceKey<Cybernetics> key = item.getCybernetics(stack);
                Quality quality = item.getQuality(stack);
                CyberneticData data = new CyberneticData(uuid, key, quality);


                Cybernetics cybernetics = player.level().getServer().registryAccess().registryOrThrow(Cybernetics.REGISTRY_KEY).get(item.getCybernetics(stack));
                if(cybernetics != null && !appliedCybernetics.contains(data))
                {
                    cybernetics.getAbilities().forEach(ability -> ability.onAdded(player, data));
                    appliedCybernetics.add(data);
                }

            }
        }

        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < handler.getSlots(); i++)
        {
            if(handler.getStackInSlot(i).getItem() instanceof CyberneticItem)
                stacks.add(handler.getStackInSlot(i));
        }
    }

    private void handlePresentCybernetics(Player player)
    {
        appliedCybernetics.forEach(data -> {
            boolean hasMatch = false;
            for (int i = 0; i < handler.getSlots(); i++)
            {
                ItemStack stack = handler.getStackInSlot(i);
                if(stack.getItem() instanceof CyberneticItem item)
                {
                    UUID itemUUID = item.getUUID(stack);
                    if(data.getUUID().equals(itemUUID))
                        hasMatch = true;
                }
            }

            if(!hasMatch)
            {
                Cybernetics cyber = player.level().getServer().registryAccess().registryOrThrow(Cybernetics.REGISTRY_KEY).get(data.key);
                if(cyber != null)
                {
                    cyber.getAbilities().forEach(ability -> ability.onRemoved(player, data));
                    appliedCybernetics.remove(data);
                }
            }

        });
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();

        tag.put(CYBERNETICSTORAGE, handler.serializeNBT());
        ListTag appliedCyberTag = new ListTag();
        appliedCybernetics.forEach(data -> {
            appliedCyberTag.add(data.serializeNBT());
        });
        tag.put(CYBERNETICS, appliedCyberTag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        handler.deserializeNBT(nbt.getCompound(CYBERNETICSTORAGE));
        ListTag cybernetics = nbt.getList(CYBERNETICS, ListTag.TAG_COMPOUND);
        appliedCybernetics.clear();
        cybernetics.forEach(tag -> appliedCybernetics.add(CyberneticData.deserializeNBT(nbt)));
    }

    public static class CyberneticData
    {
        private static final String UUID = "uuid";

        private UUID uuid;
        private ResourceKey<Cybernetics> key;
        private Quality quality;

        public CyberneticData(UUID uuid, ResourceKey<Cybernetics> key, Quality quality)
        {
            this.uuid = uuid;
            this.key = key;
            this.quality = quality;
        }

        public UUID getUUID()
        {
            return uuid;
        }

        public ResourceKey<Cybernetics> getKey()
        {
            return key;
        }

        public Quality getQuality()
        {
            return quality;
        }

        public CompoundTag serializeNBT()
        {
            CompoundTag tag = new CompoundTag();

            tag.putUUID(UUID, this.getUUID());
            tag.putString(CyberneticItem.CYBERNETICS, this.getKey().location().toString());
            tag.putString(QualityItem.QUALITY, this.getQuality().toString());

            return tag;
        }

        public static CyberneticData deserializeNBT(CompoundTag tag)
        {
            UUID uuid = tag.getUUID(UUID);
            ResourceKey<Cybernetics> key = CyberneticItem.getCyberneticTag(tag);
            Quality quality = QualityItem.getQualityTag(tag);

            return new CyberneticData(uuid, key, quality);
        }
    }
}
