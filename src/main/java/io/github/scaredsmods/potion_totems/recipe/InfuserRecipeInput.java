package io.github.scaredsmods.potion_totems.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record InfuserRecipeInput(NonNullList<ItemStack> inputs) implements RecipeInput {

    @Override
    public int size() {
        return 2;
    }

    @Override
    public ItemStack getItem(int i) {
        return inputs.get(i);
    }
}
