package cn.zbx1425.skyboxathome.block;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import cn.zbx1425.skyboxathome.client.data.SkyboxProperty;
import cn.zbx1425.skyboxathome.client.data.SkyboxRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkyboxBlockEntity extends BlockEntity {

    public String skyboxKey;

    public SkyboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SkyboxAtHome.SKYBOX_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        skyboxKey = compoundTag.contains("skyboxKey") ? compoundTag.getString("skyboxKey") : null;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (skyboxKey != null) compoundTag.putString("skyboxKey", skyboxKey);
    }

    public SkyboxProperty getProperty() {
        return SkyboxRegistry.getOrDefault(skyboxKey);
    }

    @Nullable @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return super.saveWithoutMetadata();
    }
}
