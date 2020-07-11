package com.github.tartaricacid.touhoulittlemaid.config;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Config(modid = TouhouLittleMaid.MOD_ID, name = "TouhouLittleMaid")
public class GeneralConfig {
    @Config.LangKey("config.touhou_little_maid.maid_config")
    @Config.Name("MaidConfig")
    public static MaidConfig MAID_CONFIG = new MaidConfig();

    @Config.LangKey("config.touhou_little_maid.vanilla_config")
    @Config.Name("VanillaConfig")
    public static VanillaConfig VANILLA_CONFIG = new VanillaConfig();

    @Config.LangKey("config.touhou_little_maid.mob_config")
    @Config.Name("MobConfig")
    public static MobConfig MOB_CONFIG = new MobConfig();

    @Config.LangKey("config.touhou_little_maid.misc_config")
    @Config.Name("MiscConfig")
    public static MiscellaneousConfig MISC_CONFIG = new MiscellaneousConfig();

    @Config.LangKey("config.touhou_little_maid.gashapon_config")
    @Config.Name("GashaponConfig")
    public static GashaponConfig GASHAPON_CONFIG = new GashaponConfig();

    @Config.LangKey("config.touhou_little_maid.music_config")
    @Config.Name("MusicConfig")
    public static MusicConfig MUSIC_CONFIG = new MusicConfig();

    public static class MaidConfig {
        @Config.Comment("The item that can tamed maid")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_tamed_item")
        @Config.Name("MaidTamedItem")
        public String maidTamedItem = "minecraft:cake";

        @Config.Comment("The item that can temptation maid")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_temptation_item")
        @Config.Name("MaidTemptationItem")
        public String maidTemptationItem = "minecraft:cake";

        @Config.Comment("Get number of ticks, at least during which the maid will be silent.")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_talk_interval")
        @Config.Name("MaidTalkInterval")
        @Config.RangeInt(min = 20)
        public int maidTalkInterval = 120;

        @Config.Comment("Get number of count, at least during which the maid will be silent in pickup item.")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_pickup_sound_interval")
        @Config.Name("MaidPickupSoundInterval")
        @Config.RangeInt(min = 1)
        public int maidPickupSoundInterval = 5;

        @Config.Comment("Get number of ticks, at least during which the maid will be silent in hurt.")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_hurt_sound_interval")
        @Config.Name("MaidHurtSoundInterval")
        @Config.RangeInt(min = 20)
        public int maidHurtSoundInterval = 120;

        @Config.RequiresMcRestart
        @Config.Comment("Decide which tasks are not enabled.")
        @Config.LangKey("config.touhou_little_maid.maid_config.enabled_tasks")
        @Config.Name("EnabledTasks")
        public Map<String, Boolean> enabledTasks = new HashMap<>();

        @Config.Comment("Maid cannot switch models freely.")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_cannot_change_model")
        @Config.Name("MaidCannotChangeModel")
        public boolean maidCannotChangeModel = false;

        @Config.Comment("The maximum number of maids the player own.")
        @Config.LangKey("config.touhou_little_maid.maid_config.owner_max_maid_num")
        @Config.Name("OwnerMaxMaidNum")
        @Config.RangeInt(min = 0)
        public int ownerMaxMaidNum = Integer.MAX_VALUE;
    }

    public static class VanillaConfig {
        @Config.Comment("Whether to replace the vanilla xp orb texture with the touhou project's point items.")
        @Config.LangKey("config.touhou_little_maid.vanilla_config.change_xp_texture")
        @Config.Name("ChangeXPTexture")
        public boolean changeXPTexture = false;

        @Config.Comment("Whether to replace the vanilla slime model with the yukkuri.")
        @Config.LangKey("config.touhou_little_maid.vanilla_config.change_slime_model")
        @Config.Name("ChangeSlimeTexture")
        public boolean changeSlimeModel = false;

