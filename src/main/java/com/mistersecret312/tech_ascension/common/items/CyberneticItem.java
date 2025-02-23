package com.mistersecret312.tech_ascension.common.items;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CyberneticItem extends QualityItem
{
    public static final String CYBERNETICS = "cybernetics";

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
        {
            cybernetics.getAbilities().forEach(ability -> pTooltipComponents.add(ability.getDescription(this.getQuality(pStack))));
        }

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
}
