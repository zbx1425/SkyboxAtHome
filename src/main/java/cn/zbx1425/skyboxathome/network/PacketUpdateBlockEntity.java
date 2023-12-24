package cn.zbx1425.skyboxathome.network;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PacketUpdateBlockEntity {

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(SkyboxAtHome.MODID, "update_block_entity");

    public static class Client {

        public static void send(BlockEntity blockEntity) {
            Level level = blockEntity.getLevel();
            if (level == null) return;

            final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
            packet.writeResourceLocation(level.dimension().location());
            packet.writeBlockPos(blockEntity.getBlockPos());
            packet.writeId(net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE, blockEntity.getType());

            CompoundTag tag = blockEntity.saveWithoutMetadata();
            packet.writeNbt(tag);

            ClientPlayNetworking.send(IDENTIFIER, packet);
        }
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf packet, PacketSender responseSender) {
        ResourceKey<Level> levelKey = packet.readResourceKey(net.minecraft.core.registries.Registries.DIMENSION);
        BlockPos blockPos = packet.readBlockPos();
        BlockEntityType<?> blockEntityType = packet.readById(net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE);
        if (blockEntityType != SkyboxAtHome.SKYBOX_BLOCK_ENTITY) return;
        CompoundTag compoundTag = packet.readNbt();

        server.execute(() -> {
            ServerLevel level = server.getLevel(levelKey);
            if (level == null || blockEntityType == null) return;
            level.getBlockEntity(blockPos, blockEntityType).ifPresent(blockEntity -> {
                if (compoundTag != null) {
                    blockEntity.load(compoundTag);
                    blockEntity.setChanged();
                    level.getChunkSource().blockChanged(blockPos);
                }
            });
        });
    }
}
