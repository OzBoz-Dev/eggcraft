package net.ozbozmodz.eggmod.items;

import net.minecraft.item.Item;

public class TemplateItem extends Item {
    private String type;

    public TemplateItem(Settings settings, String type) {
        super(settings.maxDamage(16).maxCount(1));
        this.type = type;

    }

    public String getType(){
        return this.type;
    }
}
