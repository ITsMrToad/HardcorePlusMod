package com.mr_toad.h_plus.api;

public enum BonusOp {
    SUM,
    DIFFERENCE,
    MULTIPLY,
    DIVISION,
    POW,
    REPLACE;


    public int getModifiedOrig(int o, int b, BonusOp op) {
        switch (op) {
            case SUM -> o += b;
            case DIFFERENCE -> o -= b;
            case MULTIPLY -> o *= b;
            case DIVISION -> o /= b;
            case POW -> o = (int) Math.pow(o, b);
            case REPLACE -> o = b;
        }
        return o;
    }


}
