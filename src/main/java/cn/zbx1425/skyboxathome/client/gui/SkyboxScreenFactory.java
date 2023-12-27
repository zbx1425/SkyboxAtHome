package cn.zbx1425.skyboxathome.client.gui;

import cn.zbx1425.skyboxathome.block.SkyboxBlockEntity;
import cn.zbx1425.skyboxathome.client.data.SkyboxProperty;
import cn.zbx1425.skyboxathome.client.data.SkyboxRegistry;
import cn.zbx1425.skyboxathome.network.PacketUpdateBlockEntity;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Map;

public class SkyboxScreenFactory {

    public static Screen create(SkyboxBlockEntity blockEntity, Screen parentScreen) {
        ConfigCategory.Builder imageCategory = ConfigCategory.createBuilder()
                        .name(Component.translatable("gui.skybox_athome.image"));

        Map<String, ButtonOption> imageButtons = new java.util.HashMap<>();
        Option<String> imageKeyOption = Option.<String>createBuilder()
                .name(Component.translatable("gui.skybox_athome.image"))
                .instant(true)
                .binding("", () -> blockEntity.skyboxKey, newValue -> blockEntity.skyboxKey = newValue)
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
                .save(() -> PacketUpdateBlockEntity.Client.send(blockEntity))
                .build()
                .generateScreen(parentScreen);
    }
}
