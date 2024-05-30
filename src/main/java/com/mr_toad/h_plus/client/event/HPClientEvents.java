package com.mr_toad.h_plus.client.event;

import com.mr_toad.h_plus.client.screen.HardcoreDeathScreen;
import com.mr_toad.h_plus.common.util.HardcoreLevelHandler;
import com.mr_toad.h_plus.core.HPlus;
import com.mr_toad.h_plus.core.init.HPItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HPClientEvents {

    @SubscribeEvent
    public static void addToTab(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.SPAWN_EGGS) {
            event.getEntries().putAfter(Items.ZOMBIE_SPAWN_EGG.getDefaultInstance(), HPItems.MARTYR_SPAWN_EGG.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(Items.ZOMBIE_SPAWN_EGG.getDefaultInstance(), HPItems.PUTRID_SPAWN_EGG.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(Items.SKELETON_SPAWN_EGG.getDefaultInstance(), HPItems.POLYGONUM_SPAWN_EGG.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(Items.SKELETON_SPAWN_EGG.getDefaultInstance(), HPItems.BONY_SANDSTONE_SPAWN_EGG.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        } else if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.getEntries().putAfter(Items.SNOWBALL.getDefaultInstance(), HPItems.SPIKY_SNOWBALL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

    }

    @Mod.EventBusSubscriber(modid = HPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class BusForge {

        public static final HardcoreDeathScreen HARDCORE_DEATH_SCREEN = new HardcoreDeathScreen();

        @SubscribeEvent
        public static void onLevelLoad(LevelEvent.Load event) {
            HardcoreLevelHandler.HANDLER.reload();
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onScreenOpen(ScreenEvent.Opening event) {
            Screen screen = event.getScreen();
            Minecraft minecraft = Minecraft.getInstance();
            if (screen instanceof DeathScreen deathScreen) {
                LocalPlayer player = minecraft.player;
                if (player != null && HardcoreLevelHandler.HANDLER.level != null) {
                    HARDCORE_DEATH_SCREEN.causeOfDeath = deathScreen.causeOfDeath;
                    event.setNewScreen(HARDCORE_DEATH_SCREEN);
                }
            } else if (screen instanceof TitleScreen && HardcoreLevelHandler.HANDLER.level != null) {
                HardcoreLevelHandler.HANDLER.deleteWorld(minecraft);
            }
        }
    }
}
