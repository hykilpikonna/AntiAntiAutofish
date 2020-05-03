package org.hydev.fabric.fish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.hydev.fabric.fish.mixinterfaces.MinecraftClientI;

/**
 * Miscellaneous utility methods.
 * <p>
 * Class created by the HyDEV Team on 2020-01-24!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-01-24 18:56
 */
public class MiscUtils
{
    /**
     * Print to player's client side chat box
     *
     * @param text Text
     * @return 1 (For convenience when using commands)
     */
    public static int print(Text text)
    {
        if (player() == null) return 1;
        player().addChatMessage(new LiteralText("[AAAutofish] ").append(text), false);
        return 1;
    }

    /**
     * Print to player's client side chat box
     *
     * @param text Text
     * @return 1 (For convenience when using commands)
     */
    public static int print(String text)
    {
        return print(new LiteralText(text));
    }

    /**
     * Sleep without exceptions
     *
     * @param ms Time in ms
     */
    public static void sleep(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static MinecraftClient mc()
    { return MinecraftClient.getInstance(); }
    public static MinecraftClientI imc()
    { return (MinecraftClientI) MinecraftClient.getInstance(); }
    public static ClientWorld world()
    { return mc().world; }
    public static ClientPlayerEntity player()
    { return mc().player; }
    public static ClientPlayerInteractionManager interactionManager()
    { return mc().interactionManager; }
}
