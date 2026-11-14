package ru.nikkey.ankaria_bees.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.CreativeModeTabs;
import ru.nikkey.ankaria_bees.AnkariaBees;

@Mod.EventBusSubscriber(modid = AnkariaBees.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AnkariaBees.MODID);
    public static final RegistryObject<Item> COPPER = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_SCRAP = ITEMS.register("netherite_scrap_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLUORITE = ITEMS.register("fluorite_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MITHRIL = ITEMS.register("mithril_nugget", () -> new Item(new Item.Properties()));

    private ModItems() {}

    @SubscribeEvent
    public static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(COPPER);
            event.accept(NETHERITE_SCRAP);
            if (ModList.get().isLoaded("mekanism")) event.accept(FLUORITE);
            if (ModList.get().isLoaded("irons_spellbooks")) event.accept(MITHRIL);
        }
    }
}
