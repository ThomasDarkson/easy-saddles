package easy.saddles.mixin;

import net.minecraft.entity.Saddleable;
import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)

public class MobEntityMixin {
	@Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
	protected void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
    	MobEntity mob = (MobEntity) (Object) this;
		if (mob instanceof Saddleable saddleMob) {
			boolean playerGood = player.isSneaking();
			if (playerGood && saddleMob.isSaddled()) {
				for (Field i : mob.getClass().getDeclaredFields()) {
					i.setAccessible(true);
					try {
						Object o = i.get(mob);
						if (o instanceof SaddledComponent component) 
						{
							mob.dropStack(new ItemStack(Items.SADDLE));
							component.setSaddled(false);
							player.swingHand(hand);
						}
					} 
					catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}