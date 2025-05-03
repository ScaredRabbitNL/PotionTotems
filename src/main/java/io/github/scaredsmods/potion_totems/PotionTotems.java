package io.github.scaredsmods.potion_totems;


import com.mojang.logging.LogUtils;
import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.potion_totems.init.PTBlockEntities;
import io.github.scaredsmods.potion_totems.init.PTBlocks;
import io.github.scaredsmods.potion_totems.init.PTItems;
import io.github.scaredsmods.potion_totems.init.PTMenuTypes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;


@Mod(PotionTotems.MOD_ID)
public class PotionTotems {

    public static final String MOD_ID = "potion_totems";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PotionTotems(IEventBus bus, ModContainer container) {


        PTItems.ITEMS.init();
        PTItems.TABS.init();
        PTBlocks.BLOCKS.init();
        PTBlockEntities.TYPES.init();
        PTMenuTypes.MENUS.init();


        container.registerConfig(ModConfig.Type.COMMON, PTConfig.SPEC);
    }


    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
