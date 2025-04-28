package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeModeTab;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.item.TotemItem;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotemItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.*;

public class PTItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, PotionTotems.MOD_ID);
    public static final ResourcefulRegistry<CreativeModeTab> TABS = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, PotionTotems.MOD_ID);



    //public static final RegistryEntry<Item> INFUSED_TOTEM = ITEMS.register("totem", PotionTotemItem::new);
    public static final RegistryEntry<Item> INFUSER_BLOCK_ITEM = ITEMS.register("infuser", () -> new BlockItem(PTBlocks.INFUSER.get(), new Item.Properties()));


    public static final RegistryEntry<CreativeModeTab> TOTEMS = TABS.register("totems", () -> new ResourcefulCreativeModeTab(PotionTotems.id("totems"))
            .setItemIcon(() -> Items.TOTEM_OF_UNDYING)
            .addContent(() ->
                    BuiltInRegistries.POTION.holders()
                            .filter(potion -> !potion.value().getEffects().isEmpty())
                            .map(potion -> PotionContents.createItemStack(Items.POTION, potion))
            )
            .addRegistry(ITEMS)
            .build());



    public static final Map<Holder<MobEffect>, RegistryEntry<Item>> TOTEM_EFFECTS = new HashMap<>();
    public static void register() {

        BuiltInRegistries.MOB_EFFECT.holders().forEach(holder -> {
            Optional<ResourceKey<MobEffect>> keyOpt = holder.unwrapKey();
            if (keyOpt.isEmpty()) return;

            ResourceKey<MobEffect> key = holder.getKey();
            MobEffect effect = holder.value();
            String totemName =  key.location().getPath() + "_infused_totem" ;


            RegistryEntry<Item> totem = ITEMS.register(totemName,
                    () -> new PotionTotemItem(holder));

            TOTEM_EFFECTS.put(holder, totem);
        });

    }






}
