package net.ozbozmodz.eggmod.fooditems;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class ModFoodComps {
    public static final FoodComponent BURNTEGG = new FoodComponent.Builder().nutrition(1).saturationModifier(0.1f).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 300, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), 1.0f).statusEffect(new StatusEffectInstance(RegisterAll.REPEL_EFFECT, 600, 2), 1.0f).alwaysEdible().build();
    public static final FoodComponent FRIEDEGG = new FoodComponent.Builder().nutrition(6).saturationModifier(1.0f).build();
}