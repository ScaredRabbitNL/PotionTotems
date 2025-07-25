package io.github.scaredsmods.potion_totems.recipe.regular;

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

public record InfusionRecipe(Ingredient potion, ItemStack output) implements Recipe<InfusionRecipeInput> {


    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(potion);
        return list;
    }

    @Override
    public boolean matches(InfusionRecipeInput input, Level level) {
        if (level.isClientSide()) return false;
        return potion.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(InfusionRecipeInput input, HolderLookup.Provider registries) {
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
        return PTRecipes.INFUSER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PTRecipes.INFUSER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<InfusionRecipe> {

        MapCodec<InfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("potion_ing").forGetter(InfusionRecipe::potion),
                        ItemStack.CODEC.fieldOf("result").forGetter(InfusionRecipe::output)
                ).apply(inst, InfusionRecipe::new));

        StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, InfusionRecipe::potion,
                ItemStack.STREAM_CODEC, InfusionRecipe::output,
                InfusionRecipe::new
        );

        @Override
        public MapCodec<InfusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
