package io.github.scaredsmods.potion_totems.datagen.api;

import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeBuilder;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeOutput;
import net.minecraft.advancements.Criterion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class SimpleRecipeBuilder implements RabbiRecipeBuilder {


    protected final ItemStack result;
    protected final ItemStack glass_bottle_result;

    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    protected String group;

    public SimpleRecipeBuilder(ItemStack result) {
        this.result = result;
        this.glass_bottle_result = new ItemStack(Items.GLASS_BOTTLE,1);
    }

    @Override
    public RabbiRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RabbiRecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }

    @Override
    public List<Item> getResult() {
        return List.of(this.result.getItem(), glass_bottle_result.getItem());
    }



}
