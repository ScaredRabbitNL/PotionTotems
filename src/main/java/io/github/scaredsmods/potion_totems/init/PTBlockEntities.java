package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.block.entity.AdvancedInfuserBlockEntity;
import io.github.scaredsmods.potion_totems.block.entity.InfuserBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PTBlockEntities {

    public static final ResourcefulRegistry<BlockEntityType<?>> TYPES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PotionTotemsMain.MOD_ID);




    public static final RegistryEntry<BlockEntityType<InfuserBlockEntity>> BE_INFUSER = TYPES.register("infuser" , () ->
            BlockEntityType.Builder.of(InfuserBlockEntity::new, PTBlocks.INFUSER.get()).build(null));

    public static final RegistryEntry<BlockEntityType<AdvancedInfuserBlockEntity>> BE_ADVANCED_INFUSER = TYPES.register("advanced_infuser", () ->
            BlockEntityType.Builder.of(AdvancedInfuserBlockEntity::new, PTBlocks.ADVANCED_INFUSER.get()).build(null));
}
