package io.github.scaredsmods.potion_totems.event.client;

import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.items.PotionTotemItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import static io.github.scaredsmods.potion_totems.registry.PTItems.TOTEM_EFFECTS;

@EventBusSubscriber(modid = PotionTotems.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        TOTEM_EFFECTS.values().forEach(itemReg -> {
            Item item = itemReg.get();
            if (item instanceof PotionTotemItem totem) {
                event.getItemColors().register((stack, layer) -> {
                    return layer == 0 ? totem.getColor() : 0xFFFFFFFF;
                }, totem);
            }
        });
    }
}