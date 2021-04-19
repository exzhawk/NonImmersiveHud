package me.exz.nonimmersivehud

import com.google.gson.JsonObject
import io.github.drmanganese.topaddons.elements.ElementTankGauge
import mcjty.theoneprobe.api.IElement


class TOPAddonHandler {
    companion object {
        init {
            println("TOPAddonHandler init")
        }

        fun updateElementJson(src: IElement, element: JsonObject) {
            when (src) {
                is ElementTankGauge -> {
                    element.addProperty("type", "TankGauge")
                    element.addProperty(
                        "amount",
                        TOPHandler.getField(src, "amount", ElementTankGauge::class) as Int
                    )
                    element.addProperty(
                        "capacity",
                        TOPHandler.getField(src, "capacity", ElementTankGauge::class) as Int
                    )
                    element.addProperty(
                        "tankName",
                        TOPHandler.getField(src, "tankName", ElementTankGauge::class) as String
                    )
                    element.addProperty(
                        "fluidName",
                        TOPHandler.getField(src, "fluidName", ElementTankGauge::class) as String
                    )
                    element.addProperty(
                        "suffix",
                        TOPHandler.getField(src, "suffix", ElementTankGauge::class) as String
                    )
                    element.addProperty(
                        "color1",
                        Integer.toHexString((TOPHandler.getField(src, "color1", ElementTankGauge::class) as Int))
                    )
                    element.addProperty(
                        "color2",
                        Integer.toHexString((TOPHandler.getField(src, "color2", ElementTankGauge::class) as Int))
                    )
                }
            }
        }
    }

}
