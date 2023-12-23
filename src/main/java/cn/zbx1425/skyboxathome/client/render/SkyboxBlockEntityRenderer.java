package cn.zbx1425.skyboxathome.client.render;

import cn.zbx1425.skyboxathome.block.SkyboxBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class SkyboxBlockEntityRenderer implements BlockEntityRenderer<SkyboxBlockEntity> {

    public SkyboxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        
    }

    @Override
    public void render(SkyboxBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {

    }
}
