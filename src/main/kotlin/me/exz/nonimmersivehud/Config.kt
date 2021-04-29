@file:JvmName("Config")
@file:Config(modid = MOD_ID)

package me.exz.nonimmersivehud

import me.exz.nonimmersivehud.Reference.MOD_ID
import net.minecraftforge.common.config.Config

@Config.Comment("Webserver listening host")
@JvmField
var host = "0.0.0.0"

@Config.Comment("Webserver listening port")
@Config.RangeInt(min = 1, max = 65535)
@JvmField
var port = 18082

@Config.Comment("Disable in-game HUD")
@JvmField
var disableInGameHUD = true
