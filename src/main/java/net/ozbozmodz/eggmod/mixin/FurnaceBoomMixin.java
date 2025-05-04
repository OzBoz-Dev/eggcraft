package net.ozbozmodz.eggmod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.ozbozmodz.eggmod.util.RegisterItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)

public class FurnaceBoomMixin {
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;setLastRecipe(Lnet/minecraft/recipe/RecipeEntry;)V"))
    private static void tick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
		// Get the cooking item. If it is burnt egg, create an explosion
		ItemStack itemStack = blockEntity.getStack(2);
		if (itemStack.isOf(RegisterItems.BURNTEGG)){
			world.createExplosion(null, pos.getX() , pos.getY(), pos.getZ(), 3, World.ExplosionSourceType.BLOCK);
		}
	}
}
