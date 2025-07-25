package io.github.scaredsmods.potion_totems.block.entity;

import io.github.scaredsmods.potion_totems.init.PTBlockEntities;
import io.github.scaredsmods.potion_totems.init.PTDataComponents;
import io.github.scaredsmods.potion_totems.init.PTRecipes;
import io.github.scaredsmods.potion_totems.recipe.regular.InfusionRecipe;
import io.github.scaredsmods.potion_totems.recipe.regular.InfusionRecipeInput;
import io.github.scaredsmods.potion_totems.screen.menu.InfuserMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class InfuserBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    //Totem input  -> Infused totem output
    //Potion input -> Glass bottle output
    private static final int TOTEM_INPUT_SLOT = 0;
    private static final int POTION_INPUT_SLOT = 1;
    private static final int INFUSED_TOTEM_OUTPUT_SLOT = 2;
    private static final int BOTTLE_OUTPUT_SLOT = 3;
    private int maxProgress = 72;
    private int currentProgress = 0;


    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0  -> InfuserBlockEntity.this.currentProgress;
                case 1 -> InfuserBlockEntity.this.maxProgress;
                default -> 4;
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
            return 4;
        }
    };

    public InfuserBlockEntity(BlockPos pos, BlockState blockState) {
        super(PTBlockEntities.BE_INFUSER.get(), pos, blockState);

    }


    private void resetProgress() {
        currentProgress = 0;
        maxProgress = 72;
    }


    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem() {

        Optional<RecipeHolder<InfusionRecipe>> recipe = getCurrentRecipe();


        ItemStack output = recipe.get().value().output();


        itemStackHandler.extractItem(TOTEM_INPUT_SLOT, 1, false);
        itemStackHandler.extractItem(POTION_INPUT_SLOT, 1, false);

        itemStackHandler.setStackInSlot(INFUSED_TOTEM_OUTPUT_SLOT, new ItemStack(output.getItem(),
                itemStackHandler.getStackInSlot(INFUSED_TOTEM_OUTPUT_SLOT).getCount() + output.getCount()));


    }

    private  boolean hasCraftingFinished() {
        return this.currentProgress >= this.maxProgress;
    }

    private  void increaseCraftingProgress() {
        currentProgress++;
    }

    private  boolean hasRecipe() {
        Optional<RecipeHolder<InfusionRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }


        ItemStack infused_totem = recipe.get().value().output();
        ItemStack bottle = itemStackHandler.getStackInSlot(BOTTLE_OUTPUT_SLOT);
        return (canInsertAmountIntoOutputSlot(infused_totem.getCount(), TOTEM_INPUT_SLOT) && canInsertItemIntoOutputSlot(infused_totem, TOTEM_INPUT_SLOT))
                &&
                (canInsertAmountIntoOutputSlot(bottle.getCount(), BOTTLE_OUTPUT_SLOT) && canInsertItemIntoOutputSlot(bottle, BOTTLE_OUTPUT_SLOT));
    }

    private Optional<RecipeHolder<InfusionRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(PTRecipes.INFUSER_TYPE.get(), new InfusionRecipeInput(itemStackHandler.getStackInSlot(POTION_INPUT_SLOT)), level);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output, int slot) {
        return itemStackHandler.getStackInSlot(slot).isEmpty() ||
                itemStackHandler.getStackInSlot(slot).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count, int slot) {
        int maxCount = itemStackHandler.getStackInSlot(slot).isEmpty() ? 64 : itemStackHandler.getStackInSlot(slot).getMaxStackSize();
        int currentCount = itemStackHandler.getStackInSlot(slot).getCount();

        return maxCount >= currentCount + count;
    }


    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        currentProgress = tag.getInt("potion_totems.infuser.currentProgress");
        maxProgress = tag.getInt("potion_totems.infuser.max_progress");
    }



    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("potion_totems.infuser.inventory", itemStackHandler.serializeNBT(registries));
        tag.putInt("potion_totems.infuser.currentProgress", currentProgress);
        tag.putInt("potion_totems.infuser.max_progress", maxProgress);

        super.saveAdditional(tag, registries);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("potion_totems.be.infuser.name");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new InfuserMenu(containerId, playerInventory, this, this.data);
    }
}
