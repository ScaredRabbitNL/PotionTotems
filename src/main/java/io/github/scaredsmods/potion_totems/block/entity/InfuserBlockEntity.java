package io.github.scaredsmods.potion_totems.block.entity;

import io.github.scaredsmods.potion_totems.block.InfuserBlock;
import io.github.scaredsmods.potion_totems.components.PotionTotemContents;
import io.github.scaredsmods.potion_totems.init.PTBlockEntities;
import io.github.scaredsmods.potion_totems.init.PTDataComponents;
import io.github.scaredsmods.potion_totems.init.PTRecipes;
import io.github.scaredsmods.potion_totems.recipe.IInfusionRecipe;
import io.github.scaredsmods.potion_totems.recipe.InfuserRecipe;
import io.github.scaredsmods.potion_totems.recipe.InfusionRecipe;
import io.github.scaredsmods.potion_totems.recipe.InfusionRecipeRegistry;
import io.github.scaredsmods.potion_totems.screen.menu.InfuserMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.brewing.PotionBrewEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InfuserBlockEntity extends BaseContainerBlockEntity {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private static final int TOTEM_INPUT_SLOT = 0;
    private static final int POTION_INPUT_SLOT = 1;
    private static final int INFUSED_TOTEM_OUTPUT_SLOT = 2;
    private static final int BOTTLE_OUTPUT_SLOT = 3;
    private int maxProgress = 72;
    private int currentProgress = 0;
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private Item ingredient;
    boolean isBrewing = false;


    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0  -> InfuserBlockEntity.this.currentProgress;
                case 1 -> InfuserBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index){
                case 0: InfuserBlockEntity.this.currentProgress = value;
                case 1: InfuserBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public InfuserBlockEntity(BlockPos pos, BlockState blockState) {
        super(PTBlockEntities.BE_INFUSER.get(), pos, blockState);

    }


    private void resetProgress() {
        currentProgress = 0;
        maxProgress = 72;
    }


    public void tick(Level level, BlockPos pos, BlockState state, InfuserBlockEntity blockEntity) {

    }


    @Override
    protected Component getDefaultName() {
        return Component.translatable("potion_totems.be.infuser.defaultname");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new InfuserMenu(containerId, inventory, this, this.data);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.currentProgress = tag.getInt("potion_totems.infuser.currentProgress");
        if (this.currentProgress > 0) {
            this.ingredient = this.items.get(3).getItem();
        }
    }



    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("potion_totems.infuser.currentProgress", this.currentProgress);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("potion_totems.infuser.maxProgress", this.maxProgress);
    }

}
