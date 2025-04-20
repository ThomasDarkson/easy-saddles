package easy.saddles.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)

public class MobEntityMixin {
	@Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
	protected void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
    	MobEntity mob = (MobEntity) (Object) this;
		boolean playerGood = player.isSneaking();
		if (playerGood) {
			ItemStack stack = mob.getEquippedStack(EquipmentSlot.SADDLE);
			if (stack != ItemStack.EMPTY) {
				if (player.getWorld() instanceof ServerWorld serverWorld) {
					mob.equipStack(EquipmentSlot.SADDLE, ItemStack.EMPTY);
					mob.dropStack(serverWorld, stack);
					player.swingHand(hand, (player instanceof ServerPlayerEntity));
				}
			}
		}
	}
}