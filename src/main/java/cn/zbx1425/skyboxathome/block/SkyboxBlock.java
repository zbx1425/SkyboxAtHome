package cn.zbx1425.skyboxathome.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SkyboxBlock extends Block implements EntityBlock {

    public SkyboxBlock() {
        super(Properties.of());
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SkyboxBlockEntity(blockPos, blockState);
    }
}
