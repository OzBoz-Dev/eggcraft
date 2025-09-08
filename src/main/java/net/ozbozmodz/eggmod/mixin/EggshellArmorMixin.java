package net.ozbozmodz.eggmod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.ozbozmodz.eggmod.armor.EggshellArmorMaterial;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class EggshellArmorMixin {

    @Shadow public abstract void playSound(@Nullable SoundEvent sound);

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"), cancellable = true)
    protected void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Again, a little trick to grab our entity instance
        LivingEntity ourEntity = (LivingEntity) (Object)this;
        ServerWorld world = (ServerWorld)ourEntity.getWorld();
        if (!world.isClient()) {
            // Check all the armor this entity is wearing
            for (ItemStack stack : ourEntity.getArmorItems()) {
                if (stack.getItem() instanceof ArmorItem) {
                    // If they have eggshell armor, damage it (breaks it), then cancel the damage method to negate entity damage
                    if (((ArmorItem) stack.getItem()).getMaterial().equals(EggshellArmorMaterial.EGGSHELL_ARMOR_MATERIAL)) {
                        stack.damage(10, ourEntity, ((ArmorItem) stack.getItem()).getSlotType());
                        world.spawnParticles(ParticleTypes.ENCHANTED_HIT, ourEntity.getX(), ourEntity.getY(), ourEntity.getZ(), 5, 0, 0.3f, 0, 1);
                        // Return false
                        cir.setReturnValue(false);
                        if (ourEntity instanceof ServerPlayerEntity serverUser && serverUser.getServer() != null) {
                            AdvancementEntry adv = serverUser.getServer().getAdvancementLoader().get(Identifier.of("eggmod", "absorb_damage"));
                            if (adv != null) {
                                AdvancementProgress progress = serverUser.getAdvancementTracker().getProgress(adv);
                                for (String criterion : progress.getUnobtainedCriteria()) {
                                    serverUser.getAdvancementTracker().grantCriterion(adv, criterion);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "damageEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V"), cancellable = true)
    protected void damageEquipment(DamageSource source, float amount, EquipmentSlot[] slots, CallbackInfo ci, @Local ItemStack itemStack){
        // Eggshell armor can only be damaged by specifically tanking a hit, avoids double counting
        if(itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getMaterial().equals(EggshellArmorMaterial.EGGSHELL_ARMOR_MATERIAL)){
            ci.cancel();
        }
    }
}
