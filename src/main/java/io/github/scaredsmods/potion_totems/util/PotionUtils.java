package io.github.scaredsmods.potion_totems.util;

import io.github.scaredsmods.potion_totems.init.PTItems;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.*;

public class PotionUtils {

    public static ItemStack createItemStack(Item item, Holder<Potion> potion, int count) {
        ItemStack itemstack = new ItemStack(item);
        itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return itemstack;
    }

    public static void copyPotionContents(ItemStack source, ItemStack target) {
        if (source.has(DataComponents.POTION_CONTENTS)) {
            PotionContents contents = source.get(DataComponents.POTION_CONTENTS);
            target.set(DataComponents.POTION_CONTENTS, contents);
            if (source.has(DataComponents.CUSTOM_NAME)) {
                createNewItemName(target);
            }
        }
    }

    public static void copyPotionContents(ItemStack target, ItemStack... sources) {
        Set<Holder<MobEffect>> seenEffects = new HashSet<>();
        List<MobEffectInstance> combinedEffects = new ArrayList<>();

        for (ItemStack source : sources) {
            if (source.has(DataComponents.POTION_CONTENTS)) {
                PotionContents contents = source.get(DataComponents.POTION_CONTENTS);
                for (MobEffectInstance effect : contents.getAllEffects()) {
                    if (!seenEffects.contains(effect.getEffect())) {
                        seenEffects.add(effect.getEffect());
                        combinedEffects.add(effect);
                    }
                }
            }
        }

        if (!combinedEffects.isEmpty()) {
            PotionContents newContents = new PotionContents(
                    Optional.empty(),
                    Optional.empty(),
                    combinedEffects
            );
            target.set(DataComponents.POTION_CONTENTS, newContents);
        }

        for (ItemStack source : sources) {
            if (source.has(DataComponents.ITEM_NAME)) {
                PotionUtils.createNewItemName(source);
            }
        }
    }

    public static void createNewItemName(ItemStack stack) {
        if (stack.getItem() == PTItems.INFUSED_TOTEM.get()) {
            if (stack.has(DataComponents.POTION_CONTENTS)) {
                PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
                if (contents == null) {}
                contents.forEachEffect(effectInstance -> {
                    ResourceLocation loc = BuiltInRegistries.MOB_EFFECT.getKey(effectInstance.getEffect().value());
                    if (loc == null) {}
                    String namespace = loc.getNamespace();
                    String name = loc.getPath();
                    String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);
                    if (namespace.equals("minecraft") || namespace.equals("potion_totems")) {
                        stack.set(DataComponents.ITEM_NAME, Component.literal("Infused Totem of " + capitalizedName));
                    }
                });
            }
        }
    }
}
