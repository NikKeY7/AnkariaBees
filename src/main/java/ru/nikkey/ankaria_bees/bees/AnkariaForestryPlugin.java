package ru.nikkey.ankaria_bees.bees;

import forestry.api.IForestryApi;
import forestry.api.apiculture.ForestryBeeSpecies;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.ForestryTaxa;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.plugin.IApicultureRegistration;
import forestry.api.plugin.IBeeSpeciesBuilder;
import forestry.api.plugin.IForestryPlugin;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.items.EnumHoneyComb;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import ru.nikkey.ankaria_bees.AnkariaBees;
import ru.nikkey.ankaria_bees.registry.ModItems;

import java.awt.Color;

public final class AnkariaForestryPlugin implements IForestryPlugin {
    private static final ResourceLocation RADIATION = id("radiation");
    private static ResourceLocation id(String path) { return new ResourceLocation(AnkariaBees.MODID, path); }
    private static ResourceLocation bee(String name) { return id("bee_" + name); }

    @Override public ResourceLocation id() { return id("bees"); }

    @Override public void registerApiculture(IApicultureRegistration api) {
        register(api, "iron", 0xb7c1c4, Items.IRON_NUGGET, ForestryBeeSpecies.INDUSTRIOUS, ForestryBeeSpecies.FOREST);
        register(api, "copper", 0xc46d46, ModItems.COPPER.get(), ForestryBeeSpecies.INDUSTRIOUS, ForestryBeeSpecies.MEADOWS);
        register(api, "gold", 0xf2c844, Items.GOLD_NUGGET, ForestryBeeSpecies.NOBLE, ForestryBeeSpecies.MODEST)
            .setTemperature(TemperatureType.HOT).setHumidity(HumidityType.ARID);
        register(api, "ancient", 0x584147, ModItems.NETHERITE_SCRAP.get(), ForestryBeeSpecies.SEETHING, bee("gold"))
            .setTemperature(TemperatureType.HELLISH).setHumidity(HumidityType.ARID)
            .setGenome(g -> { g.set(BeeChromosomes.FLOWER_TYPE, ForestryAlleles.FLOWER_TYPE_NETHER); g.set(BeeChromosomes.CAVE_DWELLING, true); });
        register(api, "redstone", 0xc51f1f, Items.REDSTONE, ForestryBeeSpecies.NOBLE, ForestryBeeSpecies.MEADOWS);

        if (ModList.get().isLoaded("mekanism")) {
            api.registerBeeEffect(RADIATION, new RadiationBeeEffect());
            register(api, "fluorite", 0xa477d5, ModItems.FLUORITE.get(), ForestryBeeSpecies.NOBLE, ForestryBeeSpecies.WINTRY)
                .setTemperature(TemperatureType.ICY)
                .setGenome(g -> g.set(BeeChromosomes.TEMPERATURE_TOLERANCE, ForestryAlleles.TOLERANCE_UP_1));
            register(api, "steel", 0x747b88, item("mekanism:nugget_steel"), bee("iron"), ForestryBeeSpecies.SEETHING)
                .setTemperature(TemperatureType.HOT).setHumidity(HumidityType.ARID);
            register(api, "lead", 0x626b90, item("mekanism:nugget_lead"), bee("iron"), ForestryBeeSpecies.MODEST);
            register(api, "osmium", 0x91bcd4, item("mekanism:nugget_osmium"), ForestryBeeSpecies.INDUSTRIOUS, ForestryBeeSpecies.DILIGENT);
            register(api, "uranium", 0x77b62d, item("mekanism:nugget_uranium"), bee("lead"), ForestryBeeSpecies.UNWEARY)
                .setGenome(g -> g.set(BeeChromosomes.EFFECT, IForestryApi.INSTANCE.getAlleleManager().registryAllele(RADIATION, BeeChromosomes.EFFECT)));
            register(api, "tin", 0xd1e4e7, item("mekanism:nugget_tin"), ForestryBeeSpecies.INDUSTRIOUS, ForestryBeeSpecies.COMMON);
            register(api, "bronze", 0xce8b45, item("mekanism:nugget_bronze"), bee("tin"), bee("copper"));
        }
        if (ModList.get().isLoaded("irons_spellbooks")) {
            register(api, "mithril", 0x99dcee, ModItems.MITHRIL.get(), bee("ancient"), ForestryBeeSpecies.EDENIC);
        }
    }

    private static IBeeSpeciesBuilder register(IApicultureRegistration api, String name, int color, Item product, ResourceLocation first, ResourceLocation second) {
        return add(api, name, color, product).addMutations(m -> m.add(first, second, 10));
    }

    private static IBeeSpeciesBuilder add(IApicultureRegistration api, String name, int color, Item product) {
        return api.registerSpecies(bee(name), ForestryTaxa.GENUS_HONEY, name, true, new Color(color))
            .addProduct(ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.HONEY), 0.30f)
            .addProduct(new ItemStack(product), product == Items.REDSTONE ? 0.05f : 0.15f);
    }

    private static Item item(String name) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
        if (item == null) throw new IllegalStateException("Missing bee product " + name);
        return item;
    }
}
