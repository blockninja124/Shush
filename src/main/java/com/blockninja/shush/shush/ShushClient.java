package com.blockninja.shush.shush;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Shush.MODID, dist = Dist.CLIENT)
public class ShushClient {
    public ShushClient(ModContainer modContainer) {
        // All of this just for no "changes need restart", damn
        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (container, modListScreen) -> new ConfigurationScreen(
                        container,
                        modListScreen,
                        (parent, type, modConfig, title) ->
                                new ConfigurationScreen.ConfigurationSectionScreen(
                                        parent,
                                        null,
                                        modConfig,
                                        title
                                )
                )
        );    }
}