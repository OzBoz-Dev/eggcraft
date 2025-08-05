package net.ozbozmodz.eggmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ExperienceCatcherScreen extends HandledScreen<ExperienceCatcherScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of("eggmod", "textures/gui/experience_catcher_gui.png");

    public ExperienceCatcherScreen(ExperienceCatcherScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 185;
        this.titleY = 6;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = ((this.width - this.backgroundWidth) / 2);
        int y = ((this.height - this.backgroundHeight) / 2);

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        // Makes sure the bar will decrease from top rather than bottom
        context.drawTexture(GUI_TEXTURE, x + 45, y + 76 - handler.getEnergyBar(), 179, 7, 34, handler.getEnergyBar(), 256, 256);
        context.drawTexture(GUI_TEXTURE, x + 45, y + 19, 180, 80, 34, 58, 256, 256);
        if (handler.getExp() > 0) context.drawTexture(GUI_TEXTURE, x + 97 - handler.getPipeWidth(), y + 77, 180 + 37 - handler.getPipeWidth(), 70, handler.getPipeWidth(), 8, 256,256);
        if (mouseX >= 168 && mouseX <= 203 && mouseY >= 46 && mouseY <= 105){
            context.drawTooltip(textRenderer, Text.literal("Experience: " + handler.getExp() + "/2000").formatted(Formatting.GREEN), mouseX, mouseY);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }


}
