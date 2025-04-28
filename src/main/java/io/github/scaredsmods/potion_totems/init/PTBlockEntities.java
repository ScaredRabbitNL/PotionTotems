package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.block.entity.BaseInfuserBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PTBlockEntities {

    public static final ResourcefulRegistry<BlockEntityType<?>> TYPES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PotionTotems.MOD_ID);

    public static final RegistryEntry<BlockEntityType<BaseInfuserBlockEntity>> INFUSER_BE = TYPES.register("infuser" , () ->
            BlockEntityType.Builder.of(BaseInfuserBlockEntity::new, PTBlocks.INFUSER.get()).build(null));
}
