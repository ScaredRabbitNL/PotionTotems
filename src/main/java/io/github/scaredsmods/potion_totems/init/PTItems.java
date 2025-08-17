package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeModeTab;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotemItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PTItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, PotionTotemsMain.MOD_ID);
    public static final ResourcefulRegistry<CreativeModeTab> TABS = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, PotionTotemsMain.MOD_ID);


    public static final RegistryEntry<Item> INFUSED_TOTEM = ITEMS.register("infused_totem", PotionTotemItem::new);



    public static final RegistryEntry<Item> INFUSER_BLOCK_ITEM = ITEMS.register("infuser", () -> new BlockItem(PTBlocks.INFUSER.get(), new Item.Properties()));


    public static final RegistryEntry<CreativeModeTab> TOTEMS = TABS.register("totems", () -> new ResourcefulCreativeModeTab(PotionTotemsMain.id("totems"))
            .setItemIcon(() -> Items.TOTEM_OF_UNDYING)
            .addRegistry(ITEMS)
            .build());



}
