package cn.zbx1425.skyboxathome.client.gui;

import cn.zbx1425.skyboxathome.block.SkyboxBlockEntity;
import cn.zbx1425.skyboxathome.client.data.SkyboxProperty;
import cn.zbx1425.skyboxathome.client.data.SkyboxRegistry;
import cn.zbx1425.skyboxathome.network.PacketUpdateBlockEntity;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import java.util.Objects;
import java.util.function.BiConsumer;

public class SkyboxScreen extends CottonClientScreen {
    public SkyboxScreen(SkyboxBlockEntity blockEntity) {
        super(new SkyboxGui(blockEntity));
    }

    public static class SkyboxGui extends LightweightGuiDescription {

        private final SkyboxBlockEntity editingBlockEntity;

        private final WSprite imageView;
        private final WLabel descriptionView;
        private final WListPanel<String, WButton> listView;

        public SkyboxGui(SkyboxBlockEntity blockEntity) {
            this.editingBlockEntity = blockEntity;

            WGridPanel root = new WGridPanel();
            setRootPanel(root);
            root.setSize(330, 240);
            root.setInsets(Insets.ROOT_PANEL);

            SkyboxProperty prop = SkyboxRegistry.getOrDefault(editingBlockEntity.skyboxKey);

            imageView = new WSprite(prop.texture);
            root.add(imageView, 9, 0, 8, 4);

            descriptionView = new WLabel(prop.description);
            root.add(descriptionView, 9, 4, 8, 8);

            BiConsumer<String, WButton> configurator = (String key, WButton btn) -> {
                SkyboxProperty btnProp = SkyboxRegistry.getOrDefault(key);
                btn.setLabel(btnProp.name);
                btn.setEnabled(!Objects.equals(key, editingBlockEntity.skyboxKey));
                btn.setOnClick(() -> {
                    setActiveItem(key, btn);
                    editingBlockEntity.skyboxKey = key;
                    PacketUpdateBlockEntity.Client.send(editingBlockEntity);
                });
            };
            listView = new WListPanel<>(
                    SkyboxRegistry.ELEMENTS.keySet().stream().toList(),
                    WButton::new, configurator
            );
            root.add(listView, 0, 0, 8, 12);

            root.validate(this);
        }

        private void setActiveItem(String key, WButton inactiveButton) {
            SkyboxProperty prop = SkyboxRegistry.getOrDefault(key);
            imageView.setImage(prop.texture);
            descriptionView.setText(prop.description);
            listView.streamChildren().forEach(wWidget -> {
                if (wWidget instanceof WButton btn) {
                    btn.setEnabled(btn != inactiveButton);
                }
            });
        }
    }
}
