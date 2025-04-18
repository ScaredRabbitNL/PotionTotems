package io.github.scaredsmods.potion_totems.datagen.client;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.registry.PTItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Locale;

public class PTEnLanguageGenerator extends LanguageProvider {

    public PTEnLanguageGenerator(PackOutput output) {
        super(output, PotionTotems.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        PTItems.ITEMS.stream().map(RegistryEntry::get).forEach(item -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            String itemName = id.getPath();
            String[] parts = itemName.split("_");

            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    String formatted = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
                    result.append(formatted).append(" ");
                }
            }
            add(item, result.toString().trim());

        });
    }
}
