package io.github.scaredsmods.potion_totems.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.recipe.InfuserRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeSerializer;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeType;
import io.github.scaredsmods.rabbilib.key.RabbiResourceKeys;
import io.github.scaredsmods.rabbilib.registry.RabbiRegistries;

public class PTRecipes {

    public static final ResourcefulRegistry<RabbiRecipeSerializer<?>> RECIPE_SERIALIZERS = ResourcefulRegistries.create(RabbiRegistries.RECIPE_SERIALIZER, PotionTotems.MOD_ID);
    public static final ResourcefulRegistry<RabbiRecipeType<?>> RECIPE_TYPES = ResourcefulRegistries.create(RabbiRegistries.RECIPE_TYPE, PotionTotems.MOD_ID);

    public static final RegistryEntry<RabbiRecipeSerializer<InfuserRecipe>> INFUSER_SERIALIZER = RECIPE_SERIALIZERS.register("infuser", InfuserRecipe.Serializer::new);
    public static final RegistryEntry<RabbiRecipeType<InfuserRecipe>> INFUSER_TYPE = RECIPE_TYPES.register("infuser", () -> new RabbiRecipeType<>() {
        @Override
        public String toString() {
            return "infuser";
        }
    });
}
