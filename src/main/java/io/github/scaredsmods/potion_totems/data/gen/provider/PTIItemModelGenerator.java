package io.github.scaredsmods.potion_totems.data.gen.provider;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.init.PTItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PTIItemModelGenerator extends ItemModelProvider {


    public PTIItemModelGenerator(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, PotionTotemsMain.MOD_ID, existingFileHelper);
    }

    @Override
    public  void registerModels() {




        basicItemWithParent(PTItems.INFUSED_TOTEM.get(), PotionTotemsMain.id("item/totem_base"));
        /*

        PTItems.ITEMS.stream().map(RegistryEntry::get).forEach(item -> {

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            String itemName = id.getPath();


            if (itemName.equals("totem_base") || itemName.equals("infuser")) return;

            withExistingParent(itemName, ResourceLocation.fromNamespaceAndPath(PotionTotemsMain.MOD_ID,"item/totem_base"));
        });

         */

    }


    public ItemModelBuilder basicItemWithParent(Item item, ResourceLocation parentId) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        String itemName = itemId.getPath();
        return withExistingParent(itemName, parentId);
    }
    public ItemModelBuilder basicItem(Item item) {
        return basicItem(item);
    }

    public ItemModelBuilder basicItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }
}
