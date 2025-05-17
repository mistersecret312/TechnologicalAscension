package com.mistersecret312.tech_ascension.common.capabilities;

import com.mistersecret312.tech_ascension.common.abilities.IActivateAbility;
import com.mistersecret312.tech_ascension.common.abilities.ITickAbility;
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

import javax.annotation.Nullable;
import java.util.*;

public class CyberneticCapability implements INBTSerializable<CompoundTag>
{
    public static final String SELECTED_CYBERNETICS = "selected_cybernetics";
    public static final String CYBERNETICSTORAGE = "cybernetic_storage";
    public static final String CYBERNETICS = "cybernetic_implants";

    private int selectedCybernetics = 0;
    private ItemStackHandler handler = new ItemStackHandler(54);
    private List<CyberneticData> appliedCybernetics = new ArrayList<>();

    public void tick(Level level, LivingEntity living)
    {
        if(level.isClientSide() || level.getServer() == null)
            return;

        appliedCybernetics.forEach(data -> {
            Cybernetics cyber = level.getServer().registryAccess().registryOrThrow(Cybernetics.REGISTRY_KEY).get(data.getKey());
            if(cyber != null)
            {
                cyber.getAbilities().stream().filter(ability -> ability instanceof ITickAbility)
                        .forEach(ability -> ((ITickAbility) ability).tick(living, handler.getStackInSlot(data.slotID)));
            }
        });

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
                CyberneticData data = new CyberneticData(i, uuid, key, quality);

                Cybernetics cybernetics = player.level().getServer().registryAccess().registryOrThrow(Cybernetics.REGISTRY_KEY).get(item.getCybernetics(stack));
                if(cybernetics != null &&
                        appliedCybernetics.stream().noneMatch(dataKey -> dataKey.uuid.equals(uuid)))
                {
                    cybernetics.getAbilities().forEach(ability -> ability.onAdded(player, stack));
                    appliedCybernetics.add(data);
                }

            }
        }
    }

    private void handlePresentCybernetics(Player player)
    {
        Set<CyberneticData> removed = new HashSet<>();
        appliedCybernetics.forEach(data -> {
            boolean hasMatch = false;
            for (int i = 0; i < handler.getSlots(); i++)
            {
                ItemStack stack = handler.getStackInSlot(i);
                if(stack.getItem() instanceof CyberneticItem item)
                {
                    UUID itemUUID = item.getUUID(stack);
                    if(data.getUUID().equals(itemUUID))
                    {
                        hasMatch = true;
                        break;
                    }
                }
            }

            if(!hasMatch)
                removed.add(data);

        });

        removed.forEach(data -> {
            appliedCybernetics.remove(data);
            Cybernetics cyber = player.level().getServer().registryAccess().registryOrThrow(Cybernetics.REGISTRY_KEY).get(data.key);
            if(cyber != null)
            {
                cyber.getAbilities().forEach(ability -> ability.onRemoved(player, handler.getStackInSlot(data.getSlotID())));
            }
        });
    }

    public void activateSelected(Player player)
    {
        CyberneticData data = appliedCybernetics.get(selectedCybernetics);
        Cybernetics cyber = player.level().getServer().registryAccess().registryOrThrow(Cybernetics.REGISTRY_KEY).get(data.key);
        cyber.getAbilities().forEach(ability ->
        {
            if(ability instanceof IActivateAbility activateAbility)
                activateAbility.onActivated(player, handler.getStackInSlot(data.slotID));
        });
    }

    public void advanceSelection()
    {
        this.selectedCybernetics += 1;
        if(this.selectedCybernetics > appliedCybernetics.size())
            this.selectedCybernetics = 0;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();

        tag.putInt(SELECTED_CYBERNETICS, this.selectedCybernetics);
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
        this.selectedCybernetics = nbt.getInt(SELECTED_CYBERNETICS);
        handler.deserializeNBT(nbt.getCompound(CYBERNETICSTORAGE));
        ListTag cybernetics = nbt.getList(CYBERNETICS, ListTag.TAG_COMPOUND);
        appliedCybernetics.clear();
        cybernetics.forEach(tag ->
        {
            CyberneticData data = new CyberneticData();
            CyberneticData newData = data.deserializeNBT(((CompoundTag) tag));
            appliedCybernetics.add(newData);
        });
    }

    public static class CyberneticData
    {
        private static final String ID = "id";
        private static final String UUID = "uuid";

        private int slotID;
        private UUID uuid;
        private ResourceKey<Cybernetics> key;
        private Quality quality;

        public CyberneticData()
        {}

        public CyberneticData(int slotID, UUID uuid, ResourceKey<Cybernetics> key, Quality quality)
        {
            this.slotID = slotID;
            this.uuid = uuid;
            this.key = key;
            this.quality = quality;
        }

        public int getSlotID()
        {
            return slotID;
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

            tag.putInt(ID, this.getSlotID());
            tag.putUUID(UUID, this.getUUID());
            tag.putString(CyberneticItem.CYBERNETICS, this.getKey().location().toString());
            tag.putString(QualityItem.QUALITY, this.getQuality().toString());

            return tag;
        }

        public CyberneticData deserializeNBT(CompoundTag tag)
        {
            int id = tag.getInt(ID);
            UUID uuid = tag.getUUID(UUID);
            ResourceKey<Cybernetics> key = CyberneticItem.getCyberneticTag(tag);
            Quality quality = QualityItem.getQualityTag(tag);

            return new CyberneticData(id, uuid, key, quality);
        }
    }
}
