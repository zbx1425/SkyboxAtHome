package cn.zbx1425.skyboxathome.network;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PacketUpdateHoldingItem {

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(SkyboxAtHome.MODID, "update_block_entity");

    public static class Client {

        public static void send(InteractionHand hand) {
            final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

            packet.writeInt(hand.ordinal());
            CompoundTag itemTag = new CompoundTag();
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.getMainHandItem().save(itemTag);
            packet.writeNbt(itemTag);

            ClientPlayNetworking.send(IDENTIFIER, packet);
        }
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf packet, PacketSender responseSender) {
        InteractionHand hand = InteractionHand.values()[packet.readInt()];
        CompoundTag itemTag = packet.readNbt();
        server.execute(() -> {
            player.setItemSlot(
                    hand == InteractionHand.MAIN_HAND
                            ? EquipmentSlot.MAINHAND
                            : EquipmentSlot.OFFHAND,
                    ItemStack.of(itemTag)
            );
        });
    }
}
