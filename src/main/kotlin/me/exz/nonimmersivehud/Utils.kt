package me.exz.nonimmersivehud

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.event.ClickEvent
import java.net.Inet4Address
import java.net.NetworkInterface


class Utils {
    companion object {

        fun sendUrlToChat(entity: EntityPlayerSP, hud: String) {
            val parent = TextComponentString("Use Non-immersive %s at".format(hud))
            val addresses = mutableListOf<String>()
            if (host == "0.0.0.0") {
                val e = NetworkInterface.getNetworkInterfaces()
                while (e.hasMoreElements()) {
                    val ee = e.nextElement().inetAddresses
                    while (ee.hasMoreElements()) {
                        val inetAddress = ee.nextElement()
                        if (inetAddress is Inet4Address) {
                            addresses.add(inetAddress.hostAddress)
                        }
                    }
                }
            } else {
                addresses.add(host)
            }
            for (address in addresses) {
                val url = "http://%s:%s/%s.html".format(address, port, hud)
                val prefixComponent = TextComponentString(" ")
                val urlComponent = TextComponentString(url)
                urlComponent.style.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
                urlComponent.style.color = TextFormatting.BLUE
                prefixComponent.appendSibling(urlComponent)
                parent.appendSibling(prefixComponent)
            }
            entity.sendMessage(parent)
        }
        fun sendFailedToChat(entity:EntityPlayerSP){
            entity.sendMessage(TextComponentString("Failed to start server! Check if port is occupied."))
        }
    }
}
