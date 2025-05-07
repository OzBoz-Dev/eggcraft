package net.ozbozmodz.eggmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EtcherBlockScreen extends HandledScreen<EtcherBlockScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of("eggmod", "textures/gui/etcher_gui.png");

    public static final Identifier ARROW_TEXTURE =
            Identifier.of("eggmod", "textures/gui/etcher_arrow.png");

    public EtcherBlockScreen(EtcherBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = ((this.width - this.backgroundWidth) / 2);
        int y = ((this.height - this.backgroundHeight) / 2);

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderProgressArrow(context, x, y);
    }

    // Renders our progress arrow, taking the arrow pos, size and progress
    private void renderProgressArrow(DrawContext context, int x, int y){
        if (handler.isCrafting()){
            context.drawTexture(ARROW_TEXTURE, x + 73, y + 35, 0, 0,
                    handler.getScaledArrowProgress(), 16, 24, 16);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
