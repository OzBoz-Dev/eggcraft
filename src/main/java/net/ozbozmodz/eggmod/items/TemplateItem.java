package net.ozbozmodz.eggmod.items;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class TemplateItem extends Item {

    public TemplateItem(Settings settings) {
        super(settings.maxDamage(16).maxCount(1));
    }

    public String getType(){
        return Registries.ITEM.getId(this).getPath().replaceFirst("templates/", "");
    }
}
