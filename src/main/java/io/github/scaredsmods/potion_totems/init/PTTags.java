package io.github.scaredsmods.potion_totems.init;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotem;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PTTags {

    public static class Blocks {



        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(PotionTotemsMain.id(name));
        }

    }

    public static class Items {

        public static final TagKey<Item> TOTEMS = createTag("totems");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(PotionTotemsMain.id(name));
        }


    }

    public static class Misc {
        public static final TagKey<PotionTotem> POTION_TOTEMS = createTag("potion_totems");

        private static TagKey<PotionTotem> createTag(String name) {
            return TagKey.create(PTResourceKeys.RK_R_POTION_TOTEM, PotionTotemsMain.id(name));
        }

    }


}
