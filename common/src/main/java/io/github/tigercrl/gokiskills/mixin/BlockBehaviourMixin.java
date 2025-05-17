package io.github.tigercrl.gokiskills.mixin;

import huix.infinity.common.world.item.*;
import io.github.tigercrl.gokiskills.skill.SkillInfo;
import io.github.tigercrl.gokiskills.skill.SkillManager;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.tigercrl.gokiskills.skill.Skills.*;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    @Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
    public void destroySpeedBonus(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        SkillInfo info = SkillManager.getInfo(player);
        ItemStack item = player.getMainHandItem();
        double bonus = 1.0;
        if (info.isEnabled(CHOPPING) && item.is(ItemTags.AXES) || item.getItem() instanceof BattleAxeTool || item.getItem() instanceof HatchetTool && blockState.is(BlockTags.MINEABLE_WITH_AXE)) {
            bonus += info.getBonus(CHOPPING);
        } else if (info.isEnabled(DIGGING) && item.is(ItemTags.SHOVELS) || item.getItem() instanceof HoeTool || item.getItem() instanceof MattockTool && blockState.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
            bonus += info.getBonus(DIGGING);
        } else if (info.isEnabled(HARVESTING) && item.is(ItemTags.HOES) || item.getItem() instanceof HoeTool || item.getItem() instanceof MattockTool && blockState.is(BlockTags.MINEABLE_WITH_HOE)) {
            bonus += info.getBonus(HARVESTING);
        } else if (info.isEnabled(MINING) && item.is(ItemTags.PICKAXES) || item.getItem() instanceof WarHammerTool && blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            bonus += info.getBonus(MINING);
        } else if (info.isEnabled(SHEARING) && item.is(Items.SHEARS) || item.getItem() instanceof ShearsTool && item.getDestroySpeed(blockState) != 1) {
            bonus += info.getBonus(SHEARING);
        }
        cir.setReturnValue(cir.getReturnValue() * (float) bonus);
    }
}