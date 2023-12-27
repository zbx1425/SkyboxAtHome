package cn.zbx1425.skyboxathome.client.gui;

import cn.zbx1425.skyboxathome.client.data.SkyboxProperty;
import cn.zbx1425.skyboxathome.client.data.SkyboxRegistry;
import cn.zbx1425.skyboxathome.item.SkyboxToolItem;
import cn.zbx1425.skyboxathome.network.PacketUpdateHoldingItem;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class SkyboxScreenFactory {

    public static Screen create(InteractionHand hand, Screen parentScreen) {
        assert Minecraft.getInstance().player != null;
        ItemStack itemStack = Minecraft.getInstance().player.getItemInHand(hand);

        ConfigCategory.Builder imageCategory = ConfigCategory.createBuilder()
                        .name(Component.translatable("gui.skybox_athome.image"));

        Map<String, ButtonOption> imageButtons = new java.util.HashMap<>();
        Option<String> imageKeyOption = Option.<String>createBuilder()
                .name(Component.translatable("gui.skybox_athome.image"))
                .instant(true)
                .binding("", () -> SkyboxToolItem.getSkyboxKey(itemStack),
                        newValue -> {
                            SkyboxToolItem.setSkyboxKey(itemStack, newValue);
                            PacketUpdateHoldingItem.Client.send(hand);
                        })
                .listener((option, value) -> {
                    for (Map.Entry<String, ButtonOption> entry1 : imageButtons.entrySet()) {
                        entry1.getValue().setAvailable(!entry1.getKey().equals(value));
                    }
                })
                .controller(StringControllerBuilder::create)
                .build();
        imageCategory.option(imageKeyOption);

        for (Map.Entry<String, SkyboxProperty> entry : SkyboxRegistry.ELEMENTS.entrySet()) {
            ButtonOption imageBtn = ButtonOption.createBuilder()
                    .name(entry.getValue().name)
                    .text(Component.empty())
                    .description(OptionDescription.createBuilder()
                            .image(entry.getValue().texture, 2, 1)
                            .text(entry.getValue().description)
                            .build())
                    .available(!entry.getKey().equals(imageKeyOption.pendingValue()))
                    .action((screen, btn) -> {
                        imageKeyOption.requestSet(entry.getKey());
                    })
                    .build();
            imageCategory.option(imageBtn);
            imageButtons.put(entry.getKey(), imageBtn);
        }

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Skybox Settings"))
                .category(imageCategory.build())
                .build()
                .generateScreen(parentScreen);
    }
}
