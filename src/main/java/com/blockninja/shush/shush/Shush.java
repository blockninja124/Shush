package com.blockninja.shush.shush;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Mod(Shush.MODID)
public class Shush {
    public static final String MODID = "shush";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static PrintStream originalOut;

    public Shush(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onConfigLoad);

        LOGGER.info("Adding custom log appender");

        registerConsoleAppender();

        LOGGER.info("Finished adding appender");

        LOGGER.info("Adding custom System.out");

        registerSystemOutStream();

        LOGGER.info("Finished adding custom System.out");

        modContainer.registerConfig(ModConfig.Type.STARTUP, Config.SPEC);
    }

    private void registerConsoleAppender() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();

        Map<String, Appender> originalAppenders = new HashMap<>(config.getAppenders());

        for (String name : originalAppenders.keySet()) {
            config.getRootLogger().removeAppender(name);
        }

        PatternLayout layout = null;

        // Very sussy
        for (Appender appender : originalAppenders.values()) {
            Layout<?> tempLayout = appender.getLayout();
            if (tempLayout instanceof PatternLayout patternLayout) {
                layout = patternLayout;
            }
        }

        ConditionalConsoleAppender appender = new ConditionalConsoleAppender(
                "ConditionalWrapper", null, layout, originalAppenders
        );
        appender.start();

        config.addAppender(appender);
        config.getRootLogger().addAppender(appender, null, null);

        context.updateLoggers();
    }

    private void registerSystemOutStream() {
        originalOut = System.out;

        PrintStream customOut = new PrintStream(new LineInterceptingOutputStream(System.out), true);

        System.setOut(customOut);
    }

    public static boolean shouldSuppress(String message) {
        if (Config.regexPattern == null) {
            return false;
        }

        Matcher matcher = Config.regexPattern.matcher(message);
        return matcher.find();
    }

    public void onConfigLoad(final ModConfigEvent event) {

        // Make sure we don't accidentally cache the previous regex data
        Config.regexPattern = null;


        if (Config.MATCHED_LOGS.get().isEmpty()) {
            LOGGER.info("No logs set to intercept");
            return;
        }

        StringBuilder fullRegex = new StringBuilder("(");
        boolean firstTime = true;
        for (String item : Config.MATCHED_LOGS.get()) {
            try {
                // Check for errors, return value ignored
                Pattern.compile(item);

                if (!firstTime) {
                    fullRegex.append("|(");
                }
                firstTime = false;

                fullRegex.append(item).append(")");
            } catch (PatternSyntaxException e) {
                LOGGER.warn("Warning: invalid regex '"+item+"', ignoring. "+e.getMessage());
            }
        }

        String lastChar = fullRegex.substring(fullRegex.toString().length()-1);
        if (lastChar.equals("(")) {
            // Might happen if there is exactly 1 item, and its invalid regex
            return;
        }

        Config.regexPattern = Pattern.compile(fullRegex.toString());

        LOGGER.info("Ready to intercept logs");
    }
}
