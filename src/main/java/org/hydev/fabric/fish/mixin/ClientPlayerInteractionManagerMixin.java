package org.hydev.fabric.fish.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.hydev.fabric.fish.mixinterfaces.ClientPlayerInteractionI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2020-03-01!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-03-01 12:26
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements ClientPlayerInteractionI
{
    @Shadow
    private MinecraftClient client;
    @Shadow
    private float currentBreakingProgress;
    @Shadow
    private boolean breakingBlock;

    @Override
    public float getCurrentBreakingProgress()
    {
        return currentBreakingProgress;
    }

    @Override
    public void setBreakingBlock(boolean breakingBlock)
    {
        this.breakingBlock = breakingBlock;
    }

    @Override
    public ItemStack windowClick_PICKUP(int slot)
    {
        return method_2906(0, slot, 0, SlotActionType.PICKUP, client.player);
    }

    @Override
    public ItemStack windowClick_QUICK_MOVE(int slot)
    {
        return method_2906(0, slot, 0, SlotActionType.QUICK_MOVE, client.player);
    }

    @Override
    public ItemStack windowClick_THROW(int slot)
    {
        return method_2906(0, slot, 1, SlotActionType.THROW, client.player);
    }

    @Override
    public void rightClickItem()
    {
        interactItem(client.player, client.world, Hand.MAIN_HAND);
    }

    @Override
    public void rightClickBlock(BlockPos pos, Direction side, Vec3d hitVec)
    {
        interactBlock(client.player, client.world, Hand.MAIN_HAND,
            new BlockHitResult(hitVec, side, pos, false));
        interactItem(client.player, client.world, Hand.MAIN_HAND);
    }

    @Override
    public void sendPlayerActionC2SPacket(PlayerActionC2SPacket.Action action, BlockPos blockPos,
                                          Direction direction)
    {
        method_21706(action, blockPos, direction);
    }

    @Shadow
    private void method_21706(PlayerActionC2SPacket.Action action, BlockPos blockPos, Direction direction) {}

    @Shadow
    public abstract ActionResult interactBlock(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Hand hand, BlockHitResult blockHitResult);

    @Shadow
    public abstract ActionResult interactItem(PlayerEntity playerEntity, World world, Hand hand);

    @Shadow
    public abstract ItemStack method_2906(int syncId, int slotId, int mouseButton, SlotActionType actionType, PlayerEntity player);
}
