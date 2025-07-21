package net.ozbozmodz.eggmod.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EggEntity.class)

public class EggshellMixin {
    @Inject(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/EggEntity;discard()V"))
    protected void onCollision(HitResult hitResult, CallbackInfo ci) {
        // 1/4 chance to drop an eggshell, 1/8 chance to drop 2
        if (new Random().nextInt(4) == 0){
            // Trick to get an instance of our object out of a mixin!
            EggEntity ourEgg = (EggEntity)(Object)this;
            World world = ourEgg.getWorld();
            ItemStack eggshell;
            if (new Random().nextInt(2) == 0){
                eggshell = new ItemStack(RegisterAll.EGGSHELL_ITEM, 2);
            }
            else eggshell = new ItemStack(RegisterAll.EGGSHELL_ITEM, 1);
            // Create an item entity, and send it
            ItemEntity eggshellItem = EntityType.ITEM.create(world);
            if (eggshellItem != null) {
                eggshellItem.setStack(eggshell);
                eggshellItem.setPosition(hitResult.getPos());
                world.spawnEntity(eggshellItem.dropStack(eggshell));
            }
        }
    }
}
