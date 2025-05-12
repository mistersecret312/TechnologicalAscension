package com.mistersecret312.tech_ascension.common.capabilities;

import com.mistersecret312.tech_ascension.common.abilities.AbilityType;
import com.mistersecret312.tech_ascension.common.abilities.ITickAbility;
import com.mistersecret312.tech_ascension.common.abilities.data.CyberneticData;
import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.init.AbilityInit;
import com.mistersecret312.tech_ascension.common.init.CyberneticDataInit;
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
    public static final String CYBERNETICSTORAGE = "cybernetic_storage";
    public static final String CYBERNETICS = "cybernetic_implants";

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
                        .forEach(ability -> ((ITickAbility) ability).tick(living, data));
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
                CyberneticData data = item.getCyberneticData(stack);

                Cybernetics cybernetics = player.level().getServer().registryAccess().registryOrThrow(Cybernetics.REGISTRY_KEY).get(item.getCybernetics(stack));
                if(cybernetics != null &&
                        appliedCybernetics.stream().noneMatch(dataKey -> dataKey.getUUID().equals(data.getUUID())))
                {
                    cybernetics.getAbilities().forEach(ability ->
                    {
                        ability.onAdded(player, data);
                    });
                    appliedCybernetics.add(data);
                }

            }
        }
    }

    private void handlePresentCybernetics(Player player)
    {
        Set<? extends CyberneticData> removed = new HashSet<>();
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
            Cybernetics cyber = player.level().getServer().registryAccess().registryOrThrow(Cybernetics.REGISTRY_KEY).get(data.getKey());
            if(cyber != null)
            {
                cyber.getAbilities().forEach(ability -> ability.onRemoved(player, data));
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
        cybernetics.forEach(tag ->
        {
            CompoundTag nbtTag = ((CompoundTag) tag);
            CyberneticData data = new CyberneticData();
            data.deserializeNBT(nbtTag);
            appliedCybernetics.add(data);
        });
    }


}
