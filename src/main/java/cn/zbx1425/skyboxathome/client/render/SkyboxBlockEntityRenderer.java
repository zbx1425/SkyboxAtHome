package cn.zbx1425.skyboxathome.client.render;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import cn.zbx1425.skyboxathome.block.SkyboxBlock;
import cn.zbx1425.skyboxathome.block.SkyboxBlockEntity;
import cn.zbx1425.skyboxathome.client.data.SkyboxProperty;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SkyboxBlockEntityRenderer implements BlockEntityRenderer<SkyboxBlockEntity> {

    public SkyboxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(SkyboxBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource buffers, int i1, int i2) {
        SkyboxProperty property = blockEntity.getProperty();
        VertexConsumer vertices = buffers.getBuffer(
                property.projectionType == SkyboxProperty.ProjectionType.Equirectangular
                        ? SkyboxRenderType.SKYBOX_RENDER_TYPE.apply(property.texture)
                        : RenderType.entitySolid(property.texture)
        );
        Matrix4f pose = poseStack.last().pose();
        pose.translate(0.5f, 0.5f, 0.5f);

        BlockPos blockPos = blockEntity.getBlockPos();
        boolean directional = blockEntity.getBlockState().getValue(SkyboxBlock.DIRECTIONAL);
        Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);

        if (property.projectionType == SkyboxProperty.ProjectionType.Equirectangular) {
            for (int i = 0; i < SKYBOX_VERTICES.length; i += 3) {
                if (directional && SKYBOX_QUADS_FACINGS[i / 12] != facing) continue;
                Vector3f offset = new Vector3f(
                        (float) (blockPos.getX() + 0.5 + SKYBOX_VERTICES[i] - CameraState.cameraPos.x()),
                        (float) (blockPos.getY() + 0.5 + SKYBOX_VERTICES[i + 1] - CameraState.cameraPos.y()),
                        (float) (blockPos.getZ() + 0.5 + SKYBOX_VERTICES[i + 2] - CameraState.cameraPos.z())
                );
                vertices.vertex(pose, SKYBOX_VERTICES[i], SKYBOX_VERTICES[i + 1], SKYBOX_VERTICES[i + 2])
                        .vertex(offset.x(), offset.y(), offset.z())
                        .endVertex();
            }
        } else {
            for (int i = 0; i < SKYBOX_VERTICES.length; i += 3) {
                if (directional && SKYBOX_QUADS_FACINGS[i / 12] != facing) continue;
                vertices.vertex(pose, SKYBOX_VERTICES[i], SKYBOX_VERTICES[i + 1], SKYBOX_VERTICES[i + 2])
                        .color(255, 255, 255, 255)
                        .uv(QUAD_UV_COORDS[(i % 4) * 2], QUAD_UV_COORDS[(i % 4) * 2 + 1])
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(LightTexture.FULL_BRIGHT)
                        .normal(poseStack.last().normal(), 0, 1, 0)
                        .endVertex();
            }
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

    private static final Direction[] SKYBOX_QUADS_FACINGS = {
        Direction.UP,
        Direction.DOWN,
        Direction.SOUTH,
        Direction.NORTH,
        Direction.WEST,
        Direction.EAST
    };

    private static final float[] QUAD_UV_COORDS = {
        0, 1,
        0, 0,
        1, 0,
        1, 1
    };
}
