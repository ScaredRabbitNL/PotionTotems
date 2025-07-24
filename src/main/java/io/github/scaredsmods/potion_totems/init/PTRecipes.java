package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.recipe.InfuserRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class PTRecipes {

    public static final ResourcefulRegistry<RecipeSerializer<?>> SERIALIZERS = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_SERIALIZER, PotionTotemsMain.MOD_ID);
    public static final ResourcefulRegistry<RecipeType<?>> TYPES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_TYPE, PotionTotemsMain.MOD_ID);




    public static final RegistryEntry<RecipeSerializer<InfuserRecipe>> INFUSER_SERIALIZER = SERIALIZERS.register("infuser", InfuserRecipe.Serializer::new);
    public static final RegistryEntry<RecipeType<InfuserRecipe>> INFUSER_TYPE = TYPES.register("infuser" ,() -> new RecipeType<>() {
        @Override
        public String toString() {
            return "infuser";
        }
    });
}
