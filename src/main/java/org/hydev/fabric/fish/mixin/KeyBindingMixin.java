package org.hydev.fabric.fish.mixin;

import net.minecraft.client.options.KeyBinding;
import org.hydev.fabric.fish.mixinterfaces.KeyBindingI;
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
 * @since 2020-03-01 14:17
 */
@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyBindingI
{
    @Shadow private boolean pressed;

    @Override
    public void setPressed(boolean pressed)
    {
        this.pressed = pressed;
    }
}
