package cn.zbx1425.skyboxathome.client.data;

import cn.zbx1425.skyboxathome.SkyboxAtHome;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SkyboxRegistry {

    public static final Map<String, SkyboxProperty> ELEMENTS = new HashMap<>();

    private static final SkyboxProperty DEFAULT_SKYBOX = new SkyboxProperty(
            new ResourceLocation(SkyboxAtHome.MODID, "textures/block/skybox.png"));

    public static void register(String key, SkyboxProperty property) {
        ELEMENTS.put(key, property);
    }

    public static SkyboxProperty getOrDefault(String key) {
        return ELEMENTS.getOrDefault(key, DEFAULT_SKYBOX);
    }

    public static void loadResources(ResourceManager manager) {
        ELEMENTS.clear();
        List<Pair<ResourceLocation, Resource>> resources = listResources(manager, "skybox_athome", "skyboxes", ".json");
        for (Pair<ResourceLocation, Resource> pair : resources) {
            try {
                try (InputStream is = pair.getSecond().open()) {
                    JsonObject rootObj = JsonParser.parseString(IOUtils.toString(is, StandardCharsets.UTF_8)).getAsJsonObject();
                    if (rootObj.has("texture")) {
                        String key = FilenameUtils.getBaseName(pair.getFirst().getPath()).toLowerCase(Locale.ROOT);
                        register(key, new SkyboxProperty(rootObj));
                    } else {
                        for (Map.Entry<String, JsonElement> entry : rootObj.entrySet()) {
                            JsonObject obj = entry.getValue().getAsJsonObject();
                            String key = entry.getKey().toLowerCase(Locale.ROOT);
                            register(key, new SkyboxProperty(obj));
                        }
                    }
                }
            } catch (Exception ex) {
                SkyboxAtHome.LOGGER.error("Failed loading rail: " + pair.getFirst().toString(), ex);
            }
        }

    }


    private static List<Pair<ResourceLocation, Resource>> listResources(ResourceManager resourceManager, String namespace, String path, String extension) {

        return resourceManager.listResourceStacks(path,
                        rl -> rl.getNamespace().equals(namespace) && rl.getPath().endsWith(extension))
                .entrySet().stream().flatMap(e -> e.getValue().stream().map(r -> new Pair<>(e.getKey(), r))).toList();
    }
}
