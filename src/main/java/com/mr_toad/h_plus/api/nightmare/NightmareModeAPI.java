package com.mr_toad.h_plus.api.nightmare;

import com.mr_toad.h_plus.core.HPlus;

public class NightmareModeAPI {

    public final boolean nightmare;

    NightmareModeAPI(boolean nightmare) {
        this.nightmare = nightmare;
    }

    public static NightmareModeAPI create(boolean nightmare) {
        HPlus.LOGGER.info("Nightmare mod is successfully created");
        return new NightmareModeAPI(nightmare);
    }

    public boolean isNightmare() {
        return this.nightmare;
    }



}
