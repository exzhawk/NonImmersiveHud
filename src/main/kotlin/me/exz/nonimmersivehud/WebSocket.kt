package me.exz.nonimmersivehud


import me.exz.nonimmersivehud.NonImmersiveHud.logger
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WebSocketAdapter


class WebSocket : WebSocketAdapter() {
    companion object {
        var nbtWebSocketClients = mutableListOf<WebSocket>()
        var lastString = mutableMapOf<String, String>()
        fun broadcast(string: String, hud: String) {
            lastString[hud] = string
            try {
                for (nbtWebSocketClient in nbtWebSocketClients) {
                    if (nbtWebSocketClient.session?.isOpen == true &&
                        nbtWebSocketClient.session?.upgradeRequest?.parameterMap?.get("hud")?.get(0) == hud
                    ) {
                        nbtWebSocketClient.sendWebSocketText(string)
                    }
                }
            } catch (e: NullPointerException) {
                logger.error("wtf???")
            }
        }
    }

    override fun onWebSocketClose(statusCode: Int, reason: String?) {
        super.onWebSocketClose(statusCode, reason)
        nbtWebSocketClients.remove(this)
    }

    override fun onWebSocketConnect(sess: Session?) {
        super.onWebSocketConnect(sess)
        nbtWebSocketClients.add(this)
        val hud = sess?.upgradeRequest?.parameterMap?.get("hud")?.get(0)
        if (hud != null) {
            this.sendWebSocketText(lastString.getOrDefault(hud, "null"))
        }
    }

    override fun onWebSocketError(cause: Throwable?) {
        super.onWebSocketError(cause)
        logger.info("websocket error")
    }

    override fun onWebSocketText(string: String) {
//        if (isConnected) {
//            println("Received message $string")
//        }
    }

    fun sendWebSocketText(string: String) {
        remote.sendStringByFuture(string)
    }
}
