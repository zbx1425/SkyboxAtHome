package cn.zbx1425.skyboxathome.client.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.function.Function;

public abstract class SkyboxRenderType extends RenderType {

    public static final VertexFormat SKYBOX_VERTEX_FORMAT;
    public static final VertexFormatElement ELEMENT_UV3D;

    public static ShaderInstance SKYBOX_SHADER;

    public static final Function<ResourceLocation, RenderType> SKYBOX_RENDER_TYPE;

    static {
        ELEMENT_UV3D = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.POSITION, 3);
        SKYBOX_VERTEX_FORMAT = new VertexFormat(ImmutableMap.of(
                "Position", DefaultVertexFormat.ELEMENT_POSITION,
                "UV3D", ELEMENT_UV3D
        ));

        SKYBOX_RENDER_TYPE = Util.memoize(resourceLocation -> {
            RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                    .setShaderState(SkyboxRenderStateShard.SKYBOX_SHADER_SHARD)
                    .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                    .createCompositeState(false);
            return RenderType.create("entity_cutout", SKYBOX_VERTEX_FORMAT, VertexFormat.Mode.QUADS, 1536, false, false, compositeState);
        });
    }

    public static void initShader(ResourceManager resourceManager) throws IOException {
        SKYBOX_SHADER = new ShaderInstance(resourceManager, "skyboxathome_skybox", SKYBOX_VERTEX_FORMAT);
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
