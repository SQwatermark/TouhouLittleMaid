package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicJsonInfo;
import com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicManger;
import com.github.tartaricacid.touhoulittlemaid.client.audio.music.NetEaseMusicList;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPortableAudio;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.PortableAudioMessageToServer;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicManger.MUSIC_LIST_GROUP;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.item.GuiMusicList.MUSIC_INDEX;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class PortableAudioGui extends GuiScreen {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/netease_music_icon.png");
    protected static int LIST_INDEX;
    private final EntityPortableAudio audio;
    private final boolean isMusicListEmpty;
    protected GuiMusicList guiMusicList;
    private GuiMusicListGroup guiMusicListGroup;
    private GuiTextField musicListField;
    private String promptMsg = "";

    public PortableAudioGui(EntityPortableAudio audio) {
        this.audio = audio;
        this.isMusicListEmpty = MUSIC_LIST_GROUP.isEmpty();
    }

    @Override
    public void initGui() {
        if (isMusicListEmpty) {
            return;
        }
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        musicListField = new GuiTextField(0, mc.fontRenderer, width - 86, 5, 80, 14) {
            @Override
            public boolean getEnableBackgroundDrawing() {
                return false;
            }
        };
        musicListField.setText(String.valueOf(MUSIC_LIST_GROUP.get(LIST_INDEX).getListId()));
        musicListField.setMaxStringLength(19);
        this.guiMusicList = new GuiMusicList(this);
        this.guiMusicListGroup = new GuiMusicListGroup(this);
        this.buttonList.add(new GuiButtonImage(1, width / 2 - 33, height - 19, 16, 16,
                32, 0, 16, ICON));
        this.buttonList.add(new GuiButtonImage(2, width / 2 - 8, height - 19, 16, 16,
                0, 0, 16, ICON));
        this.buttonList.add(new GuiButtonImage(3, width / 2 + 17, height - 19, 16, 16,
                16, 0, 16, ICON));
        this.buttonList.add(new GuiButtonImage(4, width - 105, 4, 16, 16,
                48, 0, 16, ICON));
        this.buttonList.add(new VolumeButton(5, width - 110, height - 22));
        this.buttonList.add(new GuiButtonImage(6, width / 2 + 42, height - 19, 16, 16,
                64, 0, 16, ICON));
        this.buttonList.add(new GuiButtonImage(7, width - 122, 4, 16, 16,
                80, 0, 16, ICON));
    }

    @Override
    public void updateScreen() {
        if (isMusicListEmpty) {
            return;
        }
        musicListField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isMusicListEmpty) {
            this.drawGradientRect(0, 0, this.width, this.height, 0xef101010, 0xef101010);
            mc.fontRenderer.drawSplitString(I18n.format("gui.touhou_little_maid.portable_audio.play_list.empty")
                            .replace("\\n", "\n"),
                    (width - 300) / 2, (height - 170) / 2, 300, 0xff1111);
            super.drawScreen(mouseX, mouseY, partialTicks);
            return;
        }
        this.drawGradientRect(0, 0, this.width, this.height, 0xff555555, 0xff555555);
        guiMusicList.drawScreen(mouseX, mouseY, partialTicks);
        guiMusicListGroup.drawScreen(mouseX, mouseY, partialTicks);
        this.drawGradientRect(0, this.height - 23, this.width, this.height, 0xff282c34, 0xff282c34);
        this.drawGradientRect(0, 0, width, 23, 0xff282c34, 0xff282c34);
        this.drawGradientRect(width - 86, 5, width - 6, 19, 0xff3c414e, 0xff3c414e);
        musicListField.drawTextBox();
        drawString(mc.fontRenderer, promptMsg,
                width - 110 - mc.fontRenderer.getStringWidth(promptMsg), 8, 0xbe3a3a);
        MusicJsonInfo info = MUSIC_LIST_GROUP.get(LIST_INDEX).getMusicJsonInfo();
        drawString(mc.fontRenderer, I18n.format("gui.touhou_little_maid.portable_audio.play_list.importer", info.getCreator()),
                8, 8, 0x7e7c7e);
        NetEaseMusicList.PlayList playList = MUSIC_LIST_GROUP.get(Math.min(PortableAudioGui.LIST_INDEX, MUSIC_LIST_GROUP.size() - 1)).getPlayList();
        if (MUSIC_INDEX < playList.getTrackCount()) {
            FontRenderer fontRenderer = mc.fontRenderer;
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemAndEffectIntoGUI(
                    new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 0),
                    3, height - 19);
            mc.getRenderItem().renderItemAndEffectIntoGUI(
                    new ItemStack(Blocks.DOUBLE_PLANT, 1, 0),
                    3, height - 19);
            RenderHelper.disableStandardItemLighting();
            NetEaseMusicList.Track track = playList.getTracks().get(MUSIC_INDEX);
            drawString(fontRenderer, fontRenderer.trimStringToWidth(track.getName(), width / 2 - 63),
                    23, height - 20, 0xdcdde4);
            drawString(fontRenderer, fontRenderer.trimStringToWidth(track.getArtists(), width / 2 - 63),
                    23, height - 10, 0x757775);
        }
        GlStateManager.color(1, 1, 1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (isMusicListEmpty) {
            return;
        }
        if (button.id == 1) {
            if (LIST_INDEX < MUSIC_LIST_GROUP.size()) {
                NetEaseMusicList.PlayList playList = MUSIC_LIST_GROUP.get(LIST_INDEX).getPlayList();
                if ((MUSIC_INDEX - 1) < 0) {
                    MUSIC_INDEX = playList.getTrackCount() - 1;
                } else {
                    MUSIC_INDEX -= 1;
                }
                CommonProxy.INSTANCE.sendToServer(new PortableAudioMessageToServer(audio.getUniqueID(),
                        playList, MUSIC_INDEX));
            }
            return;
        }

        if (button.id == 2) {
            CommonProxy.INSTANCE.sendToServer(PortableAudioMessageToServer.getStopMessage(audio.getUniqueID()));
            return;
        }

        if (button.id == 3) {
            if (LIST_INDEX < MUSIC_LIST_GROUP.size()) {
                NetEaseMusicList.PlayList playList = MUSIC_LIST_GROUP.get(LIST_INDEX).getPlayList();
                if ((MUSIC_INDEX + 1) < playList.getTrackCount()) {
                    MUSIC_INDEX = MUSIC_INDEX + 1;
                } else {
                    MUSIC_INDEX = 0;
                }
                CommonProxy.INSTANCE.sendToServer(new PortableAudioMessageToServer(audio.getUniqueID(),
                        playList, MUSIC_INDEX));
            }
            return;
        }

        if (button.id == 4) {
            if (StringUtils.isNotBlank(musicListField.getText())) {
                try {
                    long id = Long.parseUnsignedLong(musicListField.getText());
                    MusicManger.addSingleList(id);
                } catch (NumberFormatException ignore) {
                    promptMsg = I18n.format("gui.touhou_little_maid.portable_audio.song_id.illegal");
                }
            }
            return;
        }

        if (button.id == 6) {
            NetEaseMusicList playList = MUSIC_LIST_GROUP.get(LIST_INDEX);
            String listUrl = "https://music.163.com/#/playlist?id=" + playList.getListId();
            GuiConfirmOpenLink openLink = new GuiConfirmOpenLink(this, listUrl, 31102009, true);
            mc.displayGuiScreen(openLink);
            return;
        }

        if (button.id == 7) {
            NetEaseMusicList playList = MUSIC_LIST_GROUP.get(LIST_INDEX);
            if (MusicManger.removeSingleList(playList.getListId())) {
                LIST_INDEX = 0;
                guiMusicList = new GuiMusicList(this);
            }
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (id == 31102009) {
            if (result) {
                try {
                    NetEaseMusicList playList = MUSIC_LIST_GROUP.get(LIST_INDEX);
                    URI uri = new URI("https://music.163.com/#/playlist?id=" + playList.getListId());
                    this.openWebLink(uri);
                } catch (URISyntaxException ignore) {
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isMusicListEmpty) {
            return;
        }
        musicListField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (isMusicListEmpty) {
            return;
        }
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        guiMusicList.handleMouseInput(mouseX, mouseY);
        guiMusicListGroup.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        if (isMusicListEmpty) {
            return;
        }
        if (Character.isDigit(c) || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_DELETE
                || keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT
                || GuiScreen.isKeyComboCtrlA(keyCode) || GuiScreen.isKeyComboCtrlC(keyCode)
                || GuiScreen.isKeyComboCtrlV(keyCode) || GuiScreen.isKeyComboCtrlX(keyCode)) {
            musicListField.textboxKeyTyped(c, keyCode);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public EntityPortableAudio getAudio() {
        return audio;
    }

    private static class VolumeButton extends GuiButton {
        public float volume = 1.0F;
        public boolean pressed;

        public VolumeButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 100, 20, "");
            this.volume = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.RECORDS);
        }

        @Override
        public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            this.mouseDragged(mc, mouseX, mouseY);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawRect(x, y + 10, x + width, y + 11, 0xffafb1b3);
            Gui.drawRect(x, y + 10, x + (int) (volume * width), y + 11, 0xffaf0000);
            Gui.drawRect(x + (int) (volume * width) - 6, y + 7, x + (int) (volume * width), y + 14, 0xffafb1b3);
        }

        @Override
        protected void mouseDragged(@Nonnull Minecraft mc, int mouseX, int mouseY) {
            if (this.pressed) {
                this.volume = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
                this.volume = MathHelper.clamp(this.volume, 0.05F, 1.0F);
                mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, this.volume);
                mc.gameSettings.saveOptions();
            }
        }

        @Override
        public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY) {
            boolean isInRange = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (isInRange) {
                this.volume = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
                this.volume = MathHelper.clamp(this.volume, 0.05F, 1.0F);
                mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, this.volume);
                mc.gameSettings.saveOptions();
                this.pressed = true;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void playPressSound(@Nonnull SoundHandler soundHandlerIn) {
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY) {
            if (this.pressed) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            this.pressed = false;
        }
    }
}
