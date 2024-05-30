package com.mr_toad.h_plus.core.init;

import com.mr_toad.h_plus.core.HPlus;
import com.mr_toad.lib.api.util.ToadOtherUtils;
import com.mr_toad.lib.mtjava.floats.OptionalFloat;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HPSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HPlus.MODID);

    public static final RegistryObject<SoundEvent> FROSTED_ZOMBIE_SHOOT = ToadOtherUtils.registerSounds("martyr_shoot", HPlus.MODID, SOUNDS, OptionalFloat.empty());

}
