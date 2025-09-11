package io.github.scaredsmods.potion_totems.util;

import io.github.scaredsmods.potion_totems.init.PTItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Optional;

public class VillagerUtils {

    public static MerchantOffer getOffer(PotionType type, PotionContents contents, int xp, float priceMultiplier, int maxUses, int emeraldCost) {

        ItemStack stack;
        switch (type) {
            case POTION -> stack = new ItemStack(Items.POTION, 1);
            case POTION_TOTEM -> stack = new ItemStack(PTItems.INFUSED_TOTEM.get(), 1);
            default -> stack = null;
        }
        stack.set(DataComponents.POTION_CONTENTS, contents);
        return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), stack, maxUses, xp, priceMultiplier);
    }

    public static
    MerchantOffer getOffer(PotionType type, PotionContents contents, int xp, float priceMultiplier, int maxUses, int emeraldCost, int demand, int uses) {

        ItemStack stack;
        switch (type) {
            case POTION -> stack = new ItemStack(Items.POTION, 1);
            case POTION_TOTEM -> stack = new ItemStack(PTItems.INFUSED_TOTEM.get(), 1);
            default -> stack = null;
        }
        stack.set(DataComponents.POTION_CONTENTS, contents);
        return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), Optional.empty(), stack, uses, maxUses, xp, priceMultiplier, demand);
    }
}
