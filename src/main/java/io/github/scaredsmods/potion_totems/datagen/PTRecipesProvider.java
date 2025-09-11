package io.github.scaredsmods.potion_totems.datagen;

import io.github.scaredsmods.potion_totems.init.PTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class PTRecipesProvider extends RecipeProvider {

    public PTRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, PTBlocks.INFUSER.get())
                .pattern(" T ")
                .pattern("LLL")
                .pattern("LBL")
                .define('T', Items.TOTEM_OF_UNDYING)
                .define('B', Items.GLASS_BOTTLE)
                .define('L', ItemTags.LOGS)
                .unlockedBy("has_totem", has(Items.TOTEM_OF_UNDYING))
                .unlockedBy("has_bottle", has(Items.GLASS_BOTTLE))
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(recipeOutput);
    }
}
