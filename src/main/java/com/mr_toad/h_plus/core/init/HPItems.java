package com.mr_toad.h_plus.core.init;

import com.mr_toad.h_plus.common.item.custom.SpikyFrostedSnowballItem;
import com.mr_toad.h_plus.core.HPlus;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HPItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HPlus.MODID);

    public static final RegistryObject<Item> MARTYR_SPAWN_EGG = ITEMS.register("martyr_spawn_egg", () -> new ForgeSpawnEggItem(HPEntityType.FROSTED_ZOMBIE, 44975, 14543594, new Item.Properties()));
    public static final RegistryObject<Item> PUTRID_SPAWN_EGG = ITEMS.register("putrid_spawn_egg", () -> new ForgeSpawnEggItem(HPEntityType.JUNGLE_ZOMBIE, 44975, 9945732, new Item.Properties()));
    public static final RegistryObject<Item> POLYGONUM_SPAWN_EGG = ITEMS.register("polygonum_spawn_egg", () -> new ForgeSpawnEggItem(HPEntityType.JUNGLE_SKELETON, 15198183, 8306542, new Item.Properties()));
    public static final RegistryObject<Item> BONY_SANDSTONE_SPAWN_EGG = ITEMS.register("bony_sandstone_spawn_egg", () -> new ForgeSpawnEggItem(HPEntityType.DESERT_SKELETON, 15198183, 16380836, new Item.Properties()));

    public static final RegistryObject<Item> SPIKY_SNOWBALL = ITEMS.register("spiky_snowball", () -> new SpikyFrostedSnowballItem(new Item.Properties().stacksTo(16)));

}
