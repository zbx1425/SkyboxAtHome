package cn.zbx1425.skyboxathome.client.data;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SkyboxProperty {

    public final Component name;
    public final Component description;

    public final ResourceLocation texture;
    public final ProjectionType projectionType;

    public SkyboxProperty(JsonObject jsonObject) {
        this.name = Component.translatable(jsonObject.get("name").getAsString());
        this.description = jsonObject.has("description")
                ? Component.translatable(jsonObject.get("description").getAsString())
                : Component.literal("");
        this.texture = new ResourceLocation(jsonObject.get("texture").getAsString());
        this.projectionType = ProjectionType.Equirectangular;
    }

    public SkyboxProperty(ResourceLocation texture) {
        this.name = Component.literal("");
        this.description = Component.literal("");
        this.texture = texture;
        this.projectionType = ProjectionType.Flat;
    }

    public enum ProjectionType {
        Equirectangular,
        Flat
    }
}
