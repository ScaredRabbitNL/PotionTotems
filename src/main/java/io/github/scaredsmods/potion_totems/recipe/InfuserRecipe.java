package io.github.scaredsmods.potion_totems.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.scaredsmods.potion_totems.init.PTItems;
import io.github.scaredsmods.potion_totems.init.PTRecipes;
import io.github.scaredsmods.potion_totems.init.PTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public record InfuserRecipe(List<Ingredient> ingredients) implements Recipe<InfuserRecipeInput> {

    @Override
    public boolean matches(InfuserRecipeInput input, Level level) {
        boolean hasTotem = false;
        boolean hasPotionSource = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(PTTags.Items.TOTEMS)) {
                if (hasTotem) return false; // Only one totem allowed
                hasTotem = true;
            } else if (stack.is(Items.GLASS_BOTTLE) || stack.get(DataComponents.POTION_CONTENTS) != null) {
                hasPotionSource = true;
            } else {
                return false;
            }
        }

        return hasTotem && hasPotionSource;
    }

    @Override
    public ItemStack assemble(InfuserRecipeInput input, HolderLookup.Provider registries) {
        ItemStack totem = ItemStack.EMPTY;
        ItemStack potionSource = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(PTTags.Items.TOTEMS)) {
                totem = stack;
            } else if (stack.is(Items.GLASS_BOTTLE) || stack.get(DataComponents.POTION_CONTENTS) != null) {
                potionSource = stack;
            }
        }

        if (totem.isEmpty() || potionSource.isEmpty()) return ItemStack.EMPTY;

        PotionContents contents = potionSource.get(DataComponents.POTION_CONTENTS);
        ItemStack result = new ItemStack(PTItems.INFUSED_TOTEM.get());
        if (contents != null) {
            result.set(DataComponents.POTION_CONTENTS, contents);
        }

        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.copyOf(ingredients);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(PTItems.INFUSED_TOTEM.get());
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

        public static final MapCodec<InfuserRecipe> CODEC =
                RecordCodecBuilder.mapCodec(inst -> inst.group(
                        Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter(InfuserRecipe::ingredients)
                ).apply(inst, InfuserRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, InfuserRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<InfuserRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfuserRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static InfuserRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            int count = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int i = 0; i < count; i++) {
                ingredients.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }
            return new InfuserRecipe(ingredients);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, InfuserRecipe recipe) {
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ing : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
            }
        }
    }
}
