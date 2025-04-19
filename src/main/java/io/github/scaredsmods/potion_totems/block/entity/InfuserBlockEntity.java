package io.github.scaredsmods.potion_totems.block.entity;

import io.github.scaredsmods.potion_totems.recipe.InfuserRecipe;
import io.github.scaredsmods.potion_totems.recipe.InfuserRecipeInput;
import io.github.scaredsmods.potion_totems.recipe.PTRecipes;
import io.github.scaredsmods.potion_totems.screen.InfuserMenu;
import io.github.scaredsmods.rabbilib.api.blockentity.RabbiCraftingBlockEntity;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeHolder;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.windows.INPUT;

import java.util.Optional;

public class InfuserBlockEntity extends RabbiCraftingBlockEntity implements MenuProvider {

    public final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int OUTPUT_SLOT_1 = 2;
    private static final int OUTPUT_SLOT_2 = 3;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    public InfuserBlockEntity( BlockPos blockPos, BlockState blockState) {
        super(PTBlockEntities.INFUSER_BE.get(), blockPos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> InfuserBlockEntity.this.progress;
                    case 1 -> InfuserBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0:
                        InfuserBlockEntity.this.progress = value;
                    case 1:
                        InfuserBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

        };
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("block.potion_totems.infuser");
    }


    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new InfuserMenu(i, inventory, this, this.data);
    }
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("potion_totems.infuser.progress", progress);
        pTag.putInt("potion_totems.infuser.max_progress", maxProgress);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("potion_totems.infuser.progress");
        maxProgress = pTag.getInt("potion_totems.infuser.max_progress");
    }


    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        super.tick(level, blockPos, blockState);
    }

    @Override
    public void craftItem() {

        Optional<RabbiRecipeHolder<InfuserRecipe>> recipe = getCurrentRecipe();
        ItemStack output1 = recipe.get().value().output1();
        ItemStack output2 = recipe.get().value().output2();

        itemHandler.extractItem(INPUT_SLOT_1, 1, false);
        itemHandler.extractItem(INPUT_SLOT_2, 1, false);
        itemHandler.setStackInSlot(OUTPUT_SLOT_1, new ItemStack(output1.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT_1).getCount() + output1.getCount()));
        itemHandler.setStackInSlot(OUTPUT_SLOT_2, new ItemStack(output2.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT_2).getCount() + output2.getCount()));
    }

    @Override
    public void resetProgress() {
        progress = 0;
        maxProgress = 72;
    }

    @Override
    public boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    @Override
    public void increaseCraftingProgress() {
        progress++;
    }

    @Override
    public boolean hasRecipe() {
        Optional<RabbiRecipeHolder<InfuserRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output1 = recipe.get().value().output1();
        ItemStack output2 = recipe.get().value().output2();

        return (canInsertAmountIntoOutputSlot(output1.getCount()) && canInsertAmountIntoOutputSlot(output2.getCount()) && canInsertItemIntoOutputSlot(output1,output2));
    }


    public Optional<RabbiRecipeHolder<InfuserRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager().getRecipeFor(PTRecipes.INFUSER_RECIPE_TYPE.get(), new InfuserRecipeInput(itemHandler.getStackInSlot(INPUT_SLOT_1), itemHandler.getStackInSlot(INPUT_SLOT_2)), level);
    }

    @Override
    public boolean canInsertItemIntoOutputSlot(ItemStack output1, ItemStack output2) {
        return (itemHandler.getStackInSlot(OUTPUT_SLOT_1).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT_1).getItem() == output1.getItem());
    }

    @Override
    public boolean canInsertAmountIntoOutputSlot(int i) {
        int maxCount = (itemHandler.getStackInSlot(OUTPUT_SLOT_1).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT_1).getMaxStackSize())
                | (itemHandler.getStackInSlot(OUTPUT_SLOT_2).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT_2).getMaxStackSize());
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT_1).getCount() | itemHandler.getStackInSlot(OUTPUT_SLOT_2).getCount();

        return maxCount >= currentCount + i;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
