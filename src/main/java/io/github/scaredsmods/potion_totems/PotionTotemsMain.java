package io.github.scaredsmods.potion_totems;


import com.mojang.logging.LogUtils;
import io.github.scaredsmods.potion_totems.init.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;


@Mod(PotionTotemsMain.MOD_ID)
public class PotionTotemsMain {

    public static final String MOD_ID = "potion_totems";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PotionTotemsMain(IEventBus bus, ModContainer container) {


        PTItems.ITEMS.init();
        PTItems.TABS.init();
        PTBlocks.BLOCKS.init();
        PTBlockEntities.TYPES.init();
        PTMenuTypes.MENUS.init();
        PTPotions.POTIONS.init();
        PTVillagers.VILLAGER_PROFESSIONS.init();
        PTVillagers.POI_TYPES.init();
        PTConfigs.init();
    }


    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }


}
