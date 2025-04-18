package io.github.scaredsmods.potion_totems.event.client;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.items.PotionTotemItem;
import io.github.scaredsmods.potion_totems.registry.PTItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        PTItems.ITEMS.stream().map(RegistryEntry::get).forEach(item -> {
            event.register(PotionTotemItem::getColor,item);
        });
        event.register(PotionTotemItem::getColor, PTItems.BASE_TOTEM.get());

    }


}