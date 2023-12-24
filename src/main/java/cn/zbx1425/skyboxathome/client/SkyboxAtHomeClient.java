package cn.zbx1425.skyboxathome.client;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import cn.zbx1425.skyboxathome.client.data.SkyboxRegistry;
import cn.zbx1425.skyboxathome.client.render.CameraState;
import cn.zbx1425.skyboxathome.client.render.SkyboxBlockEntityRenderer;
import cn.zbx1425.skyboxathome.client.render.SkyboxRenderType;
import cn.zbx1425.skyboxathome.network.PacketSkyboxScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class SkyboxAtHomeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(SkyboxAtHome.SKYBOX_BLOCK_ENTITY, SkyboxBlockEntityRenderer::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(SkyboxAtHome.MODID, "skybox_shader");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                try {
                    SkyboxRenderType.initShader(resourceManager);
                } catch (Exception ex) {
                    SkyboxAtHome.LOGGER.error("Failed to reload skybox shader", ex);
                }
                SkyboxRegistry.loadResources(resourceManager);
            }
        });

        WorldRenderEvents.AFTER_SETUP.register(context -> {
            CameraState.acquire(context);
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketSkyboxScreen.IDENTIFIER, PacketSkyboxScreen.Client::handle);
    }


}
