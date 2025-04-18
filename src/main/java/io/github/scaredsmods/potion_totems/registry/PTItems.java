package io.github.scaredsmods.potion_totems.registry;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeModeTab;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.potion_totems.items.PotionTotemItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PTItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, PotionTotems.MOD_ID);
    public static final ResourcefulRegistry<CreativeModeTab> TABS = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, PotionTotems.MOD_ID);

    public static final RegistryEntry<CreativeModeTab> TOTEMS = TABS.register("totems", () -> new ResourcefulCreativeModeTab(PotionTotems.id("totems"))
            .setItemIcon(() -> Items.TOTEM_OF_UNDYING)
            .addRegistry(ITEMS)
            .build());


    public static final ResourcefulRegistry<Item> B_ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, PotionTotems.MOD_ID);
    public static final RegistryEntry<Item> BASE_TOTEM =  B_ITEMS.register("base_totem", () -> new PotionTotemItem(MobEffects.ABSORPTION));


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
        ITEMS.init();
        B_ITEMS.init();
    }
}
