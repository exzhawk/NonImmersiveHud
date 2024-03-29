package me.exz.nonimmersivehud

import com.google.gson.Gson
import mcp.mobius.waila.api.SpecialChars
import mcp.mobius.waila.api.SpecialChars.patternRender
import mcp.mobius.waila.api.event.WailaTooltipEvent
import mcp.mobius.waila.overlay.RayTracing
import me.exz.nonimmersivehud.NonImmersiveHud.logger
import me.exz.nonimmersivehud.WebServer.serverStartFailed
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


//@EventBusSubscriber
class WailaHandler {
    companion object {
        init {
            logger.info("WailaHandler init")
        }

        @JvmStatic
        private var lastTooltipJson = ""

        @JvmStatic
        private val gson = Gson()

        private var currentTip: List<String> = ArrayList()

        const val hud = "waila"

        @Suppress("unused")
        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun onClientTick(event: TickEvent.ClientTickEvent) {
            if (event.phase == TickEvent.Phase.END) {
                val target = RayTracing.instance().target
                if (target == null || (target.typeOfHit != RayTraceResult.Type.BLOCK && target.typeOfHit != RayTraceResult.Type.ENTITY)) {
                    val tooltipTexts = null
                    val tooltipJson = gson.toJson(tooltipTexts)
                    if (tooltipJson != lastTooltipJson) {
                        WebSocket.broadcast(tooltipJson, hud)
                        lastTooltipJson = tooltipJson
                    }
                    return
                }

                val tooltipTexts = mutableListOf<Any>()
                for (linesInTip in currentTip) {
                    val lines = linesInTip.split(SpecialChars.WailaSplitter)
                    for (line in lines) {
                        val renderMatcher = patternRender.matcher(line)
                        if (renderMatcher.find()) {
                            val renderName = renderMatcher.group("name")
                            val params = renderMatcher.group("args").split("\\+,".toRegex())
                            if (renderName == "waila.health") {
                                tooltipTexts.add(
                                    mapOf(
                                        "id" to "waila:render_health",
                                        "health" to params[1].toFloat(),
                                        "max" to params[2].toFloat()
                                    )
                                )
                            }
                        } else {
                            tooltipTexts.add(line)
                        }
                    }
                }
                val tooltipJson = gson.toJson(tooltipTexts)
                if (tooltipJson != lastTooltipJson) {
                    WebSocket.broadcast(tooltipJson, hud)
                    lastTooltipJson = tooltipJson
                }
            }
        }

        @JvmStatic
        private var lastJoinedWorldHashCode: Int = 0

        @Suppress("unused")
        @SubscribeEvent
        @JvmStatic
        @SideOnly(Side.CLIENT)
        fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
            val entity = event.entity
            if (entity is EntityPlayerSP) {
                if (event.world.hashCode() != lastJoinedWorldHashCode) {
                    lastJoinedWorldHashCode = event.world.hashCode()
                    if (serverStartFailed) {
                        Utils.sendFailedToChat(entity)
                    } else {
                        Utils.sendUrlToChat(entity, hud)
                    }
                }
            }
        }


        @Suppress("unused")
        @JvmStatic
        @SubscribeEvent
        fun onWailaTooltip(event: WailaTooltipEvent) {
            currentTip = event.currentTip
        }
    }

}
