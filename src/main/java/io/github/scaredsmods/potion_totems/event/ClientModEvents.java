package io.github.scaredsmods.potion_totems.event;


import io.github.scaredsmods.potion_totems.init.PTMenuTypes;
import io.github.scaredsmods.potion_totems.init.PTRegistries;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotemItem;
import io.github.scaredsmods.potion_totems.screen.InfuserScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    /*

    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event) {
        BuiltInRegistries.MOB_EFFECT.forEach(entry -> {
            event.register((stack, layer) -> {
                if (layer == 1) {
                    return PotionTotemItem.getColorForStack(stack);
                }
                return 0xFFFFFFFF;
            });
        });
    }
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {

        event.register(PTMenuTypes.INFUSER_MENU.get(), InfuserScreen::new);
    }

     */


    @SubscribeEvent // on the mod event bus
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(PTRegistries.R_POTION_TOTEM);
    }

}
