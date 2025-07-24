package io.github.scaredsmods.potion_totems.item;

import io.github.scaredsmods.potion_totems.components.PotionTotemContents;
import io.github.scaredsmods.potion_totems.event.RegisterInfusionRecipesEvent;
import io.github.scaredsmods.potion_totems.init.PTDataComponents;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotem;
import io.github.scaredsmods.potion_totems.recipe.IInfusionRecipe;
import io.github.scaredsmods.potion_totems.recipe.InfusionRecipe;
import io.github.scaredsmods.potion_totems.recipe.InfusionRecipeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TotemInfusion {

    public static final TotemInfusion EMPTY = new TotemInfusion(List.of(), List.of(), List.of());
    private final List<Ingredient> containers;
    private final List<TotemInfusion.Mix<PotionTotem>> potionTotemMixes;
    private final List<TotemInfusion.Mix<Item>> containerMixes;
    private final InfusionRecipeRegistry registry;

    TotemInfusion(List<Ingredient> containers, List<TotemInfusion.Mix<PotionTotem>> potionTotemMixes, List<TotemInfusion.Mix<Item>> containerMixes) {
        this(containers, potionTotemMixes, containerMixes, List.of());
    }

    TotemInfusion(List<Ingredient> containers, List<TotemInfusion.Mix<PotionTotem>> potionTotemMixes, List<TotemInfusion.Mix<Item>> containerMixes, List<IInfusionRecipe> recipes) {
        this.containers = containers;
        this.potionTotemMixes = potionTotemMixes;
        this.containerMixes = containerMixes;
        this.registry = new InfusionRecipeRegistry(recipes);
    }

    public boolean isIngredient(ItemStack stack) {
        return this.registry.isValidIngredient(stack) || this.isContainerIngredient(stack) || this.isPotionIngredient(stack);
    }

    /**
     * Checks if an item stack is a valid input for brewing,
     * for use in the lower 3 slots where water bottles would normally go.
     */
    public boolean isInput(ItemStack stack) {
        return this.registry.isValidInput(stack) || isContainer(stack);
    }

    /**
     * Retrieves recipes that use the more general interface.
     * This does NOT include the container and potion mixes.
     */
    public List<IInfusionRecipe> getRecipes() {
        return registry.recipes();
    }


    private boolean isContainer(ItemStack stack) {
        for (Ingredient ingredient : this.containers) {
            if (ingredient.test(stack)) {
                return true;
            }
        }

        return false;
    }

    public boolean isContainerIngredient(ItemStack stack) {
        for (TotemInfusion.Mix<Item> mix : this.containerMixes) {
            if (mix.ingredient.test(stack)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPotionIngredient(ItemStack stack) {
        for (TotemInfusion.Mix<PotionTotem> mix : this.potionTotemMixes) {
            if (mix.ingredient.test(stack)) {
                return true;
            }
        }

        return false;
    }

    public boolean isBrewablePotion(Holder<PotionTotem> potionTotem) {
        for (TotemInfusion.Mix<PotionTotem> mix : this.potionTotemMixes) {
            if (mix.to.is(potionTotem)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasMix(ItemStack reagent, ItemStack potionItem) {
        if (registry.hasOutput(reagent, potionItem)) return true;
        return this.isContainer(reagent) && (this.hasContainerMix(reagent, potionItem) || this.hasPotionMix(reagent, potionItem));
    }

    public boolean hasContainerMix(ItemStack reagent, ItemStack potionItem) {
        for (TotemInfusion.Mix<Item> mix : this.containerMixes) {
            if (reagent.is(mix.from) && mix.ingredient.test(potionItem)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPotionMix(ItemStack reagent, ItemStack potionItem) {
        Optional<Holder<PotionTotem>> optional = reagent.getOrDefault(PTDataComponents.POTION_TOTEM_CONTENTS, PotionTotemContents.EMPTY).potionTotem();
        if (optional.isEmpty()) {
            return false;
        } else {
            for (TotemInfusion.Mix<PotionTotem> mix : this.potionTotemMixes) {
                if (mix.from.is(optional.get()) && mix.ingredient.test(potionItem)) {
                    return true;
                }
            }

            return false;
        }
    }

    public ItemStack mix(ItemStack potion, ItemStack potionItem) {
        if (potionItem.isEmpty()) {
            return potionItem;
        } else {
            var customMix = registry.getOutput(potionItem, potion); // Parameters are swapped compared to what vanilla passes!
            if (!customMix.isEmpty()) return customMix;
            Optional<Holder<PotionTotem>> optional = potionItem.getOrDefault(PTDataComponents.POTION_TOTEM_CONTENTS, PotionTotemContents.EMPTY).potionTotem();
            if (optional.isEmpty()) {
                return potionItem;
            } else {
                for (TotemInfusion.Mix<Item> mix : this.containerMixes) {
                    if (potionItem.is(mix.from) && mix.ingredient.test(potion)) {
                        return PotionTotemContents.createItemStack(mix.to.value(), optional.get());
                    }
                }

                for (TotemInfusion.Mix<PotionTotem> mix1 : this.potionTotemMixes) {
                    if (mix1.from.is(optional.get()) && mix1.ingredient.test(potion)) {
                        return PotionTotemContents.createItemStack(potionItem.getItem(), mix1.to);
                    }
                }

                return potionItem;
            }
        }
    }

    public static TotemInfusion bootstrap(FeatureFlagSet enabledFeatures, RegistryAccess registryAccess) {
        TotemInfusion.Builder potionbrewingBuilder = new TotemInfusion.Builder(enabledFeatures);
        addMixes(potionbrewingBuilder);
        NeoForge.EVENT_BUS.post(new RegisterInfusionRecipesEvent(potionbrewingBuilder, registryAccess));
        return potionbrewingBuilder.build();
    }

    public static void addMixes(TotemInfusion.Builder builder) {

    }
    public static class Builder {
        private final List<Ingredient> containers = new ArrayList<>();
        private final List<TotemInfusion.Mix<PotionTotem>> potionMixes = new ArrayList<>();
        private final List<TotemInfusion.Mix<Item>> containerMixes = new ArrayList<>();
        private final List<IInfusionRecipe> recipes = new ArrayList<>();
        private final FeatureFlagSet enabledFeatures;

        public Builder(FeatureFlagSet enabledFeatures) {
            this.enabledFeatures = enabledFeatures;
        }

        private static void expectPotion(Item item) {
            if (!(item instanceof PotionItem)) {
                throw new IllegalArgumentException("Expected a potion, got: " + BuiltInRegistries.ITEM.getKey(item));
            }
        }

        public void addContainerRecipe(Item input, Item reagent, Item result) {
            if (input.isEnabled(this.enabledFeatures) && reagent.isEnabled(this.enabledFeatures) && result.isEnabled(this.enabledFeatures)) {
                expectPotion(input);
                expectPotion(result);
                this.containerMixes
                        .add(new TotemInfusion.Mix<>(input.builtInRegistryHolder(), Ingredient.of(reagent), result.builtInRegistryHolder()));
            }
        }

        public void addContainer(Item container) {
            if (container.isEnabled(this.enabledFeatures)) {
                expectPotion(container);
                this.containers.add(Ingredient.of(container));
            }
        }

        public void addMix(Holder<PotionTotem> input, Item reagent, Holder<PotionTotem> result) {
            if (input.value().isEnabled(this.enabledFeatures)
                    && reagent.isEnabled(this.enabledFeatures)
                    && result.value().isEnabled(this.enabledFeatures)) {
                this.potionMixes.add(new TotemInfusion.Mix<>(input, Ingredient.of(reagent), result));
            }
        }

        public void addStartMix(Item reagent, Holder<PotionTotem> result) {
            if (result.value().isEnabled(this.enabledFeatures)) {
                //TODO: ADD VALUES
            }
        }

        /**
         * Adds a new simple brewing recipe.
         *
         * @param input      the ingredient that goes in the same slot as water bottles would
         * @param ingredient the ingredient that goes in the same slot as nether wart would
         * @param output     the item stack that will replace the input once brewing is done
         */
        public void addRecipe(Ingredient input, DataComponentIngredient ingredient, ItemStack output) {
            addRecipe(new InfusionRecipe(input, ingredient, output, new ItemStack(Items.GLASS_BOTTLE)));
        }

        /**
         * Adds a new brewing recipe with custom logic.
         */
        public void addRecipe(IInfusionRecipe recipe) {
            this.recipes.add(recipe);
        }

        public TotemInfusion build() {
            return new TotemInfusion(List.copyOf(this.containers), List.copyOf(this.potionMixes), List.copyOf(this.containerMixes), List.copyOf(this.recipes));
        }
    }

    static record Mix<T>(Holder<T> from, Ingredient ingredient, Holder<T> to) {
    }
}
