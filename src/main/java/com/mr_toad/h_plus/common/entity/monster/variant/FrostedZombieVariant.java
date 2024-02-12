package com.mr_toad.h_plus.common.entity.monster.variant;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public enum FrostedZombieVariant implements StringRepresentable {

    VAR_A(0, "var_a"),
    VAR_B(1, "var_b");

    public static final StringRepresentable.EnumCodec<FrostedZombieVariant> CODEC = StringRepresentable.fromEnum(FrostedZombieVariant::values);

    private static final IntFunction<FrostedZombieVariant> BY_ID = ByIdMap.continuous(FrostedZombieVariant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    private final int id;
    private final String name;

    FrostedZombieVariant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public static FrostedZombieVariant byName(String name) {
        return CODEC.byName(name, VAR_A);
    }

    public static FrostedZombieVariant byId(int id) {
        return BY_ID.apply(id);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}