package cn.zbx1425.skyboxathome.client.render;

import cn.zbx1425.skyboxathome.mixin.GameRendererAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import org.joml.Matrix4f;

public class CameraState {

    public static void acquire(WorldRenderContext context) {
        double fov = ((GameRendererAccessor) context.gameRenderer())
                .invokeGetFov(context.camera(), context.tickDelta(), true);
        Matrix4f projection = new Matrix4f(context.gameRenderer().getProjectionMatrix(fov));
        projection.invert();
        SkyboxRenderType.setIBoblessProjMat(projection);
    }


}
