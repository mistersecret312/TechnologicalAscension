package com.mistersecret312.tech_ascension.common.init;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.datapack.Cybernetics;
import com.mistersecret312.tech_ascension.common.items.CyberneticItem;
import com.mistersecret312.tech_ascension.common.items.QualityItem;
import com.mistersecret312.tech_ascension.common.util.Quality;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ItemTabInit
{
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TechAscensionMod.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TABS.register("main_tab",
            () -> CreativeModeTab.builder().icon(() -> ItemInit.ARM.get().getDefaultInstance())
                    .title(Component.translatable("creativetab.main_tab"))
                    .displayItems((parameters, output) ->
                    {
                        output.accept(BlockInit.RIPPERDOC_CHAIR.get());
                        output.accept(CyberneticItem.create(ItemInit.ARM.get(), ResourceKey.create(Cybernetics.REGISTRY_KEY, new ResourceLocation("tech_ascension:cool_arm")), Quality.ICONIC));
                        output.accept(CyberneticItem.create(ItemInit.ENDER_MODULE.get(), ResourceKey.create(Cybernetics.REGISTRY_KEY, new ResourceLocation("tech_ascension:ender_module")), Quality.ICONIC));
                    })
                    .build());


    public static void register(IEventBus eventBus)
    {
        TABS.register(eventBus);
    }
}
