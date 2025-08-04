package net.ozbozmodz.eggmod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.ozbozmodz.eggmod.throwableEggs.ExperienceEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (ExperienceOrbEntity.class)
public class ExperienceEggMixin {

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExperience(I)V"), cancellable = true)
    protected void onPlayerCollision(PlayerEntity player, CallbackInfo ci, @Local int i){
        ExperienceOrbEntity ourEntity = (ExperienceOrbEntity)(Object)this;
        for (ItemStack stack : player.getEquippedItems()){
            if (stack.getItem() instanceof ExperienceEggItem ei && ei.getExperience(stack) < ei.maxExperience){
                ei.addExp(i, stack);
                ourEntity.discard();
                ci.cancel();
            }
        }
    }
}
