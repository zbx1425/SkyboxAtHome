package cn.zbx1425.skyboxathome.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // I can't figure out how to accordingly account for the bobbing effect's contribution
    // to the camera's translation, and with that the skybox would distort in a funny way
    // So I just disabled it for now
    @Redirect(method = "bobView", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    void redirectBobViewTranslate(PoseStack instance, float f, float g, float h) {

    }
}
