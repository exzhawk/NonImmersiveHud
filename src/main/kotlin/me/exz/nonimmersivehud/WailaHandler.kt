package me.exz.nonimmersivehud

import com.google.gson.Gson
import mcp.mobius.waila.api.RenderableTextComponent
import mcp.mobius.waila.overlay.RayTracing
import mcp.mobius.waila.overlay.WailaTickHandler
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber


@EventBusSubscriber(modid = NonImmersiveHud.ID, value = [Dist.CLIENT])
object WailaHandler {
    private var lastTooltipJson = ""
    private val gson = Gson()

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onClientTick(event: ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            val tooltip = WailaTickHandler.instance().tooltip
            val target = RayTracing.INSTANCE.target
            if (target == null || (target.type != RayTraceResult.Type.BLOCK && target.type != RayTraceResult.Type.ENTITY)) {
                val tooltipTexts = null;
                val tooltipJson = gson.toJson(tooltipTexts)
                if (tooltipJson != lastTooltipJson) {
                    WebSocket.broadcast(tooltipJson)
                    lastTooltipJson = tooltipJson
                    println(tooltipJson)
                }
                return
            }

            if (tooltip != null) {

                val tooltipTexts = mutableListOf<Any>()
                for (line in tooltip.lines) {
                    val component = line.component
                    if (component is RenderableTextComponent) {
                        for (container in component.renderers) {
                            val id = container.id
                            val data = container.data
                            if (id.toString() == "waila:render_health") {
                                val health: Float = data.getFloat("health")
                                val maxHealth: Float = data.getFloat("max")
                                tooltipTexts.add(
                                    mapOf(
                                        "id" to "waila:render_health",
                                        "health" to health,
                                        "max" to maxHealth
                                    )
                                )
                            }
                        }
                    } else {
                        tooltipTexts.add(component.string)
                    }
                }
                val tooltipJson = gson.toJson(tooltipTexts)
                if (tooltipJson != lastTooltipJson) {
                    WebSocket.broadcast(tooltipJson)
                    lastTooltipJson = tooltipJson
                    println(tooltipJson)
                }
            }
        }
    }


}
