package com.blockninja.shush.shush;

import java.util.List;
import java.util.regex.Pattern;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MATCHED_LOGS =
            BUILDER.comment("Regex patterns to remove from logs")
            .defineListAllowEmpty("matchedLogs", List.of(), Config::validateString);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Pattern regexPattern;

    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }
}
