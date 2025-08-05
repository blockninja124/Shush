package com.blockninja.shush.shush;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class Config {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> MATCHED_LOGS =
            BUILDER.comment("Regex patterns to remove from logs")
            .defineListAllowEmpty("matchedLogs", List.of(), () -> "\\w+", Config::validateString);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static Pattern regexPattern;

    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }
}
