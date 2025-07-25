package io.github.scaredsmods.potion_totems.recipe.brewing;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

public class InfusionRecipe implements IInfusionRecipe {

    private final Ingredient totem;
    private final DataComponentIngredient potion;
    private final ItemStack potionTotem;


    public InfusionRecipe(Ingredient totem, DataComponentIngredient potion, ItemStack potionTotem, ItemStack bottle) {
        this.totem = totem;
        this.potion = potion;
        this.potionTotem = potionTotem;

    }


    @Override
    public boolean isInput(ItemStack input) {
        return this.potion.test(input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return this.totem.test(ingredient);
    }

    public Ingredient getTotem() {
        return totem;
    }

    public DataComponentIngredient getPotion() {
        return potion;
    }

    public ItemStack getPotionTotem() {
        return potionTotem;
    }


    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        return isInput(input) && isIngredient(ingredient) ? getPotionTotem().copy() : ItemStack.EMPTY;
    }
}
