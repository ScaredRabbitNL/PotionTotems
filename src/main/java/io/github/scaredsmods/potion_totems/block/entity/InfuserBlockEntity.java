package io.github.scaredsmods.potion_totems.block.entity;

import io.github.scaredsmods.potion_totems.init.PTBlockEntities;
import io.github.scaredsmods.potion_totems.init.PTItems;
import io.github.scaredsmods.potion_totems.screen.menu.InfuserMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

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
                default -> 2;
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
        ItemStack in1 = itemStackHandler.getStackInSlot(TOTEM_INPUT_SLOT);     // TOTEM
        ItemStack in2 = itemStackHandler.getStackInSlot(POTION_INPUT_SLOT);    // POTION

        ItemStack output1 = new ItemStack(PTItems.INFUSED_TOTEM.get());
        ItemStack output2 = new ItemStack(Items.GLASS_BOTTLE, 1);


        copyPotionContents(in2, output1);


        itemStackHandler.extractItem(TOTEM_INPUT_SLOT, 1, false);
        itemStackHandler.extractItem(POTION_INPUT_SLOT, 1, false);


        ItemStack currentOutput1 = itemStackHandler.getStackInSlot(INFUSED_TOTEM_OUTPUT_SLOT);
        if (currentOutput1.isEmpty()) {
            itemStackHandler.setStackInSlot(INFUSED_TOTEM_OUTPUT_SLOT, output1);
        } else {

            currentOutput1.grow(1);
        }

        ItemStack currentOutput2 = itemStackHandler.getStackInSlot(BOTTLE_OUTPUT_SLOT);
        if (currentOutput2.isEmpty()) {
            itemStackHandler.setStackInSlot(BOTTLE_OUTPUT_SLOT, output2);
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

    private boolean hasRecipe() {
        ItemStack in1 = itemStackHandler.getStackInSlot(TOTEM_INPUT_SLOT);
        ItemStack in2 = itemStackHandler.getStackInSlot(POTION_INPUT_SLOT);

        ItemStack output2 = new ItemStack(Items.GLASS_BOTTLE);
        ItemStack output1 = new ItemStack(PTItems.INFUSED_TOTEM.get());

        return (in1.is(Items.TOTEM_OF_UNDYING) && canInsertIntoSlot(output1, INFUSED_TOTEM_OUTPUT_SLOT, output1.getCount()))
                &&
                (in2.is(Items.POTION) && canInsertIntoSlot(output2, BOTTLE_OUTPUT_SLOT, output2.getCount()));
    }


    public static void copyPotionContents(ItemStack source, ItemStack target) {
        if (source.has(DataComponents.POTION_CONTENTS)) {
            PotionContents contents = source.get(DataComponents.POTION_CONTENTS);
            target.set(DataComponents.POTION_CONTENTS, contents);
            if (source.has(DataComponents.CUSTOM_NAME)) {
                target.set(DataComponents.CUSTOM_NAME, source.get(DataComponents.CUSTOM_NAME));
            }
        }
    }
    private boolean canInsertIntoSlot (ItemStack output, int slot, int count) {
        return canInsertItemIntoOutputSlot(output, slot) && canInsertAmountIntoOutputSlot(count, slot);
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
