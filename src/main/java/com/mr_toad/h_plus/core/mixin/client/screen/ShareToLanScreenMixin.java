package com.mr_toad.h_plus.core.mixin.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.commands.PublishCommand;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;

@Mixin(ShareToLanScreen.class)
public abstract class ShareToLanScreenMixin extends Screen {

    @Shadow @Final private static Component ALLOW_COMMANDS_LABEL;
    @Shadow @Final private static Component GAME_MODE_LABEL;
    @Shadow @Final private Screen lastScreen;
    @Shadow private EditBox portEdit;
    @Shadow private int port;

    @Shadow @Nullable protected abstract Component tryParsePort(String s);

    @Shadow @Final private static Component INFO_TEXT;
    @Shadow @Final private static Component PORT_INFO_TEXT;

    @Shadow private GameType gameMode;
    @Shadow private boolean commands;
    @Unique private Component h_$bp01;
    @Unique private Component h_$bp02;

    protected ShareToLanScreenMixin(Component c) {
        super(c);
    }

    /**
     * @author n
     * @reason n
     */

    @Overwrite
    public void init() {

        boolean hardcore = this.minecraft.level.getLevelData().isHardcore();
        IntegratedServer server = this.minecraft.getSingleplayerServer();

        this.portEdit = new EditBox(this.font, this.width / 2 - 75, 160, 150, 20, Component.translatable("lanServer.port"));

        this.gameMode = server.getDefaultGameType();
        this.commands = server.getWorldData().getAllowCommands();

        if (!hardcore) {
            this.addRenderableWidget(CycleButton.builder(GameType::getShortDisplayName).withValues(GameType.values()).withInitialValue(this.gameMode).create(this.width / 2 - 155, 100, 150, 20, GAME_MODE_LABEL, (cycleButton, gameType) -> this.gameMode = gameType));
            this.addRenderableWidget(CycleButton.onOffBuilder(this.commands).create(this.width / 2 + 5, 100, 150, 20, ALLOW_COMMANDS_LABEL, (cycleButton, c) -> this.commands = c));
        }

        Button b = Button.builder(Component.translatable("lanServer.start"), (button) -> {
            this.minecraft.setScreen(null);
            MutableComponent component;
            if (server.publishServer(this.gameMode, this.commands, this.port)) {
                component = PublishCommand.getSuccessMessage(this.port);
            } else {
                component = Component.translatable("commands.publish.failed");
            }

            this.minecraft.gui.getChat().addMessage(component);
            this.minecraft.updateTitle();
        }).bounds(this.width / 2 - 155, this.height - 28, 150, 20).build();

        this.portEdit.setHint(Component.literal("" + this.port).withStyle(ChatFormatting.DARK_GRAY));
        this.portEdit.setResponder((s) -> {
            Component c = this.tryParsePort(s);
            this.portEdit.setHint(Component.literal("" + this.port).withStyle(ChatFormatting.DARK_GRAY));
            if (c == null) {
                this.portEdit.setTextColor(14737632);
                this.portEdit.setTooltip(null);
                b.active = true;
            } else {
                this.portEdit.setTextColor(16733525);
                this.portEdit.setTooltip(Tooltip.create(c));
                b.active = false;
            }

        });

        this.addRenderableWidget(this.portEdit);
        this.addRenderableWidget(b);
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (button) -> this.minecraft.setScreen(this.lastScreen)).bounds(this.width / 2 + 5, this.height - 28, 150, 20).build());

        this.h_$bp01 = Component.translatable("h_plus.shareToLan.blocked01");
        this.h_$bp02 = Component.translatable("h_plus.shareToLan.blocked02");

    }

    /**
     * @author n
     * @reason n
     */

    @Overwrite
    public void render(PoseStack stack, int i0, int i1, float i2) {
        if (!this.minecraft.level.getLevelData().isHardcore()) {
            drawCenteredString(stack, this.font, this.title, this.width / 2, 50, 16777215);
            drawCenteredString(stack, this.font, INFO_TEXT, this.width / 2, 82, 16777215);
        } else {
            drawCenteredString(stack, this.font, this.h_$bp01, this.width / 2, 80, 16777215);
            drawCenteredString(stack, this.font, this.h_$bp02, this.width / 2, 110, 16777215);
        }
        this.renderBackground(stack);
        drawCenteredString(stack, this.font, PORT_INFO_TEXT, this.width / 2, 142, 16777215);
        super.render(stack, i0, i1, i2);
    }



}
