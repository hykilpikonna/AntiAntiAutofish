package org.hydev.fabric.fish.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.hydev.fabric.fish.mixinterfaces.ClientPlayerInteractionI;
import org.hydev.fabric.fish.mixinterfaces.MinecraftClientI;
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
 * @since 2020-03-01 11:43
 */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements MinecraftClientI
{
    @Shadow private void doItemUse() {}
    @Override public void rightClick() { doItemUse(); }

    @Shadow public ClientPlayerInteractionManager interactionManager;
    @Override public ClientPlayerInteractionI getInteractionManager()
    {
        return (ClientPlayerInteractionI) interactionManager;
    }
}
