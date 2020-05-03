package org.hydev.fabric.fish.mixinterfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

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
public interface ClientPlayerInteractionI
{
    public float getCurrentBreakingProgress();

    public void setBreakingBlock(boolean breakingBlock);

    public ItemStack windowClick_PICKUP(int slot);

    public ItemStack windowClick_QUICK_MOVE(int slot);

    public ItemStack windowClick_THROW(int slot);

    public void rightClickItem();

    public void rightClickBlock(BlockPos pos, Direction side, Vec3d hitVec);

    public void sendPlayerActionC2SPacket(PlayerActionC2SPacket.Action action, BlockPos blockPos, Direction direction);
}