        @Config.Comment("Whether to replace the vanilla totem model with the life point.")
        @Config.LangKey("config.touhou_little_maid.vanilla_config.change_totem_model")
        @Config.Name("ChangeTotemTexture")
        public boolean changeTotemModel = false;
    }

    public static class MobConfig {
        @Config.Comment("Maid Fairy's Power Point")
        @Config.LangKey("config.touhou_little_maid.mob_config.maid_fairy_power_point")
        @Config.Name("MaidFairyPowerPoint")
        @Config.RangeDouble(min = 0, max = 5)
        public double maidFairyPowerPoint = 0.16;

        @Config.RequiresMcRestart
        @Config.Comment("Maid Fairy's Spawn Probability (Zombie Is 100, Enderman Is 10)")
        @Config.LangKey("config.touhou_little_maid.mob_config.maid_fairy_spawn_probability")
        @Config.Name("MaidFairySpawnProbability")
        @Config.RangeInt(min = 0)
        public int maidFairySpawnProbability = 70;

        @Config.Comment("Rinnosuke's Power Point")
        @Config.LangKey("config.touhou_little_maid.mob_config.rinnosuke_power_point")
        @Config.Name("RinnosukePowerPoint")
        @Config.RangeDouble(min = 0, max = 5)
        public double rinnosukePowerPoint = 0.64;

        @Config.RequiresMcRestart
        @Config.Comment("Rinnosuke's Spawn Probability (Zombie Is 100, Enderman Is 10)")
        @Config.LangKey("config.touhou_little_maid.mob_config.rinnosuke_spawn_probability")
        @Config.Name("RinnosukeSpawnProbability")
        @Config.RangeInt(min = 0)
        public int rinnosukeSpawnProbability = 30;
    }

    public static class MiscellaneousConfig {
        @Config.Comment("Shrine Lamp Effect Cost (Power Point/Per Hour)")
        @Config.LangKey("config.touhou_little_maid.misc_config.shrine_lamp.effect_cost")
        @Config.Name("ShrineLampEffectCost")
        @Config.RangeDouble(min = 0)
        public double shrineLampEffectCost = 0.9;

        @Config.Comment("Shrine Lamp Max Storage Power Point")
        @Config.LangKey("config.touhou_little_maid.misc_config.shrine_lamp.max_storage")
        @Config.Name("ShrineLampMaxStorage")
        @Config.RangeDouble(min = 0)
        public double shrineLampMaxStorage = 100.0;

        @Config.Comment("Loss Power Point After Player Death")
        @Config.LangKey("config.touhou_little_maid.misc_config.player_death_loss_power_point")
        @Config.Name("PlayerDeathLossPowerPoint")
        @Config.RangeDouble(min = 0, max = 5)
        public double playerDeathLossPowerPoint = 1.0;

        @Config.Comment("Only Creative Player Can Ride Broom")
        @Config.LangKey("config.touhou_little_maid.misc_config.creative_player_can_ride_broom")
        @Config.Name("CreativePlayerCanRideBroom")
        public boolean creativePlayerCanRideBroom = true;

        @Config.Comment("Power Point HUD Position X")
        @Config.LangKey("config.touhou_little_maid.misc_config.power_point_hud_x")
        @Config.Name("PowerPointHudX")
        @Config.RangeDouble(min = 0, max = 1)
        public double PowerPointHudX = 0;

        @Config.Comment("Power Point HUD Position Y")
        @Config.LangKey("config.touhou_little_maid.misc_config.power_point_hud_y")
        @Config.Name("PowerPointHudY")
        @Config.RangeDouble(min = 0, max = 1)
        public double PowerPointHudY = 0;

        @Config.Comment("Override the render of Custom NPC mod")
        @Config.LangKey("config.touhou_little_maid.misc_config.override_npc_render")
        @Config.Name("OverrideNpcRender")
        @Config.RequiresMcRestart
        public boolean overrideNpcRender = true;

