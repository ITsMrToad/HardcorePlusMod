package com.mr_toad.h_plus.common.util;

import com.mr_toad.h_plus.core.HPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelStorageSource;

import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.util.List;

public class HardcoreLevelHandler {

    public static final HardcoreLevelHandler HANDLER = new HardcoreLevelHandler();

    @Nullable public String level;

    public void delete(@Nullable Minecraft minecraft) {
        if (minecraft != null) {
            if (minecraft.level != null) {
                minecraft.level.disconnect();
            }
            minecraft.clearLevel(new GenericDirtMessageScreen(Component.translatable("h_plus.hardcore_death.deleting", this.level)));
            minecraft.setScreen(new TitleScreen());

            this.deleteWorld(minecraft);
        }
    }

    public void deleteWorld(Minecraft mc) {
        if (this.level == null) return;
        LevelStorageSource levelSource = mc.getLevelSource();
        try(LevelStorageSource.LevelStorageAccess storageAccess = levelSource.createAccess(this.level)) {
            try {
                storageAccess.deleteLevel();
            } catch (IOException e) {
                try {
                    storageAccess.close();
                } catch (IOException e1) {
                    e.addSuppressed(e1);
                }
                throw e;
            }
        } catch (IOException e) {
            SystemToast.onWorldDeleteFailure(mc, this.level);
            HPlus.LOGGER.error("Failed to delete world {}", this.level, e);
        }
        this.level = null;
    }

    public void hardcorePlayerDied(@Nullable Minecraft minecraft) {
        if (minecraft == null) return;

        IntegratedServer singleplayerServer = minecraft.getSingleplayerServer();
        if (minecraft.isSingleplayer()) {
            if (singleplayerServer != null) {
                this.level = singleplayerServer.storageSource.getLevelId();
            }
        } else {
            ClientLevel clientLevel = minecraft.level;
            if (clientLevel != null) {
                MinecraftServer server = clientLevel.getServer();
                LocalPlayer localPlayer = minecraft.player;

                if (localPlayer == null) return;

                if (!localPlayer.hasPermissions(3)) {
                    this.level = null;
                    localPlayer.sendSystemMessage(Component.translatable("h_plus.hardcore_death.no_permissions"));
                    return;
                }

                if (server != null) {
                    List<ServerPlayer> playerList = server.getPlayerList().getPlayers();
                    if (playerList.size() == 1) {
                        this.level = server.storageSource.getLevelId();
                    } else {
                        List<ServerPlayer> aliveOuter = playerList.stream().filter(p -> p.isAlive() && !p.isSpectator()).toList();
                        if (!aliveOuter.isEmpty()) {
                            localPlayer.sendSystemMessage(Component.translatable("h_plus.hardcore_death.alive_players", aliveOuter.size()));
                            this.level = null;
                        } else {
                            this.level = server.storageSource.getLevelId();
                        }
                    }
                }
            }
        }
    }

    public void reload() {
        this.level = null;
    }
}
