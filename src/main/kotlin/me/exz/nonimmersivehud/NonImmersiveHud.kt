package me.exz.nonimmersivehud

import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(NonImmersiveHud.ID)
object NonImmersiveHud {
    const val ID: String = "nonimmersivehud"
    val LOGGER: Logger = LogManager.getLogger()

    init {
        MOD_BUS.addListener(::setup)
        MOD_BUS.addListener(::doClientStuff)
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.spec)
    }

    private fun setup(event: FMLCommonSetupEvent) {
    }

    private fun doClientStuff(event: FMLClientSetupEvent) {
        WebServer.startServer()
    }


}
