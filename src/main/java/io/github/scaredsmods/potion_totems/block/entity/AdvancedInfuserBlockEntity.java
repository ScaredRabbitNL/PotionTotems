package io.github.scaredsmods.potion_totems.block.entity;

import io.github.scaredsmods.potion_totems.init.PTBlockEntities;
import io.github.scaredsmods.potion_totems.init.PTItems;
import io.github.scaredsmods.potion_totems.lib.block.entity.BaseInfuserBlockEntity;
import io.github.scaredsmods.potion_totems.screen.menu.AdvancedInfuserMenu;
import io.github.scaredsmods.potion_totems.util.PotionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AdvancedInfuserBlockEntity extends BaseInfuserBlockEntity {

    public final ItemStackHandler stackHandler = new ItemStackHandler(4) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!(level == null) && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };


    //Infused Totem input  -> Infused totem output
    //Potion input -> Glass bottle output
    private static final int INFUSED_TOTEM_INPUT_SLOT = 0;
    private static final int POTION_INPUT_SLOT = 1;
    private static final int INFUSED_TOTEM_OUTPUT_SLOT = 2;
    private static final int BOTTLE_OUTPUT_SLOT = 3;
    private int maxProgress = 72;
    private int currentProgress = 0;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {

            return switch (index) {
                case 0 -> AdvancedInfuserBlockEntity.this.currentProgress;
                case 1 -> AdvancedInfuserBlockEntity.this.maxProgress;
                default -> 2;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index){
                case 0: AdvancedInfuserBlockEntity.this.currentProgress = value;
                case 1: AdvancedInfuserBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

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

    public AdvancedInfuserBlockEntity(BlockPos pos, BlockState blockState) {
        super(PTBlockEntities.BE_ADVANCED_INFUSER.get(), pos, blockState, Component.translatable("potion_totems.be.advanced_infuser.name"));
    }

    private void craftItem() {
        ItemStack in1 = stackHandler.getStackInSlot(INFUSED_TOTEM_INPUT_SLOT);     // TOTEM
        ItemStack in2 = stackHandler.getStackInSlot(POTION_INPUT_SLOT);    // POTION



        ItemStack output1 = new ItemStack(PTItems.INFUSED_TOTEM.get(), 1);
        ItemStack output2 = new ItemStack(Items.GLASS_BOTTLE, 1);

        PotionContents contents = in1.get(DataComponents.POTION_CONTENTS);

        if (contents.hasEffects()) {
            PotionUtils.copyPotionContents(output1, in1, in2);
        } else {
            PotionUtils.copyPotionContents(in2, output1);
        }

        stackHandler.extractItem(INFUSED_TOTEM_INPUT_SLOT, 1, false);
        stackHandler.extractItem(POTION_INPUT_SLOT, 1, false);


        ItemStack currentOutput1 = stackHandler.getStackInSlot(INFUSED_TOTEM_OUTPUT_SLOT);
        if (currentOutput1.isEmpty()) {
            stackHandler.setStackInSlot(INFUSED_TOTEM_OUTPUT_SLOT, output1);
        } else {

            currentOutput1.grow(1);
        }

        ItemStack currentOutput2 = stackHandler.getStackInSlot(BOTTLE_OUTPUT_SLOT);
        if (currentOutput2.isEmpty()) {
            stackHandler.setStackInSlot(BOTTLE_OUTPUT_SLOT, output2);
        } else {
            currentOutput2.grow(1);
        }
    }


    private  boolean hasCraftingFinished() {
        return this.currentProgress >= this.maxProgress;
    }

    private  void increaseCraftingProgress() {
        currentProgress++;
    }

    private void resetProgress() {
        currentProgress = 0;
        maxProgress = 72;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(stackHandler.getSlots());
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            inventory.setItem(i, stackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    private boolean hasRecipe() {
        ItemStack in1 = stackHandler.getStackInSlot(INFUSED_TOTEM_INPUT_SLOT);
        ItemStack in2 = stackHandler.getStackInSlot(POTION_INPUT_SLOT);

        ItemStack output2 = new ItemStack(Items.GLASS_BOTTLE);
        ItemStack output1 = new ItemStack(PTItems.INFUSED_TOTEM.get());

        return (in1.is(PTItems.INFUSED_TOTEM.get()) && canInsertIntoSlot(output1, INFUSED_TOTEM_OUTPUT_SLOT, output1.getCount(), stackHandler))
                &&
                (in2.is(Items.POTION) && canInsertIntoSlot(output2, BOTTLE_OUTPUT_SLOT, output2.getCount(), stackHandler));
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AdvancedInfuserMenu(containerId, playerInventory, this, this.data);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        stackHandler.deserializeNBT(registries, tag.getCompound("potion_totems.advanced_infuser.inventory"));
        currentProgress = tag.getInt("potion_totems.advanced_infuser.currentProgress");
        maxProgress = tag.getInt("potion_totems.advanced_infuser.max_progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("potion_totems.advanced_infuser.inventory", stackHandler.serializeNBT(registries));
        tag.putInt("potion_totems.advanced_infuser.currentProgress", currentProgress);
        tag.putInt("potion_totems.advanced_infuser.max_progress", maxProgress);

        super.saveAdditional(tag, registries);
    }
}
