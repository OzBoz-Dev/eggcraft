package net.ozbozmodz.eggmod.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.ozbozmodz.eggmod.recipe.EtcherRecipe;
import net.ozbozmodz.eggmod.screen.EtcherBlockScreen;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class EggmodREIClient implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new EtcherCategory());
        registry.addWorkstations(EtcherCategory.ETCHER_DISPLAY_CATEGORY_IDENTIFIER, EntryStacks.of(RegisterAll.ETCHER_BLOCK));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(EtcherRecipe.class, RegisterAll.ETCHER_RECIPE_TYPE,
                EtcherDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle((screen.width-176)/2 + 78,
                ((screen.height-166)/2) + 30, 20, 25), EtcherBlockScreen.class,
                EtcherCategory.ETCHER_DISPLAY_CATEGORY_IDENTIFIER);
    }
}
