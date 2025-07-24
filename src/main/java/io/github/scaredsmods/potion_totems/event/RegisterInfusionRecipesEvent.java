package io.github.scaredsmods.potion_totems.event;

import io.github.scaredsmods.potion_totems.item.TotemInfusion;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.ApiStatus;

public class RegisterInfusionRecipesEvent extends Event {
    private final TotemInfusion.Builder builder;
    private final RegistryAccess registryAccess;

    @ApiStatus.Internal
    public RegisterInfusionRecipesEvent(TotemInfusion.Builder builder, RegistryAccess registryAccess) {
        this.builder = builder;
        this.registryAccess = registryAccess;
    }

    public TotemInfusion.Builder getBuilder() {
        return builder;
    }

    public RegistryAccess getRegistryAccess() {
        return registryAccess;
    }
}
