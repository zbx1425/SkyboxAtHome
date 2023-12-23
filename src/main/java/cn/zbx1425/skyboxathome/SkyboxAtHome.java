package cn.zbx1425.skyboxathome;

import cn.zbx1425.skyboxathome.block.SkyboxBlock;
import cn.zbx1425.skyboxathome.block.SkyboxBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyboxAtHome implements ModInitializer {

    public static final String MODID = "skybox_athome";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final Block SKYBOX_BLOCK = new SkyboxBlock();
    public static final BlockItem SKYBOX_BLOCKITEM = new BlockItem(SKYBOX_BLOCK, new Item.Properties());
    public static final BlockEntityType<SkyboxBlockEntity> SKYBOX_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(SkyboxBlockEntity::new, SKYBOX_BLOCK).build();

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.BLOCK,
                new ResourceLocation(MODID, "skybox"), SKYBOX_BLOCK);
        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(MODID, "skybox"), SKYBOX_BLOCKITEM);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(MODID, "skybox"), SKYBOX_BLOCK_ENTITY);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register(consumer -> consumer.accept(new ItemStack(SKYBOX_BLOCKITEM)));
    }
}
