package io.github.scaredsmods.potion_totems.getter;

import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeHolder;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeInput;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

public interface RecipeManagerExtensionGetter {


    default  <I extends RabbiRecipeInput, T extends RabbiRecipe<I>> Optional<RabbiRecipeHolder<T>> getRecipeFor(RabbiRecipeType<T> recipeType, I input, Level level) {
        return Optional.empty();
    }

    default  <I extends RabbiRecipeInput, T extends RabbiRecipe<I>> Optional<RabbiRecipeHolder<T>> getRecipeFor(
            RabbiRecipeType<T> recipeType, I input, Level level, @Nullable ResourceLocation lastRecipe
    ) {


        return Optional.empty();
    }

    default  <I extends RabbiRecipeInput, T extends RabbiRecipe<I>> Optional<RabbiRecipeHolder<T>> getRecipeFor(
            RabbiRecipeType<T> recipeType, I input, Level level, @Nullable RabbiRecipeHolder<T> lastRecipe
    ) {
        return Optional.empty();
    }
    default   <I extends RabbiRecipeInput, T extends RabbiRecipe<I>> Collection<RabbiRecipeHolder<T>> byType(RabbiRecipeType<T> type) {
        return null;
    }

    @Nullable
    default   <T extends RabbiRecipe<?>> RabbiRecipeHolder<T> byKeyTyped(RabbiRecipeType<T> type, ResourceLocation name) {

        return null;
    }


}
