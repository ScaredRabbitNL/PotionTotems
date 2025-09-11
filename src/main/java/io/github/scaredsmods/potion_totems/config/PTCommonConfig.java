package io.github.scaredsmods.potion_totems.config;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.entry.EntryFlag;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PTCommonConfig extends Config {

    public PTCommonConfig() {
        super(PotionTotemsMain.id("potion_totems-common"));
    }


    public InfusedTotemSection infusedTotemSection = new InfusedTotemSection();
    public static class InfusedTotemSection extends ConfigSection {

        public InfusedTotemSection() {
            super();
        }

        public ValidatedInt duration = new ValidatedInt(400, 5000, 20, ValidatedNumber.WidgetType.SLIDER);
        public ValidatedInt amplifier = new ValidatedInt(0, 4, 0, ValidatedNumber.WidgetType.SLIDER);
    }


    @Override
    public @NotNull FileType fileType() {
        return FileType.JSON5;
    }
}
