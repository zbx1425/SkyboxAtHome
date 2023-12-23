package cn.zbx1425.skyboxathome.block;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkyboxBlockEntity extends BlockEntity {

    public ResourceLocation texture;

    public SkyboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SkyboxAtHome.SKYBOX_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        texture = compoundTag.contains("texture")
            ? new ResourceLocation(compoundTag.getString("texture"))
            : null;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        if (texture != null) compoundTag.putString("texture", texture.toString());
    }
}
