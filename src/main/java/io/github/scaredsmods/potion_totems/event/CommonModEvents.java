package io.github.scaredsmods.potion_totems.event;

import io.github.scaredsmods.potion_totems.init.PTRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class CommonModEvents {


    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(PTRegistries.TOTEM_REGISTRY);
    }



}
