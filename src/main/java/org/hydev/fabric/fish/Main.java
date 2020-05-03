package org.hydev.fabric.fish;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import org.hydev.fabric.fish.mixinterfaces.KeyBindingI;

import java.util.Random;

import static net.minecraft.enchantment.EnchantmentHelper.getLevel;
import static net.minecraft.enchantment.EnchantmentHelper.hasVanishingCurse;
import static org.hydev.fabric.fish.MiscUtils.*;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2020-03-01!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-03-01 11:16
 */
public class Main implements ModInitializer
{
    public static Main instance;
    public static Random random = new Random();

    public boolean enabled;
    public boolean walking;
    public boolean walkRight;
    public int walkTimer;

    private int bestRodValue;
    private int bestRodSlot;

    private int castRodTimer;
    private int reelInTimer;

    private int scheduledWindowClick;

    @Override
    public void onInitialize()
    {
        instance = this;

        bestRodValue = -1;
        bestRodSlot = -1;
        castRodTimer = 0;
        reelInTimer = -1;
        scheduledWindowClick = -1;

        System.out.println("AntiAntiAutofish loaded.");
    }

    public void resetTimer()
    {
        walkTimer = 0;
        castRodTimer = generateCastRodTimer();
        reelInTimer = 1200;
    }

    public int generateCastRodTimer()
    {
        return 15 + random.nextInt(15);
    }

    /**
     * On tick
     */
    public void onUpdate()
    {
        if (!enabled) return;

        ClientPlayerEntity player = mc().player;
        PlayerInventory inventory = player.inventory;

        if(scheduledWindowClick != -1)
        {
            imc().getInteractionManager().windowClick_PICKUP(scheduledWindowClick);
            return;
        }

        updateBestRod();

        if (bestRodSlot == -1)
        {
            print("Out of fishing rods.");
            enabled = false;
            return;
        }

        if (bestRodSlot != inventory.selectedSlot)
        {
            selectBestRod();
            return;
        }

        // AntiAntiAutofish - Walk
        if (walkTimer > 0)
        {
            // Start walking if not already
            if (!walking)
            {
                KeyBindingI key = (KeyBindingI) (walkRight ? mc().options.keyRight : mc().options.keyLeft);
                key.setPressed(walking = true);
            }

            walkTimer --;
            return;
        }
        else
        {
            // Finished walking
            if (walking)
            {
                KeyBindingI key = (KeyBindingI) (walkRight ? mc().options.keyRight : mc().options.keyLeft);
                key.setPressed(walking = false);
            }
        }

        // Not casted yet
        if (player.fishHook == null)
        {
            // Wait for timer
            if (castRodTimer > 0)
            {
                castRodTimer --;
            }

            // Timer is done
            else
            {
                rightClick();
                resetTimer();
            }
        }

        // Casted
        else
        {
            // Auto reel in after 60s
            if (reelInTimer > 0)
            {
                reelInTimer--;
            }
            else
            {
                rightClick();
                resetTimer();
            }
        }
    }

    private void updateBestRod()
    {
        PlayerInventory inventory = player().inventory;
        int selectedSlot = inventory.selectedSlot;
        ItemStack selectedStack = inventory.getInvStack(selectedSlot);

        // start with selected rod
        bestRodValue = getRodValue(selectedStack);
        bestRodSlot = bestRodValue > -1 ? selectedSlot : -1;

        // search inventory for better rod
        for(int slot = 0; slot < 36; slot++)
        {
            ItemStack stack = inventory.getInvStack(slot);
            int rodValue = getRodValue(stack);

            if(rodValue > bestRodValue)
            {
                bestRodValue = rodValue;
                bestRodSlot = slot;
            }
        }
    }

    private int getRodValue(ItemStack stack)
    {
        if(stack.isEmpty() || !(stack.getItem() instanceof FishingRodItem))
            return -1;

        int luckOTSLvl = getLevel(Enchantments.LUCK_OF_THE_SEA, stack);
        int lureLvl = getLevel(Enchantments.LURE, stack);
        int unbreakingLvl = getLevel(Enchantments.UNBREAKING, stack);
        int mendingBonus = getLevel(Enchantments.MENDING, stack);
        int noVanishBonus = hasVanishingCurse(stack) ? 0 : 1;

        return luckOTSLvl * 9 + lureLvl * 9 + unbreakingLvl * 2 + mendingBonus + noVanishBonus;
    }

    private void selectBestRod()
    {
        PlayerInventory inventory = player().inventory;

        if(bestRodSlot < 9)
        {
            inventory.selectedSlot = bestRodSlot;
            return;
        }

        int firstEmptySlot = inventory.getEmptySlot();

        if(firstEmptySlot != -1)
        {
            if(firstEmptySlot >= 9)
            {
                imc().getInteractionManager().windowClick_QUICK_MOVE(36 + inventory.selectedSlot);
            }

            imc().getInteractionManager().windowClick_QUICK_MOVE(bestRodSlot);

        }
        else
        {
            imc().getInteractionManager().windowClick_PICKUP(bestRodSlot);
            imc().getInteractionManager().windowClick_PICKUP(36 + inventory.selectedSlot);

            scheduledWindowClick = -bestRodSlot;
        }
    }

    public void onReceivedPacket(Packet<?> packet)
    {
        if (!enabled) return;

        double validRange = 1.5;

        if(player() == null || player().fishHook == null)
            return;

        if(!(packet instanceof PlaySoundS2CPacket))
            return;

        // check sound type
        PlaySoundS2CPacket sound = (PlaySoundS2CPacket) packet;
        if(!SoundEvents.ENTITY_FISHING_BOBBER_SPLASH.equals(sound.getSound()))
            return;

        // check position
        FishingBobberEntity bobber = player().fishHook;
        if(Math.abs(sound.getX() - bobber.x) > validRange
            || Math.abs(sound.getZ() - bobber.z) > validRange)
            return;

        // catch fish
        rightClick();
        resetTimer();

        // Random rotation
        PlayerUtils.rotate(new PlayerUtils.Rotation(player().yaw + 180, random.nextFloat() * 50 - 25));

        walkTimer = 10;
    }

    private void rightClick()
    {
        // check held item
        ItemStack stack = player().inventory.getMainHandStack();
        if(stack.isEmpty() || !(stack.getItem() instanceof FishingRodItem))
            return;

        // right click
        imc().rightClick();
    }
}
