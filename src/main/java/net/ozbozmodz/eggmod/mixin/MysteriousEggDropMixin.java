package net.ozbozmodz.eggmod.mixin;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.event.GameEvent;
import net.ozbozmodz.eggmod.util.RegisterAll;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChickenEntity.class)
public class MysteriousEggDropMixin {
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/ChickenEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"), cancellable = true)
    protected void tickMovement(CallbackInfo ci){
        ChickenEntity chicken = (ChickenEntity)(Object)(this);
        Random r = Random.create();
        // 10% chance to drop a mysterious egg instead of a normal one
        if (r.nextFloat() <= 0.1f){
            chicken.playSound(RegisterAll.MYSTERIOUS_EGG_LAID, 1.0F, (r.nextFloat() - r.nextFloat()) * 0.2F + 1.0F);
            chicken.dropItem(RegisterAll.MYSTERIOUS_EGG_ITEM);
            chicken.emitGameEvent(GameEvent.ENTITY_PLACE);
            chicken.eggLayTime = r.nextInt(6000) + 6000;
            ci.cancel();
        }
    }
}
