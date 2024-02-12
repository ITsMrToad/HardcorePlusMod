package com.mr_toad.h_plus.client.event;

import com.mr_toad.h_plus.core.HPlus;
import com.mr_toad.h_plus.core.init.HPItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.CreativeModeTabEvent;
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
       // } else if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
       //     event.getEntries().putAfter(Items.SNOWBALL.getDefaultInstance(), HPItems.SPIKY_SNOWBALL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

    }
}
