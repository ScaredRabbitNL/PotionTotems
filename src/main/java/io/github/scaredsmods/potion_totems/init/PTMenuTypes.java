package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.screen.menu.AdvancedInfuserMenu;
import io.github.scaredsmods.potion_totems.screen.menu.InfuserMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;

public class PTMenuTypes {

    public static final ResourcefulRegistry<MenuType<?>> MENUS = ResourcefulRegistries.create(BuiltInRegistries.MENU, PotionTotemsMain.MOD_ID);
    public static final RegistryEntry<MenuType<InfuserMenu>> INFUSER_MENU = registerMenuType("infuser_menu" , InfuserMenu::new);
    public static final RegistryEntry<MenuType<AdvancedInfuserMenu>> ADVANCED_INFUSER_MENU = registerMenuType("advanced_infuser_menu", AdvancedInfuserMenu::new);


    private static <T extends AbstractContainerMenu> RegistryEntry<MenuType<T>> registerMenuType(String  name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
