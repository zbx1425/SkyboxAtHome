package cn.zbx1425.skyboxathome.network;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import cn.zbx1425.skyboxathome.block.SkyboxBlockEntity;
import cn.zbx1425.skyboxathome.client.gui.SkyboxScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class PacketSkyboxScreen {

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(SkyboxAtHome.MODID, "skybox_screen");

    public static void send(ServerPlayer target, BlockPos blockPos) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeBlockPos(blockPos);
        ServerPlayNetworking.send(target, IDENTIFIER, buffer);
    }

    public static class Client {

        public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buffer, PacketSender responseSender) {
            BlockPos blockPos = buffer.readBlockPos();
            if (client.level == null) return;
            Optional<SkyboxBlockEntity> blockEntity = client.level.getBlockEntity(blockPos, SkyboxAtHome.SKYBOX_BLOCK_ENTITY);
            if (blockEntity.isEmpty()) return;

            client.execute(() -> client.setScreen(new SkyboxScreen(blockEntity.get())));
        }
    }
}
