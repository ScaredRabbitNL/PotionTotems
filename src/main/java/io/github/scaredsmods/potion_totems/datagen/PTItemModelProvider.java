package io.github.scaredsmods.potion_totems.datagen;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.init.PTItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PTItemModelProvider extends ItemModelProvider {

    public PTItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PotionTotemsMain.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItemWithParent(PTItems.INFUSED_TOTEM.get(), PotionTotemsMain.id("item/totem_base"));
    }

    public ItemModelBuilder basicItemWithParent(Item item, ResourceLocation parentId) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        String itemName = itemId.getPath();
        return withExistingParent(itemName, parentId);
    }
}
