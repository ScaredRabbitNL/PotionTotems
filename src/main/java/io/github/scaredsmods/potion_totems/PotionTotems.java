package io.github.scaredsmods.potion_totems;

import com.mojang.logging.LogUtils;
import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.potion_totems.registry.PTItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(PotionTotems.MOD_ID)
public class PotionTotems {

    public static final String MOD_ID = "potion_totems";
    public static final Logger LOGGER = LogUtils.getLogger();



    public PotionTotems(IEventBus modEventBus, ModContainer modContainer) {



        PTItems.register();
        PTItems.ITEMS.init();
        PTItems.TABS.init();
        PTItems.B_ITEMS.init();


        //NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, PTConfig.SPEC);
    }

    public static ResourceLocation id(String key) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }


    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }




}
