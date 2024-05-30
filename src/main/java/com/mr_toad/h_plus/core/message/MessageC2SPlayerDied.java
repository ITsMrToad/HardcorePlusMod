package com.mr_toad.h_plus.core.message;

import com.mr_toad.h_plus.common.util.HardcoreLevelHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageC2SPlayerDied {

    public MessageC2SPlayerDied() {}

    public static void write(MessageC2SPlayerDied msg, FriendlyByteBuf buf) {}

    public static MessageC2SPlayerDied read(FriendlyByteBuf buf) {
        return new MessageC2SPlayerDied();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        HardcoreLevelHandler.HANDLER.hardcorePlayerDied(Minecraft.getInstance());
    }
}
