package me.exz.nonimmersivehud


import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WebSocketAdapter


class WebSocket : WebSocketAdapter() {
    companion object {
        var nbtWebSocketClients = mutableListOf<WebSocket>()
        fun broadcast(string: String) {
            println(string)
            for (nbtWebSocketClient in nbtWebSocketClients) {
                if (nbtWebSocketClient.session.isOpen) {
                    nbtWebSocketClient.sendWebSocketText(string)
                }
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
    }

    override fun onWebSocketError(cause: Throwable?) {
        super.onWebSocketError(cause)
        println("websocket error")
    }

    override fun onWebSocketText(string: String) {
        if (isConnected) {
            println("Received message $string")
        }
    }

    fun sendWebSocketText(string: String) {
        remote.sendStringByFuture(string)
    }
}