        @Config.Comment("Only creative player can change chair model")
        @Config.LangKey("config.touhou_little_maid.misc_config.chair_cannot_change_model")
        @Config.Name("ChairCannotChangeModel")
        public boolean chairCannotChangeModel = false;

        @Config.Comment("Only creative player can destroy chair")
        @Config.LangKey("config.touhou_little_maid.misc_config.chair_cannot_be_destroied")
        @Config.Name("ChairCannotBeDestroied")
        public boolean chairCannotBeDestroied = false;

        @Config.Comment("Will the danmaku be affected by the fluid?")
        @Config.LangKey("config.touhou_little_maid.misc_config.danmaku_affected_by_fluid")
        @Config.Name("DanmakuAffectedByFluid")
        public boolean danmakuAffectedByFluid = true;

        @Config.Comment("Whether to give guide book when entering the game for the first time?")
        @Config.LangKey("config.touhou_little_maid.misc_config.give_guide_book_first")
        @Config.Name("GiveGuideBookFirst")
        public boolean giveGuideBookFirst = true;
    }

    public static class GashaponConfig {
        @Config.Comment("The item that can purchase for gashapon")
        @Config.LangKey("config.touhou_little_maid.gashapon_config.purchase_gashapon_item")
        @Config.Name("PurchaseGashaponItem")
        public String purchaseGashaponItem = "minecraft:emerald";

        @Config.Comment("Gashapon's purchase price")
        @Config.LangKey("config.touhou_little_maid.gashapon_config.purchase_gashapon_price")
        @Config.Name("PurchaseGashaponPrice")
        @Config.RangeInt(min = 1)
        public int purchaseGashaponPrice = 2;

        @Config.Comment("UR Level Gashapon's Weights")
        @Config.LangKey("config.touhou_little_maid.gashapon_config.gashapon_weights_5")
        @Config.Name("GashaponWeights5")
        @Config.RangeInt(min = 0)
        public int gashaponWeights5 = 5;

        @Config.Comment("SSR Level Gashapon's Weights")
        @Config.LangKey("config.touhou_little_maid.gashapon_config.gashapon_weights_4")
        @Config.Name("GashaponWeights4")
        @Config.RangeInt(min = 0)
        public int gashaponWeights4 = 10;

        @Config.Comment("SR Level Gashapon's Weights")
        @Config.LangKey("config.touhou_little_maid.gashapon_config.gashapon_weights_3")
        @Config.Name("GashaponWeights3")
        @Config.RangeInt(min = 0)
        public int gashaponWeights3 = 20;

        @Config.Comment("R Level Gashapon's Weights")
        @Config.LangKey("config.touhou_little_maid.gashapon_config.gashapon_weights_2")
        @Config.Name("GashaponWeights2")
        @Config.RangeInt(min = 0)
        public int gashaponWeights2 = 30;

        @Config.Comment("N Level Gashapon's Weights")
        @Config.LangKey("config.touhou_little_maid.gashapon_config.gashapon_weights_1")
        @Config.Name("GashaponWeights1")
        @Config.RangeInt(min = 0)
        public int gashaponWeights1 = 50;
    }

    public static class MusicConfig {
        @Config.Comment("Players who match this reg can send music messages")
        @Config.LangKey("config.touhou_little_maid.music_config.player_name_reg")
        @Config.Name("PlayerNameReg")
        public String playerNameReg = ".*";

        @Config.Comment("Receive music information from the server?")
        @Config.LangKey("config.touhou_little_maid.music_config.receive_music")
        @Config.Name("ReceiveMusic")
        public Boolean receiveMusic = true;
    }

    /**
     * 用于 GUI 界面配置调节的保存
     */
    @Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(TouhouLittleMaid.MOD_ID)) {
                ConfigManager.sync(TouhouLittleMaid.MOD_ID, Config.Type.INSTANCE);
                TouhouLittleMaid.LOGGER.info("Touhou Little Mod's Config Already Change.");
            }
        }
    }
}
