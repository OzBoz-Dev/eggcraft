package net.ozbozmodz.eggmod.compat;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.LinkedList;
import java.util.List;

public class EtcherCategory implements DisplayCategory<BasicDisplay> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of("eggmod", "textures/gui/etcher_gui.png");

    public static final CategoryIdentifier<EtcherDisplay> ETCHER_DISPLAY_CATEGORY_IDENTIFIER =
            CategoryIdentifier.of("eggmod", "etcher_block");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return ETCHER_DISPLAY_CATEGORY_IDENTIFIER;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("block.eggmod.etcher_block");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(RegisterAll.ETCHER_ITEM.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(GUI_TEXTURE, new Rectangle(startPoint.x, startPoint.y, 175, 82)));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 26, startPoint.y + 26))
                .entries(display.getInputEntries().get(0)).markInput()
        );

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 133, startPoint.y + 26))
                .entries(display.getInputEntries().get(1)).markInput()
        );

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 80, startPoint.y + 20))
                .entries(display.getInputEntries().get(2)).markInput()
        );

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 80, startPoint.y + 66))
                .entries(display.getOutputEntries().get(0)).markOutput()
        );

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }
}
