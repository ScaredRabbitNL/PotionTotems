package io.github.scaredsmods.potion_totems.datagen;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.init.PTBlocks;
import io.github.scaredsmods.potion_totems.init.PTVillagers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PTEnglishLanguageProvider extends LanguageProvider {

    public PTEnglishLanguageProvider(PackOutput output) {
        super(output, PotionTotemsMain.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        BuiltInRegistries.POTION.holders().forEach(holder -> {
            ResourceLocation id = BuiltInRegistries.POTION.getKey(holder.value());
            String effectName = id.getPath();

            String formattedName = Arrays.stream(effectName.split("_"))
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "));

            add("item.potion_totems.infused_totem.effect." + effectName, "Infused Totem of " + formattedName);
        });

        add("item.potion_totems.infused_totem.effect.empty", "Infused Totem");
        add("item.potion_totems.infused_totem.effect.custom", "Infused Totem");
        add("itemGroup.potion_totems.totems", "Potion Totems");
        add(PTBlocks.INFUSER.get(), "Infuser");
        add(PTBlocks.ADVANCED_INFUSER.get(), "Advanced Infuser");
        add("potion_totems.be.infuser.name", "Totem Infuser");
        add("potion_totems.gui.infuser.title", "Totem Infuser");
        add("potion_totems.be.advanced_infuser.name", "Advanced Totem Infuser");
        add("potion_totems.gui.advanced_infuser.title", "Advanced Totem Infuser");
        add("entity.minecraft.villager.potion_totems.totem_master", "Totem Master");
    }
}
