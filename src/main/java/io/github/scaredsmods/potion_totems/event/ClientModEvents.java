package io.github.scaredsmods.potion_totems.event;


import io.github.scaredsmods.potion_totems.init.PTItems;
import io.github.scaredsmods.potion_totems.init.PTMenuTypes;
import io.github.scaredsmods.potion_totems.screen.InfuserScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {


    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register(
                (p_329703_, p_329704_) -> p_329704_ > 0
                        ? -1
                        : FastColor.ARGB32.opaque(p_329703_.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor()),
                PTItems.INFUSED_TOTEM.get()
        );
    }




    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {

        event.register(PTMenuTypes.INFUSER_MENU.get(), InfuserScreen::new);
    }




}
