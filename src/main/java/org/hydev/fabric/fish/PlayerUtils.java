package org.hydev.fabric.fish;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import static org.hydev.fabric.fish.MiscUtils.player;

/**
 * Utils for controlling the player.
 * <p>
 * Class created by the HyDEV Team on 2020-01-24!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-01-24 22:34
 */
public class PlayerUtils
{
    /**
     * Get player's eye vector
     *
     * @return Eye vector
     * @author Wurst7 https://github.com/Wurst-Imperium/Wurst7
     */
    public static Vec3d getEyesPos()
    {
        ClientPlayerEntity player = player();

        return new Vec3d(player.x,
            player.y + player.getEyeHeight(player.getPose()),
            player.z);
    }

    /**
     * Get what does it take to rotate to a direction
     *
     * @param vec Final direction
     * @return Rotation required
     * @author Wurst7 https://github.com/Wurst-Imperium/Wurst7
     */
    public static Rotation getNeededRotations(Vec3d vec)
    {
        Vec3d eyesPos = getEyesPos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new Rotation(yaw, pitch);
    }

    /**
     * Rotate
     *
     * @param rotation Relative rotation
     */
    public static void rotate(Rotation rotation)
    {
        player().yaw = rotation.getYaw();
        player().pitch = rotation.getPitch();
    }

    /**
     * Rotation
     */
    public static final class Rotation
    {
        private final float yaw;
        private final float pitch;

        /*public Rotation(float yaw, float pitch)
        {
            this.yaw = MathHelper.wrapDegrees(yaw);
            this.pitch = MathHelper.wrapDegrees(pitch);
        }*/

        public Rotation(float yaw, float pitch)
        {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public float getYaw()
        {
            return yaw;
        }

        public float getPitch()
        {
            return pitch;
        }
    }
}
