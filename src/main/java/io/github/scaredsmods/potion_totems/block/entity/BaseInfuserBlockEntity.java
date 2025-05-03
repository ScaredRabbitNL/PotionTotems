package io.github.scaredsmods.potion_totems.block.entity;

import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.init.PTBlockEntities;
import io.github.scaredsmods.potion_totems.init.PTItems;
import io.github.scaredsmods.potion_totems.screen.menu.InfuserMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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

public class BaseInfuserBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStackHandler itemStackHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(),getBlockState(), getBlockState(), 3);
            }
        }
    };
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int OUTPUT_SLOT_1 = 2;
    public static final int OUTPUT_SLOT_2 = 3;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    public BaseInfuserBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PTBlockEntities.INFUSER_BE.get(), blockPos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0  -> BaseInfuserBlockEntity.this.progress;
                    case 1 -> BaseInfuserBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0: BaseInfuserBlockEntity.this.progress = value;
                    case 1: BaseInfuserBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }


    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()){
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);
            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }


    private boolean hasCraftingFinished() {
        return this.progress >= maxProgress;
    }


    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {

        // Input Slot 1 -> Output Slot 2
        // Input Slot 2 -> Output Slot 1
        ItemStack in1 = itemStackHandler.getStackInSlot(INPUT_SLOT_1);
        ItemStack in2 = itemStackHandler.getStackInSlot(INPUT_SLOT_2);

        PotionContents contents = in2.get(DataComponents.POTION_CONTENTS);
        ItemStack output1 = new ItemStack(Items.GLASS_BOTTLE);
        ItemStack output2 = new ItemStack(PTItems.INFUSED_TOTEM.get());
        output2.set(DataComponents.POTION_CONTENTS, contents);
        return (in1.is(Items.TOTEM_OF_UNDYING) &&
                canInsertAmountIntoOutputSlot(output2.getCount(), OUTPUT_SLOT_2) && canInsertItemIntoOutputSlot(output2, OUTPUT_SLOT_2)) && 
                (in2.is(Items.POTION) && canInsertAmountIntoOutputSlot(output1.getCount(), OUTPUT_SLOT_1) && canInsertItemIntoOutputSlot(output1, OUTPUT_SLOT_1));
    }
    
    private void craftItem() {
        // Input Slot 1 -> Output Slot 2
        // Input Slot 2 -> Output Slot 1

        ItemStack in1 = itemStackHandler.getStackInSlot(INPUT_SLOT_1);
        ItemStack in2 = itemStackHandler.getStackInSlot(INPUT_SLOT_2);

        PotionContents contents = in2.get(DataComponents.POTION_CONTENTS);
        
        ItemStack output1 = new ItemStack(Items.GLASS_BOTTLE, 1);
        ItemStack output2 = new ItemStack(PTItems.INFUSED_TOTEM.get());
        output2.set(DataComponents.POTION_CONTENTS, contents);

        itemStackHandler.extractItem(INPUT_SLOT_1, 1, false);
        itemStackHandler.extractItem(INPUT_SLOT_2, 1, false);

        itemStackHandler.setStackInSlot(OUTPUT_SLOT_1, new ItemStack(output1.getItem(),
                itemStackHandler.getStackInSlot(OUTPUT_SLOT_1).getCount() + output1.getCount()));
        itemStackHandler.setStackInSlot(OUTPUT_SLOT_2, new ItemStack(output2.getItem(),
                itemStackHandler.getStackInSlot(OUTPUT_SLOT_2).getCount() + output2.getCount()));
        
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
    public Component getDisplayName() {
        return Component.translatable(PotionTotems.MOD_ID + ".block_entity.infuser.title");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new InfuserMenu(containerId, playerInventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemStackHandler.serializeNBT(registries));
        tag.putInt("infuser.progress", progress);
        tag.putInt("infuser.maxProgress", maxProgress);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("infuser.progress");
        maxProgress = tag.getInt("infuser.maxProgress");

    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
