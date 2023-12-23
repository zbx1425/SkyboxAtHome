package cn.zbx1425.skyboxathome.client;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import cn.zbx1425.skyboxathome.client.render.SkyboxBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class SkyboxAtHomeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(SkyboxAtHome.SKYBOX_BLOCK_ENTITY, SkyboxBlockEntityRenderer::new);
    }
}
