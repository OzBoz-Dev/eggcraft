package net.ozbozmodz.eggmod.fooditems;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class modFoodComps {
    public static final FoodComponent BURNTEGG = new FoodComponent.Builder().nutrition(1).saturationModifier(0.1f).statusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 2), 1.0f).alwaysEdible().build();
    public static final FoodComponent FRIEDEGG = new FoodComponent.Builder().nutrition(6).saturationModifier(1.0f).build();
}