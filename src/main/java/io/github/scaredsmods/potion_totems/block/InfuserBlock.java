package io.github.scaredsmods.potion_totems.block;

import com.mojang.serialization.MapCodec;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.block.entity.InfuserBlockEntity;
import io.github.scaredsmods.potion_totems.init.PTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class InfuserBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final MapCodec<InfuserBlock> CODEC = simpleCodec(InfuserBlock::new);

    public static final EnumProperty<InfuserBlockModelType> MODEL_TYPE = EnumProperty.create("model", InfuserBlockModelType.class);


    public InfuserBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(MODEL_TYPE, InfuserBlockModelType.MAIN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODEL_TYPE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfuserBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return state.getValue(MODEL_TYPE) == InfuserBlockModelType.MAIN ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }
    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.isClientSide) {
            super.playerWillDestroy(level, pos, state, player);
            return state;
        }

        InfuserBlockModelType infuserModel = state.getValue(MODEL_TYPE);
        if (infuserModel == InfuserBlockModelType.MAIN) {
            BlockPos otherpos = pos.relative(state.getValue(FACING).getClockWise());
            BlockState otherstate = level.getBlockState(otherpos);
            if (otherstate.getBlock() == this) {
                level.setBlock(otherpos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, otherpos, Block.getId(otherstate));
            }
        }
        if (infuserModel == InfuserBlockModelType.SIDE) {
            BlockPos otherpos = pos.relative(state.getValue(FACING).getCounterClockWise());
            BlockState otherstate = level.getBlockState(otherpos);
            if (otherstate.getBlock() == this) {
                level.setBlock(otherpos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, otherpos, Block.getId(otherstate));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos blockpos = pos.relative(state.getValue(FACING).getClockWise());
            level.setBlock(blockpos, state.setValue(MODEL_TYPE, InfuserBlockModelType.SIDE), Block.UPDATE_ALL);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, Block.UPDATE_ALL);
        }
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

    @Deprecated
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING).getClockWise())).canBeReplaced();
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

    public enum InfuserBlockModelType implements StringRepresentable {
        MAIN, SIDE;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return getSerializedName();
        }
    }
}
