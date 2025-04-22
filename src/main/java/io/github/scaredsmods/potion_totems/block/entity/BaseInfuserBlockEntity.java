package io.github.scaredsmods.potion_totems.block.entity;

import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.item.PotionTotemItem;
import io.github.scaredsmods.potion_totems.recipe.InfuserRecipe;
import io.github.scaredsmods.potion_totems.recipe.InfuserRecipeInput;
import io.github.scaredsmods.potion_totems.registry.PTBlockEntities;
import io.github.scaredsmods.potion_totems.registry.PTItems;
import io.github.scaredsmods.potion_totems.registry.PTRecipes;
import io.github.scaredsmods.potion_totems.screen.menu.InfuserMenu;
import io.github.scaredsmods.rabbilib.api.blockentity.RabbiCraftingBlockEntity;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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


    public void craftItem() {
        Optional<RabbiRecipeHolder<InfuserRecipe>> recipe = getCurrentRecipe();
        ItemStack output1 = recipe.get().value().output();
        ItemStack output2 = new ItemStack(Items.GLASS_BOTTLE, 1);

        itemStackHandler.extractItem(INPUT_SLOT_1, 1, false);
        itemStackHandler.extractItem(INPUT_SLOT_2, 1, false);

        itemStackHandler.setStackInSlot(OUTPUT_SLOT_1, new ItemStack(output1.getItem(), itemStackHandler.getStackInSlot(OUTPUT_SLOT_1).getCount() + output1.getCount()));
        itemStackHandler.setStackInSlot(OUTPUT_SLOT_2, new ItemStack(output2.getItem(), itemStackHandler.getStackInSlot(OUTPUT_SLOT_2).getCount() + output2.getCount()));

    }

    public void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }


    public boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }


    public void increaseCraftingProgress() {
        progress++;
    }


    protected boolean hasRecipe() {
        Optional<RabbiRecipeHolder<InfuserRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }


        ItemStack output1 = recipe.get().value().output();
        ItemStack output2 = new ItemStack(Items.GLASS_BOTTLE, 1);

        return canInsertAmountIntoOutputSlot(OUTPUT_SLOT_1, output1.getCount()) && canInsertAmountIntoOutputSlot(OUTPUT_SLOT_2, output2.getCount()) && canInsertItemIntoOutputSlot(output1, output2);
    }

    private Optional<RabbiRecipeHolder<InfuserRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager().getRecipeFor(PTRecipes.INFUSER_TYPE.get(), new InfuserRecipeInput(itemStackHandler.getStackInSlot(INPUT_SLOT_1), itemStackHandler.getStackInSlot(INPUT_SLOT_2)), level);
    }



    public boolean canInsertItemIntoOutputSlot(ItemStack output1, ItemStack output2) {
        return itemStackHandler.getStackInSlot(OUTPUT_SLOT_1).isEmpty() || (itemStackHandler.getStackInSlot(OUTPUT_SLOT_1).getItem() == output1.getItem() &&
                itemStackHandler.getStackInSlot(OUTPUT_SLOT_2).getItem() == output2.getItem());
    }


    public boolean canInsertAmountIntoOutputSlot(int count, int slot) {
        int maxCount = itemStackHandler.getStackInSlot(slot).isEmpty() ? 64: itemStackHandler.getStackInSlot(slot).getMaxStackSize();
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
