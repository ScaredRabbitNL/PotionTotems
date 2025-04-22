package io.github.scaredsmods.potion_totems.recipe;

import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeInput;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record InfuserRecipeInput(ItemStack input1, ItemStack input2) implements RabbiRecipeInput {
    @Override
    public NonNullList<ItemStack> getItems(int i) {
        return NonNullList.of(input1, input2);
    }

    @Override
    public int size() {
        return 2;
    }
}
