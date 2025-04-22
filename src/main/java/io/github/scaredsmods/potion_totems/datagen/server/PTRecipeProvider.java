package io.github.scaredsmods.potion_totems.datagen.server;

import io.github.scaredsmods.potion_totems.datagen.api.InfuserRecipeBuilder;
import io.github.scaredsmods.potion_totems.datagen.api.SimpleRecipeBuilder;
import io.github.scaredsmods.potion_totems.registry.PTItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PTRecipeProvider extends RecipeProvider {
    public PTRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        PTItems.ITEMS.boundStream().forEach(item -> {
            BuiltInRegistries.MOB_EFFECT.holders().forEach(holder -> {
                Optional<ResourceKey<MobEffect>> keyOpt = holder.unwrapKey();
                if (keyOpt.isEmpty()) return;
                ResourceKey<MobEffect> key = holder.getKey();
                BuiltInRegistries.POTION.holders().forEach(holder1 -> {
                    ItemStack input_item_2 = createPotionStack(Items.POTION, holder1);
                    new InfuserRecipeBuilder(item.getDefaultInstance(), Ingredient.of(Items.TOTEM_OF_UNDYING), Ingredient.of(input_item_2));
                });

            });

        });
    }

    public static ItemStack createPotionStack(Item baseItem, Holder<Potion> potion) {
        ItemStack stack = new ItemStack(baseItem);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return stack;
    }



}
