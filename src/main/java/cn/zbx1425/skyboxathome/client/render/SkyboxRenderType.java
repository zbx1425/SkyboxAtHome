package cn.zbx1425.skyboxathome.client.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.function.Function;

public abstract class SkyboxRenderType extends RenderType {

    public static ShaderInstance SKYBOX_SHADER;
    public static Uniform I_BOBLESS_PROJ_MAT;

    public static final Function<ResourceLocation, RenderType> SKYBOX_RENDER_TYPE;

    static {

        SKYBOX_RENDER_TYPE = Util.memoize(resourceLocation -> {
            RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                    .setShaderState(SkyboxRenderStateShard.SKYBOX_SHADER_SHARD)
                    .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                    .createCompositeState(false);
            return RenderType.create("entity_cutout", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 1536, false, false, compositeState);
        });
    }

    public static void initShader(ResourceManager resourceManager) throws IOException {
        SKYBOX_SHADER = new ShaderInstance(resourceManager, "skyboxathome_skybox", DefaultVertexFormat.POSITION);
        I_BOBLESS_PROJ_MAT = SKYBOX_SHADER.getUniform("IBoblessProjMat");
    }

    public static void setIBoblessProjMat(Matrix4f matrix4f) {
        I_BOBLESS_PROJ_MAT.set(matrix4f);
    }

    private static abstract class SkyboxRenderStateShard extends RenderStateShard {

        public static ShaderStateShard SKYBOX_SHADER_SHARD = new ShaderStateShard(() -> SKYBOX_SHADER);

        public SkyboxRenderStateShard(String string, Runnable runnable, Runnable runnable2) {
            super(string, runnable, runnable2);
        }
    }

    public SkyboxRenderType(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }
}
