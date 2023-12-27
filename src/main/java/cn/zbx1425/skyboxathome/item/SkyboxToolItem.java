package cn.zbx1425.skyboxathome.item;

import cn.zbx1425.skyboxathome.client.gui.SkyboxScreenFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SkyboxToolItem extends Item {

    public SkyboxToolItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (player.isShiftKeyDown()) {
            if (level.isClientSide()) {
                Minecraft.getInstance().setScreen(SkyboxScreenFactory.create(interactionHand, Minecraft.getInstance().screen));
            }
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        }
        return super.use(level, player, interactionHand);
    }

    public static String getSkyboxKey(ItemStack itemStack) {
        CompoundTag toolTag = itemStack.getOrCreateTagElement("skybox_tool");
        return toolTag.contains("skybox_key") ? toolTag.getString("skybox_key") : "";
    }

    public static void setSkyboxKey(ItemStack itemStack, String skyboxKey) {
        CompoundTag toolTag = itemStack.getOrCreateTagElement("skybox_tool");
        toolTag.putString("skybox_key", skyboxKey);
    }
}
