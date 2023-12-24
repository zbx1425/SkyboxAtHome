package cn.zbx1425.skyboxathome.block;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import cn.zbx1425.skyboxathome.client.data.SkyboxProperty;
import cn.zbx1425.skyboxathome.client.data.SkyboxRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkyboxBlockEntity extends BlockEntity {

    public String skyboxKey;

    public SkyboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SkyboxAtHome.SKYBOX_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        skyboxKey = compoundTag.contains("skyboxKey") ? compoundTag.getString("skyboxKey") : null;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        if (skyboxKey != null) compoundTag.putString("skyboxKey", skyboxKey);
    }

    public SkyboxProperty getProperty() {
        return SkyboxRegistry.getOrDefault(skyboxKey);
    }
}
