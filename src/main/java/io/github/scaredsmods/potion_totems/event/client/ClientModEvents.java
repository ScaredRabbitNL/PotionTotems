package io.github.scaredsmods.potion_totems.event.client;

import io.github.scaredsmods.potion_totems.item.PotionTotemItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import static io.github.scaredsmods.potion_totems.item.PTItems.TOTEM_EFFECTS;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event) {
        TOTEM_EFFECTS.values().forEach(entry -> {
            event.register((stack, layer) -> {
                if (layer == 1) {
                    return PotionTotemItem.getColorForStack(stack);
                }
                return 0xFFFFFFFF;
            }, entry.get());
        });
    }

}