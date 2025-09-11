package io.github.scaredsmods.potion_totems.init;

import io.github.scaredsmods.potion_totems.config.PTCommonConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public class PTConfigs {

    public static PTCommonConfig ptCommonConfig = ConfigApiJava.registerAndLoadConfig(PTCommonConfig::new, RegisterType.BOTH);

    public static void init() {}
}
