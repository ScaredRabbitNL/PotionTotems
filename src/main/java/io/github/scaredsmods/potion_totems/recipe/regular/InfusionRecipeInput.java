package io.github.scaredsmods.potion_totems.recipe.regular;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record InfusionRecipeInput(ItemStack potion) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        NonNullList<ItemStack> list = NonNullList.create();

        list.add(potion);
        return list.get(index);
    }

    @Override
    public int size() {
        return 1;
    }
}
