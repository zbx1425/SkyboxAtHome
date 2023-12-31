package cn.zbx1425.skyboxathome.block;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import cn.zbx1425.skyboxathome.item.SkyboxToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SkyboxBlock extends Block implements EntityBlock {

    public SkyboxBlock() {
        super(Properties.of().noOcclusion());
        registerDefaultState(getStateDefinition().any()
                .setValue(DIRECTIONAL, false)
                .setValue(BlockStateProperties.FACING, Direction.UP)
        );
    }

    public static final BooleanProperty DIRECTIONAL = BooleanProperty.create("directional");

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTIONAL).add(BlockStateProperties.FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SkyboxBlockEntity(blockPos, blockState);
    }

    @Override @SuppressWarnings("deprecation")
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(DIRECTIONAL)) return Shapes.empty();
        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }


    @Override @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (blockState.getValue(DIRECTIONAL)) return Shapes.empty();
        return super.getOcclusionShape(blockState, blockGetter, blockPos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (blockState.getValue(DIRECTIONAL)) return true;
        return super.propagatesSkylightDown(blockState, blockGetter, blockPos);
    }

    @Override @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getItemInHand(interactionHand).is(SkyboxAtHome.SKYBOX_TOOL_ITEM)) {
            if (level.isClientSide) return InteractionResult.SUCCESS;
            Optional<SkyboxBlockEntity> blockEntity = level.getBlockEntity(blockPos, SkyboxAtHome.SKYBOX_BLOCK_ENTITY);
            if (blockEntity.isPresent()) {
                String skyboxKey = SkyboxToolItem.getSkyboxKey(player.getItemInHand(interactionHand));
                if (!blockEntity.get().skyboxKey.equals(skyboxKey)) {
                    blockEntity.get().skyboxKey = skyboxKey;
                    blockEntity.get().setChanged();
                    ((ServerLevel) level).getChunkSource().blockChanged(blockPos);
                    return InteractionResult.SUCCESS;
                }
            }

            if (blockState.getValue(DIRECTIONAL)) {
                if (blockState.getValue(BlockStateProperties.FACING) == blockHitResult.getDirection()) {
                    level.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.FACING, blockHitResult.getDirection().getOpposite()));
                } else if (blockState.getValue(BlockStateProperties.FACING) == blockHitResult.getDirection().getOpposite()) {
                    level.setBlockAndUpdate(blockPos, blockState.setValue(DIRECTIONAL, false));
                } else {
                    level.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.FACING, blockHitResult.getDirection()));
                }
            } else {
                level.setBlockAndUpdate(blockPos, blockState
                        .setValue(DIRECTIONAL, true)
                        .setValue(BlockStateProperties.FACING, blockHitResult.getDirection())
                );
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}
