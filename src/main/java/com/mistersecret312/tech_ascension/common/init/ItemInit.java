package com.mistersecret312.tech_ascension.common.init;

import com.mistersecret312.tech_ascension.TechAscensionMod;
import com.mistersecret312.tech_ascension.common.items.CyberneticItem;
import com.mistersecret312.tech_ascension.common.items.QualityItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TechAscensionMod.MODID);

    public static final RegistryObject<CyberneticItem> ARM = ITEMS.register("cyber_arm", () -> new CyberneticItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<CyberneticItem> ENDER_MODULE = ITEMS.register("ender_module", () -> new CyberneticItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus bus)
    {
        ITEMS.register(bus);
    }
}
