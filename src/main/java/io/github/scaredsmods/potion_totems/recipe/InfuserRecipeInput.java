package io.github.scaredsmods.potion_totems.recipe;

import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeInput;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public record InfuserRecipeInput(ItemStack input1, ItemStack input2) implements RabbiRecipeInput {
    @Override
    public NonNullList<ItemStack> getItems(int i) {
        NonNullList<ItemStack> list = NonNullList.create();
        list.add(input1);
        list.add(input2);
        return list;
    }

    @Override
    public int size() {
        return 2;
    }
}
