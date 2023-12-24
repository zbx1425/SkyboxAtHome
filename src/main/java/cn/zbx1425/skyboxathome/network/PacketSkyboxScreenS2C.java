package cn.zbx1425.skyboxathome.network;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PacketSkyboxScreenS2C {

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(SkyboxAtHome.MODID, "skybox_screen");

    public static void send(BlockPos blockPos) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeBlockPos(blockPos);
        ClientPlayNetworking.send(IDENTIFIER, buffer);
    }

    public static class Client {

        public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buffer, PacketSender responseSender) {
            BlockPos blockPos = buffer.readBlockPos();
        }
    }
}
