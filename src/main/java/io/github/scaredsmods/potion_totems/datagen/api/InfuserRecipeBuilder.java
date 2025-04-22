package io.github.scaredsmods.potion_totems.datagen.api;

import io.github.scaredsmods.potion_totems.recipe.InfuserRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;

public class InfuserRecipeBuilder extends SimpleRecipeBuilder {


    private final Ingredient inputItem1;
    private final Ingredient inputItem2;

    public InfuserRecipeBuilder(ItemStack result, Ingredient inputItem1, Ingredient inputItem2) {
        super(result);
        this.inputItem1 = inputItem1;
        this.inputItem2 = inputItem2;
    }

    @Override
    public void save(RabbiRecipeOutput output, ResourceLocation id) {
        // Build the advancement.
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        // Our factory parameters are the result, the block state, and the ingredient.
        InfuserRecipe recipe = new InfuserRecipe(this.inputItem1, this.inputItem2, this.result);
        // Pass the id, the recipe, and the recipe advancement into the RecipeOutput.
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
    }
}
