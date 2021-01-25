package me.exz.nonimmersivehud

import com.google.gson.Gson
import mcp.mobius.waila.api.SpecialChars
import mcp.mobius.waila.api.SpecialChars.patternRender
import mcp.mobius.waila.api.event.WailaTooltipEvent
import mcp.mobius.waila.overlay.RayTracing
import net.minecraft.util.math.RayTraceResult

import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent


@EventBusSubscriber
class WailaHandler {
    companion object {

        @JvmStatic
        private var lastTooltipJson = ""

        @JvmStatic
        private val gson = Gson()

        private var currentTip: List<String> = ArrayList()

        @Suppress("unused")
        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun onClientTick(event: TickEvent.ClientTickEvent) {
            if (event.phase == TickEvent.Phase.END) {
//                val tooltip = WailaTickHandler.instance().tooltip
                val target = RayTracing.instance().target
                if (target == null || (target.typeOfHit != RayTraceResult.Type.BLOCK && target.typeOfHit != RayTraceResult.Type.ENTITY)) {
                    val tooltipTexts = null
                    val tooltipJson = gson.toJson(tooltipTexts)
                    if (tooltipJson != lastTooltipJson) {
                        WebSocket.broadcast(tooltipJson)
                        lastTooltipJson = tooltipJson
//                        println(tooltipJson)
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
                    WebSocket.broadcast(tooltipJson)
                    lastTooltipJson = tooltipJson
//                    println(tooltipJson)
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
