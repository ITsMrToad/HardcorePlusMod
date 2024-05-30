package com.mr_toad.h_plus.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mr_toad.h_plus.common.util.HardcoreLevelHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import org.jetbrains.annotations.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class HardcoreDeathScreen extends Screen {

    public static final Component TITLE = Component.translatable("deathScreen.title.hardcore");
    public static final Component DELETE = Component.translatable("h_plus.hardcore_death.delete");
    public static final Component SPECTATE = Component.translatable("deathScreen.spectate");

    public final List<Button> buttons = Lists.newArrayList();

    public int delayTicker;

    public Button deleteButton;
    public Button spectateButton;

    private Component deathScore = CommonComponents.EMPTY;

    public Component causeOfDeath;

    public HardcoreDeathScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        this.delayTicker = 0;
        this.buttons.clear();

        if (this.minecraft == null) return;
        if (this.minecraft.player == null) return;

        this.spectateButton = this.addRenderableWidget(Button.builder(SPECTATE, b -> {
            this.minecraft.player.respawn();
            this.minecraft.setScreen(null);
        }).bounds(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build());

        this.buttons.add(this.spectateButton);

        this.deleteButton = this.addRenderableWidget(Button.builder(DELETE, b -> HardcoreLevelHandler.HANDLER.delete(this.minecraft)).bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
        this.buttons.add(this.deleteButton);

        this.buttons.forEach(b -> b.active = false);

        LocalPlayer localPlayer = this.minecraft.player;
        if (localPlayer == null) return;
        this.deathScore = Component.translatable("deathScreen.score").append(": ").append(Component.literal(Integer.toString(localPlayer.getScore())).withStyle(ChatFormatting.RED));
    }

    @Override
    public void render(PoseStack stack, int mx, int my, float pt) {
        Screen.fillGradient(stack, 0, 0, this.width, this.height, 1615855616, -1602211792);
        stack.pushPose();

        stack.scale(2.0F, 2.0F, 2.0F);
        Screen.drawCenteredString(stack, this.font, this.getTitle(), this.width / 2 / 2, 30, 16777215);

        stack.popPose();

        if (this.causeOfDeath != null) {
            Screen.drawCenteredString(stack, this.font, this.causeOfDeath, this.width / 2, 85, 16777215);
        }

        Screen.drawCenteredString(stack, this.font, this.deathScore, this.width / 2, 100, 16777215);
        if (this.causeOfDeath != null && my > 85 && my < 85 + 9) {
            Style style = this.getClickedComponentStyleAt(mx);
            this.renderComponentHoverEffect(stack, style, mx, my);
        }
        super.render(stack, mx, my, pt);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (this.causeOfDeath != null && my > 85.0D && my < (double)(85 + 9)) {
            Style style = this.getClickedComponentStyleAt((int) mx);
            if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
                this.handleComponentClicked(style);
                return false;
            }
        }

        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.delayTicker;
        if (this.delayTicker == 20) {
            this.buttons.forEach(b -> b.active = true);
        }
    }

    @Nullable
    private Style getClickedComponentStyleAt(int m) {
        if (this.causeOfDeath == null || this.minecraft == null) {
            return null;
        } else {
            int i = this.minecraft.font.width(this.causeOfDeath);
            int j = this.width / 2 - i / 2;
            int k = this.width / 2 + i / 2;

            return m >= j && m <= k ? this.minecraft.font.getSplitter().componentStyleAtWidth(this.causeOfDeath, m - j) : null;
        }
    }

}
