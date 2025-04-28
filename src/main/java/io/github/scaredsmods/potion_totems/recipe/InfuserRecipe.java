package io.github.scaredsmods.potion_totems.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.scaredsmods.potion_totems.init.PTRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record InfuserRecipe(Ingredient input1, Ingredient input2, ItemStack output) implements Recipe<InfuserRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input1);
        list.add(input2);
        return list;
    }

    @Override
    public boolean matches(InfuserRecipeInput infuserRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return input1.test(infuserRecipeInput.getItem(0)) && input2.test(infuserRecipeInput.getItem(1));
    }

    @Override
    public ItemStack assemble(InfuserRecipeInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PTRecipes.INFUSER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PTRecipes.INFUSER_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<InfuserRecipe> {



        public static final MapCodec<InfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient_1").forGetter(InfuserRecipe::input1),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient_2").forGetter(InfuserRecipe::input2),
                ItemStack.CODEC.fieldOf("result").forGetter(InfuserRecipe::output)
        ).apply(inst, InfuserRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, InfuserRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, InfuserRecipe::input1,
                        Ingredient.CONTENTS_STREAM_CODEC, InfuserRecipe::input2,
                        ItemStack.STREAM_CODEC, InfuserRecipe::output,
                        InfuserRecipe::new);


        @Override
        public MapCodec<InfuserRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfuserRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }


}
