package net.ozbozmodz.eggmod.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

import java.awt.*;

public class ReachEffect extends StatusEffect {

    public ReachEffect() {
        super(StatusEffectCategory.NEUTRAL, Color.HSBtoRGB(0, 100, 46));
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        // set reach to 6-8 blocks
        EntityAttributeInstance blockRange = entity.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE);
        EntityAttributeInstance entityRange = entity.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
        if (blockRange != null && entityRange != null){
            blockRange.updateModifier(new EntityAttributeModifier(Identifier.ofVanilla("player_block_interaction_range"), (amplifier+1)*4, EntityAttributeModifier.Operation.ADD_VALUE));
            entityRange.updateModifier(new EntityAttributeModifier(Identifier.ofVanilla("player_entity_interaction_range"), (amplifier+1)*4, EntityAttributeModifier.Operation.ADD_VALUE));
        }
        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        EntityAttributeInstance blockRange = attributeContainer.getCustomInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE);
        EntityAttributeInstance entityRange = attributeContainer.getCustomInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
        if (blockRange != null && entityRange != null) {
            blockRange.removeModifier(Identifier.ofVanilla("player_block_interaction_range"));
            entityRange.removeModifier(Identifier.ofVanilla("player_entity_interaction_range"));
        }
        super.onRemoved(attributeContainer);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
