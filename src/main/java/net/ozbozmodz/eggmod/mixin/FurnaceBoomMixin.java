package net.ozbozmodz.eggmod.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.ozbozmodz.eggmod.util.RegisterAll;
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
		if (itemStack.isOf(RegisterAll.BURNT_EGG_ITEM)){
            for (PlayerEntity player : world.getPlayers()){
                if (world.isPlayerInRange(pos.getX(), pos.getY(), pos.getZ(), 10) && player instanceof ServerPlayerEntity serverUser && serverUser.getServer() != null) {
                    AdvancementEntry adv = serverUser.getServer().getAdvancementLoader().get(Identifier.of("eggmod", "get_burnt_egg"));
                    if (adv != null) {
                        AdvancementProgress progress = serverUser.getAdvancementTracker().getProgress(adv);
                        for (String criterion : progress.getUnobtainedCriteria()) {
                            serverUser.getAdvancementTracker().grantCriterion(adv, criterion);
                        }
                    }
                }
            }
            if (blockEntity.getStack(0).isOf(RegisterAll.RAW_GIANT_EGG_ITEM)){
                world.createExplosion(null, pos.getX() , pos.getY(), pos.getZ(), 6, World.ExplosionSourceType.BLOCK);
            }
			else world.createExplosion(null, pos.getX() , pos.getY(), pos.getZ(), 3, World.ExplosionSourceType.BLOCK);
		}
	}
}
