package io.github.scaredsmods.potion_totems.event;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.init.PTItems;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = PotionTotemsMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEvents {


    @SubscribeEvent
    public static void resetNames(RegisterEvent e) {
        Registry<?> registry = e.getRegistry();
        if (registry == BuiltInRegistries.ITEM){
            if (registry.containsKey(PTItems.INFUSED_TOTEM.getId())) {
                ItemStack infused_totem = new ItemStack(PTItems.INFUSED_TOTEM.get());
                PotionContents contents = infused_totem.get(DataComponents.POTION_CONTENTS);
                if (contents !=null){
                    contents.forEachEffect(effectInstance -> {
                        ResourceLocation loc = BuiltInRegistries.MOB_EFFECT.getKey(effectInstance.getEffect().value());
                        assert loc != null;
                        String namespace = loc.getNamespace();
                        if (!namespace.equals( "minecraft") && !namespace.equals("potion_totems")) {
                            infused_totem.set(DataComponents.ITEM_NAME, Component.literal("Infused Totem"));
                        }
                    });
                }
            }
        }
    }
}
