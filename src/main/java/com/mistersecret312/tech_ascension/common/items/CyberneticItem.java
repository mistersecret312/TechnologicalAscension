package com.mistersecret312.tech_ascension.common.items;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CyberneticItem extends QualityItem
{
    public static final String CYBERNETICS = "cybernetics";
    public static final String CYBER_UUID = "cyberUUID";

    public CyberneticItem(Properties pProperties)
    {
        super(pProperties);
    }

    public static ItemStack create(CyberneticItem item, ResourceKey<Cybernetics> cybernetics, Quality quality)
    {
        ItemStack stack = QualityItem.create(item, quality);
        item.setCybernetics(stack, cybernetics);

        return stack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents,
                                TooltipFlag pIsAdvanced)
    {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        Minecraft minecraft = Minecraft.getInstance();
        ClientPacketListener clientPacketListener = minecraft.getConnection();
        RegistryAccess registries = clientPacketListener.registryAccess();
        Registry<Cybernetics> cyberneticsRegistry = registries.registryOrThrow(Cybernetics.REGISTRY_KEY);

        cyberneticsRegistry.entrySet();
        Cybernetics cybernetics = cyberneticsRegistry.get(this.getCybernetics(pStack));
        if(cybernetics != null)
            cybernetics.getAbilities().forEach(ability -> pTooltipComponents.add(ability.getDescription(pStack)));

    }

    public void setCybernetics(ItemStack stack, ResourceKey<Cybernetics> cybernetics)
    {
        stack.getOrCreateTag().putString(CYBERNETICS, cybernetics.location().toString());
    }

    @Nullable
    public ResourceKey<Cybernetics> getCybernetics(ItemStack stack)
    {
        String ID = "";
        if(stack.getTag() != null && stack.getTag().contains(CYBERNETICS))
        {
            ID = stack.getTag().getString(CYBERNETICS);
        }
        if(!ID.isBlank())
        {
            ResourceLocation id = ResourceLocation.tryParse(ID);
            if(id == null)
                id = new ResourceLocation(TechAscensionMod.MODID, "empty");
            return ResourceKey.create(Cybernetics.REGISTRY_KEY, id);
        }

        return null;
    }

    public UUID getUUID(ItemStack stack)
    {
        if(stack.getTag() != null)
        {
            if(!stack.getTag().contains(CYBER_UUID))
                stack.getOrCreateTag().putUUID(CYBER_UUID, UUID.randomUUID());
        }
        else
        {
            stack.getOrCreateTag().putUUID(CYBER_UUID, UUID.randomUUID());
        }
        return stack.getTag().getUUID(CYBER_UUID);
    }

    public static ResourceKey<Cybernetics> getCyberneticTag(CompoundTag tag)
    {
        String ID = "";
        if(tag != null && tag.contains(CYBERNETICS))
        {
            ID = tag.getString(CYBERNETICS);
        }
        if(!ID.isBlank())
        {
            ResourceLocation id = ResourceLocation.tryParse(ID);
            if(id == null)
                id = new ResourceLocation(TechAscensionMod.MODID, "empty");
            return ResourceKey.create(Cybernetics.REGISTRY_KEY, id);
        }

        return null;
    }
}
