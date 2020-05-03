package org.hydev.fabric.fish.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.hydev.fabric.fish.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2020-03-01!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-03-01 12:34
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", ordinal = 0), method = "tick()V")
    private void onTick(CallbackInfo ci)
    {
        Main.instance.onUpdate();
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String msg, CallbackInfo info)
    {
        // Command
        if (msg.toLowerCase().startsWith("/aaa"))
        {
            // Let it not pass to the server
            info.cancel();

            // Enable
            if (msg.equalsIgnoreCase("/aaa enable"))
            {
                Main.instance.enabled = true;
            }

            // Disable
            if (msg.equalsIgnoreCase("/aaa disable"))
            {
                Main.instance.enabled = false;
            }
        }
    }
}
