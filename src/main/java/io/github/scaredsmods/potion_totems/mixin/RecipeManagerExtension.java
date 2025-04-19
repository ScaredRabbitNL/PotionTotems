package io.github.scaredsmods.potion_totems.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeHolder;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeInput;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public class RecipeManagerExtension implements RecipeManagerExtensionGetter {

    Multimap<RabbiRecipeType<?>, RabbiRecipeHolder<?>> byRabbiType = ImmutableMultimap.of();
    Map<ResourceLocation, RabbiRecipeHolder<?>> byRabbiName = ImmutableMap.of();

    @Override
    public <I extends RabbiRecipeInput, T extends RabbiRecipe<I>> Optional<RabbiRecipeHolder<T>> getRecipeFor(RabbiRecipeType<T> recipeType, I input, Level level) {
        return this.getRecipeFor(recipeType, input, level, (RabbiRecipeHolder<T>)null); }


    @Override
    public <I extends RabbiRecipeInput, T extends RabbiRecipe<I>> Optional<RabbiRecipeHolder<T>> getRecipeFor(RabbiRecipeType<T> recipeType, I input, Level level, @Nullable ResourceLocation lastRecipe) {
        RabbiRecipeHolder<T> recipeholder = lastRecipe != null ? this.byKeyTyped(recipeType, lastRecipe) : null;
        return this.getRecipeFor(recipeType, input, level, recipeholder);
    }

    @Override
    public <I extends RabbiRecipeInput, T extends RabbiRecipe<I>> Optional<RabbiRecipeHolder<T>> getRecipeFor(RabbiRecipeType<T> recipeType, I input, Level level, @Nullable RabbiRecipeHolder<T> lastRecipe) {
        if (input.isEmpty()) {
            return Optional.empty();
        } else {
            return lastRecipe != null && lastRecipe.value().matches(input, level)
                    ? Optional.of(lastRecipe)
                    : this.byType(recipeType).stream().filter(p_344413_ -> p_344413_.value().matches(input, level)).findFirst();
        }
    }

    @Override
    public <I extends RabbiRecipeInput, T extends RabbiRecipe<I>> Collection<RabbiRecipeHolder<T>> byType(RabbiRecipeType<T> type) {
        return (Collection<RabbiRecipeHolder<T>>)(Collection<?>)byRabbiType.get(type);
    }

    @Nullable
    @Override
    public  <T extends RabbiRecipe<?>> RabbiRecipeHolder<T> byKeyTyped(RabbiRecipeType<T> type, ResourceLocation name) {
        RabbiRecipeHolder<?> recipeholder = byRabbiName.get(name);
        return (RabbiRecipeHolder<T>)(recipeholder != null && recipeholder.value().getType().equals(type) ? recipeholder : null);
    }
}
