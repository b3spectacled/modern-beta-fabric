package com.bespectacled.modernbeta.config;

import java.util.Arrays;
import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "compat_config")
public class ConfigCompat implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public List<String> biomesWithCustomSurfaces = Arrays.asList();
}
