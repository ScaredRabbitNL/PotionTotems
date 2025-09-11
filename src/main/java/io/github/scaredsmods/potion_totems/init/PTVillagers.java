package io.github.scaredsmods.potion_totems.init;

import com.google.common.collect.ImmutableSet;
import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;

public class PTVillagers {



    public static final ResourcefulRegistry<PoiType> POI_TYPES = ResourcefulRegistries.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, PotionTotemsMain.MOD_ID);
    public static final ResourcefulRegistry<VillagerProfession> VILLAGER_PROFESSIONS = ResourcefulRegistries.create(BuiltInRegistries.VILLAGER_PROFESSION, PotionTotemsMain.MOD_ID);



    public static final HolderRegistryEntry<PoiType> TOTEM_MASTER_POI = POI_TYPES.registerHolder("totem_master_poi", () ->
            new PoiType(ImmutableSet.copyOf(PTBlocks.INFUSER.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final RegistryEntry<VillagerProfession> TOTEM_MASTER = VILLAGER_PROFESSIONS.register("totem_master", () ->
            new VillagerProfession("totem_master", holder -> holder.value() == TOTEM_MASTER_POI.holder().value(),
                    poiTypeHolder -> poiTypeHolder.value() == TOTEM_MASTER_POI.holder().value(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_CLERIC));






}
