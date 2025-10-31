package com.smilezyd.trimmability.util;

import com.mojang.serialization.Codec;
import com.smilezyd.trimmability.Trimmability;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static final ComponentType<String> CUSTOM_HELMET_TRIM = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Trimmability.MOD_ID, "custom_helmet_trim"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static void registerModComponents() {
        Trimmability.LOGGER.info("Registered custom data components");
    }
}
