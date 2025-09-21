package net.ozbozmodz.eggmod.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.awt.*;

public class ReachEffect extends StatusEffect {

    public ReachEffect() {
        super(StatusEffectCategory.NEUTRAL, Color.HSBtoRGB(0, 100, 46));
    }

    private EntityAttributeModifier currentBlockModifier;
    private EntityAttributeModifier currentEntityModifier;

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        // set reach to 6-8 blocks
        EntityAttributeInstance blockRange = entity.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE);
        EntityAttributeInstance entityRange = entity.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
        if (blockRange != null && entityRange != null){
            currentBlockModifier = new EntityAttributeModifier("player_block_interaction_range", (amplifier+1)*4, EntityAttributeModifier.Operation.ADD_VALUE);
            currentEntityModifier = new EntityAttributeModifier("player_entity_interaction_range", (amplifier+1)*4, EntityAttributeModifier.Operation.ADD_VALUE);
            blockRange.updateModifier(currentBlockModifier);
            entityRange.updateModifier(currentEntityModifier);
        }
        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        EntityAttributeInstance blockRange = attributeContainer.getCustomInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE);
        EntityAttributeInstance entityRange = attributeContainer.getCustomInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
        if (blockRange != null && entityRange != null && currentBlockModifier != null && currentEntityModifier != null) {
            blockRange.removeModifier(currentBlockModifier);
            entityRange.removeModifier(currentEntityModifier);
        }
        super.onRemoved(attributeContainer);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
