package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author TartaricAcid
 * @date 2019/10/20 17:59
 **/
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class PlayerLoggedInNotice {
    private static boolean notFirst = false;

    @SubscribeEvent
    public static void onEnterGame(PlayerEvent.PlayerLoggedInEvent event) {
        missingPatchouliNotice(event);
    }

    private static void missingPatchouliNotice(PlayerEvent.PlayerLoggedInEvent event) {
        boolean missingPatchouli = !Loader.isModLoaded("patchouli");
        if (notFirst) {
            return;
        }
        if (missingPatchouli) {
            String json = I18n.format("message.touhou_little_maid.missing_patchouli");
            ITextComponent component = ITextComponent.Serializer.fromJsonLenient(json);
            event.player.sendMessage(component);
        }
        notFirst = true;
    }
}
