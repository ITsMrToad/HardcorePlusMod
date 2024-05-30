package com.mr_toad.h_plus.common.entity.monster.variant;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

@MethodsReturnNonnullByDefault
public enum DesertSkeletonVariant implements StringRepresentable {

    DESERT(0, "desert"),
    BADLANDS(1, "badlands");

    private static final IntFunction<DesertSkeletonVariant> BY_ID = ByIdMap.continuous(DesertSkeletonVariant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    private final int id;
    private final String name;

    DesertSkeletonVariant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public static DesertSkeletonVariant byName(String name) {
        return StringRepresentable.fromEnum(DesertSkeletonVariant::values).byName(name, DESERT);
    }

    public static DesertSkeletonVariant byId(int id) {
        return BY_ID.apply(id);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
