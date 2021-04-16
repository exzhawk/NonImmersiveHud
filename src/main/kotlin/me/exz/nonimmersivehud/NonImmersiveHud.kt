package me.exz.nonimmersivehud

import me.exz.nonimmersivehud.Reference.MOD_ID
import me.exz.nonimmersivehud.Reference.LANG_ADAPTER
import me.exz.nonimmersivehud.Reference.MOD_NAME
import me.exz.nonimmersivehud.Reference.VERSION
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, modLanguageAdapter = LANG_ADAPTER)
object NonImmersiveHud {


    @Suppress("unused", "UNUSED_PARAMETER")
    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    fun preInit(event: FMLPreInitializationEvent) {
        WebServer.startServer()
        if (Loader.isModLoaded("theoneprobe")) {
            MinecraftForge.EVENT_BUS.register(TOPHandler::class.java)
        }
        if (Loader.isModLoaded("waila")) {
            MinecraftForge.EVENT_BUS.register(WailaHandler::class.java)
        }
    }


}
