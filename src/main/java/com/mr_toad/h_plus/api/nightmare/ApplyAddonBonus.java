package com.mr_toad.h_plus.api.nightmare;

import com.mr_toad.h_plus.api.BonusOp;

public class ApplyAddonBonus {

    public int orig;
    public final int bonus;
    public final BonusOp operation;

    public ApplyAddonBonus(int orig, int bonus, BonusOp operation) {
        this.orig = orig;
        this.bonus = bonus;
        this.operation = operation;
    }

    public void apply() {
        this.orig = this.getOperation().getModifiedOrig(this.getOrig(), this.getBonus(), this.getOperation());
    }

    public int getOrig() {
        return this.orig;
    }

    public int getBonus() {
        return this.bonus;
    }

    public BonusOp getOperation() {
        return this.operation;
    }


}
