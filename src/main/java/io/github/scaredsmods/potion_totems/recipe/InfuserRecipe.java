package io.github.scaredsmods.potion_totems.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.scaredsmods.potion_totems.registry.PTRecipes;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeSerializer;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public record InfuserRecipe(Ingredient inputItem1, Ingredient inputItem2, ItemStack output) implements RabbiRecipe<InfuserRecipeInput> {

    @Override
    public List<Ingredient> getIngredients() {
        return List.of(inputItem1, inputItem2);
    }

    @Override
    public boolean matches(InfuserRecipeInput infuserRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return inputItem1.test(infuserRecipeInput.getItems(0).getFirst()) && inputItem2.test(infuserRecipeInput.getItems(1).get(1));
    }

    @Override
    public NonNullList<ItemStack> assemble(InfuserRecipeInput infuserRecipeInput, HolderLookup.Provider provider) {
        NonNullList<ItemStack> list = NonNullList.create();
        list.add(output.copy());
        list.add(new ItemStack(Items.GLASS_BOTTLE));
        return list;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public NonNullList<ItemStack> getResults(HolderLookup.Provider provider) {
        NonNullList<ItemStack> list = NonNullList.create();
        list.add(output);
        list.add(new ItemStack(Items.GLASS_BOTTLE));
        return list;
    }

    @Override
    public RabbiRecipeSerializer<?> getSerializer() {
        return PTRecipes.INFUSER_SERIALIZER.get();
    }

    @Override
    public RabbiRecipeType<?> getType() {
        return PTRecipes.INFUSER_TYPE.get();
    }

    public static class Serializer implements RabbiRecipeSerializer<InfuserRecipe> {




        public static final MapCodec<InfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient_1").forGetter(InfuserRecipe::inputItem1),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient_2").forGetter(InfuserRecipe::inputItem2),
                ItemStack.CODEC.fieldOf("result").forGetter(InfuserRecipe::output)
        ).apply(inst, InfuserRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, InfuserRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, InfuserRecipe::inputItem1,
                        Ingredient.CONTENTS_STREAM_CODEC, InfuserRecipe::inputItem2,
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
