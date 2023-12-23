package cn.zbx1425.skyboxathome.client.render;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import cn.zbx1425.skyboxathome.block.SkyboxBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SkyboxBlockEntityRenderer implements BlockEntityRenderer<SkyboxBlockEntity> {

    public SkyboxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(SkyboxBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource buffers, int i1, int i2) {
        VertexConsumer vertices = buffers.getBuffer(
                SkyboxRenderType.SKYBOX_RENDER_TYPE.apply(blockEntity.texture != null
                        ? blockEntity.texture
                        : new ResourceLocation(SkyboxAtHome.MODID, "textures/skybox/test.png")
                )
        );
        Matrix4f pose = poseStack.last().pose();
        pose.translate(0.5f, 0.5f, 0.5f);

        BlockPos blockPos = blockEntity.getBlockPos();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        for (int i = 0; i < SKYBOX_VERTICES.length; i += 3) {
            vertices.vertex(pose, SKYBOX_VERTICES[i], SKYBOX_VERTICES[i + 1], SKYBOX_VERTICES[i + 2])
                    .vertex(
                            blockPos.getX() + 0.5f + SKYBOX_VERTICES[i] - cameraPos.x(),
                            blockPos.getY() + 0.5f + SKYBOX_VERTICES[i + 1] - cameraPos.y(),
                            blockPos.getZ() + 0.5f + SKYBOX_VERTICES[i + 2] - cameraPos.z()
                    )
                    .endVertex();
        }
    }

    private static final float[] SKYBOX_VERTICES = {
        0.5f, 0.5f, -0.5f,
        -0.5f, 0.5f, -0.5f,
        -0.5f, 0.5f,  0.5f,
        0.5f, 0.5f,  0.5f,
    
        0.5f, -0.5f,  0.5f,
        -0.5f, -0.5f,  0.5f,
        -0.5f, -0.5f, -0.5f,
        0.5f, -0.5f, -0.5f,
    
        0.5f,  0.5f, 0.5f,
        -0.5f,  0.5f, 0.5f,
        -0.5f, -0.5f, 0.5f,
        0.5f, -0.5f, 0.5f,
    
        0.5f, -0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,
        -0.5f,  0.5f, -0.5f,
        0.5f,  0.5f, -0.5f,
    
        -0.5f,  0.5f,  0.5f,
        -0.5f,  0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,
        -0.5f, -0.5f,  0.5f,
    
        0.5f,  0.5f, -0.5f,
        0.5f,  0.5f,  0.5f,
        0.5f, -0.5f,  0.5f,
        0.5f, -0.5f, -0.5f,
    };
}
