package io.github.scaredsmods.potion_totems.recipe;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

import java.util.List;

public record InfusionRecipeRegistry(List<IInfusionRecipe> recipes) {


    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!input.isEmpty() && input.getCount() == 1) {
            if (ingredient.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                for(IInfusionRecipe recipe : this.recipes) {
                    ItemStack output = recipe.getOutput(input, ingredient);
                    if (!output.isEmpty()) {
                        return output;
                    }
                }
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }



    public boolean hasOutput(ItemStack input, ItemStack ingredient) {
       return !getOutput(input, ingredient).isEmpty();
    }

   public boolean isValidIngredient(ItemStack stack) {
       if (stack.isEmpty()) {
           return false;
       } else {
           for(IInfusionRecipe recipe : this.recipes) {
               if (recipe.isIngredient(stack)) {
                   return true;
               }
           }

           return false;
       }
   }

   public boolean isValidInput(ItemStack stack) {
       for(IInfusionRecipe recipe : this.recipes) {
           if (recipe.isInput(stack)) {
               return true;
           }
       }

       return false;
   }
}
