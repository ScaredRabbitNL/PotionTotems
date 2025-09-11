package io.github.scaredsmods.potion_totems.block;

import com.mojang.serialization.MapCodec;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.block.entity.InfuserBlockEntity;
import io.github.scaredsmods.potion_totems.init.PTBlockEntities;
import io.github.scaredsmods.potion_totems.lib.block.BaseHorizontalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class InfuserBlock extends BaseHorizontalBlock {

    public static final MapCodec<InfuserBlock> CODEC = simpleCodec(InfuserBlock::new);

    public static final VoxelShape SHAPE = Shapes.join(Stream.of(
            Block.box(0, 12, 0, 32, 16, 16),
            Block.box(28, 0, 0, 32, 12, 4),
            Block.box(28, 0, 12, 32, 12, 16),
            Block.box(0, 0, 12, 4, 12, 16),
            Block.box(0, 0, 0, 4, 12, 4),
            Block.box(2, 6, 4, 2, 12, 12),
            Block.box(30, 6, 4, 30, 12, 12),
            Stream.of(
            Block.box(13, 2.5, 4, 13, 4.5, 14),
            Block.box(6, 2.5, 4, 13, 2.5, 14),
            Block.box(7, 2.5, 5, 13, 4.5, 13),
            Block.box(6, 4.5, 4, 13, 4.5, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(13, 0.25, 4, 13, 2.25, 14),
            Block.box(6, 0.25, 4, 13, 0.25, 14),
            Block.box(7, 0.25, 5, 13, 2.25, 13),
            Block.box(6, 2.25, 4, 13, 2.25, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(13, 4.75, 4, 13, 6.75, 14),
            Block.box(6, 4.75, 4, 13, 4.75, 14),
            Block.box(7, 4.75, 5, 13, 6.75, 13),
            Block.box(6, 6.75, 4, 13, 6.75, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(4, 6, 2, 28, 12, 2),
            Block.box(4, 6, 14, 28, 12, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), Stream.of(
            Block.box(3, 16, 4, 11, 22, 12),
            Block.box(5, 22, 6, 9, 24, 10),
            Block.box(4, 24, 5, 10, 27, 11)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), BooleanOp.OR);

    public InfuserBlock(Properties properties) {
        super(properties, SHAPE);
        runCalculation(SHAPE);
    }


    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseHorizontalBlock> codec() {
        return CODEC;
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfuserBlockEntity(pos, state);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof InfuserBlockEntity infuserBlockEntity) {
                infuserBlockEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof InfuserBlockEntity baseInfuserBlockEntity) {
                player.openMenu(new SimpleMenuProvider(baseInfuserBlockEntity, Component.translatable(PotionTotemsMain.MOD_ID + ".gui.infuser.title")), pos);
            }else {
                throw new IllegalStateException("Missing container provider");
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()){
            return null;
        }
        return createTickerHelper(blockEntityType, PTBlockEntities.BE_INFUSER.get(), (level1, pos, state1, blockEntity) ->
                blockEntity.tick(level1, pos, state1));
    }



}
