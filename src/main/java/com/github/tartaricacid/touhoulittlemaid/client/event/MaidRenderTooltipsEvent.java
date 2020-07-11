package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidModelCoupon;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPhoto;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

/**
 * @author TartaricAcid
 * @date 2020/1/22 20:19
 **/
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class MaidRenderTooltipsEvent {
    private static final String MAID_ID = "touhou_little_maid:entity.passive.maid";

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.PostText event) {
        ItemStack stack = event.getStack();
        if (stack.getItem() == MaidItems.MAID_MODEL_COUPON && ItemMaidModelCoupon.hasModelData(stack)) {
            onRenderMaidTooltips(ItemMaidModelCoupon.getModelData(stack), event.getX(), event.getY(), null);
            return;
        }
        if (stack.getItem() == MaidItems.PHOTO && ItemPhoto.hasMaidNbtData(stack)) {
            onRenderMaidTooltips("", event.getX(), event.getY(), stack);
        }
    }

    private static void onRenderMaidTooltips(String modelId, int x, int y, @Nullable ItemStack photo) {
        x = x - 20;
        if (y < 65) {
            y = 65;
        }
        Minecraft mc = Minecraft.getMinecraft();
        RenderHelper.renderBackground(x, y, 60, 60);
        World world = mc.world;

        ScaledResolution res = new ScaledResolution(mc);
        double scaleW = mc.displayWidth / res.getScaledWidth_double();
        double scaleH = mc.displayHeight / res.getScaledHeight_double();
        GL11.glScissor((int) ((x - 62) * scaleW), (int) ((-y - 2) * scaleH + mc.displayHeight),
                (int) (64 * scaleW), (int) (64 * scaleH));

        GlStateManager.enableDepth();
        GlStateManager.color(1, 1, 1);
        GlStateManager.translate(x - 30, y - 5, 500);
        GlStateManager.scale(24, 24, -24);
        GlStateManager.rotate(190, 1, 0, 0);
        GlStateManager.rotate((world.getWorldTime() * 2) % 360, 0, -1, 0);

        float renderItemScale = 1.0f;
        GlStateManager.pushMatrix();

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        Entity entity;
        try {
            entity = EntityCacheUtil.ENTITY_CACHE.get(MAID_ID, () -> {
                Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(MAID_ID), world);
                if (e == null) {
                    return new EntityMaid(world);
                } else {
                    return e;
                }
            });
            if (entity instanceof EntityMaid) {
                EntityMaid maid = (EntityMaid) entity;
                clearMaidDataResidue(maid, true);
                maid.setModelId(modelId);
                renderItemScale = CustomResourcesLoader.MAID_MODEL.getModelRenderItemScale(modelId);
                if (photo != null) {
                    maid.readEntityFromNBT(ItemPhoto.getMaidNbtData(photo));
                    if (photo.hasDisplayName()) {
                        maid.setCustomNameTag(photo.getDisplayName());
                    }
                }
            }
        } catch (ExecutionException e) {
            return;
        }

        GlStateManager.enableColorMaterial();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        final boolean lightmapEnabled = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GlStateManager.scale(renderItemScale, renderItemScale, renderItemScale);
        Minecraft.getMinecraft().getRenderManager().setRenderShadow(false);
        Minecraft.getMinecraft().getRenderManager().renderEntity(entity,
                0, 0, 0, 0, 0, true);
        Minecraft.getMinecraft().getRenderManager().setRenderShadow(true);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.popMatrix();

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        if (lightmapEnabled) {
            GlStateManager.enableTexture2D();
        } else {
            GlStateManager.disableTexture2D();
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableBlend();
    }
}
