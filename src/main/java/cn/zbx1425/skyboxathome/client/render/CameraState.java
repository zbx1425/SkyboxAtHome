package cn.zbx1425.skyboxathome.client.render;

import cn.zbx1425.skyboxathome.mixin.GameRendererAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class CameraState {

    // public static Matrix4f bobMatrix;
    public static Vec3 cameraPos;

    public static void acquire(WorldRenderContext context) {
        /*
        double fov = ((GameRendererAccessor) context.gameRenderer())
                .invokeGetFov(context.camera(), context.tickDelta(), true);
        Matrix4f projection = new Matrix4f(context.gameRenderer().getProjectionMatrix(fov));
        Matrix4f projectionWithBob = RenderSystem.getProjectionMatrix();
        projection.invert().mul(projectionWithBob);
        bobMatrix = projection;
         */
        cameraPos = context.camera().getPosition();
    }


}
