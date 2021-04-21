package me.exz.nonimmersivehud

import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.resource.Resource
import org.eclipse.jetty.websocket.servlet.WebSocketServlet
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory
import kotlin.concurrent.thread


object WebServer {
    fun startServer() {
        val host = host
        val port = port
        thread(start = true) {
            print(String.format("Starting server, listening at %s:%s", host, port))
            val server = Server()
            val connector = ServerConnector(server)
            connector.host = host
            connector.port = port
            server.connectors = arrayOf<Connector>(connector)

            val context = ServletContextHandler(ServletContextHandler.SESSIONS)

            context.contextPath = "/"
            val uri = NonImmersiveHud::class.java.getResource("/assets/nonimmersivehud/static").toURI()
            context.baseResource = Resource.newResource(uri)
            context.welcomeFiles = arrayOf("index.html")
            val holderPwd = ServletHolder("default", DefaultServlet::class.java)
            holderPwd.setInitParameter("cacheControl", "max-age=0,public")
            holderPwd.setInitParameter("useFileMappedBuffer", "false")
            context.addServlet(holderPwd, "/")

            val wsHolder = ServletHolder(object : WebSocketServlet() {
                override fun configure(factory: WebSocketServletFactory) {
                    factory.register(WebSocket::class.java)
                }
            })
            context.addServlet(wsHolder, "/w")

            val resourceLocationHolder = ServletHolder("resourceLocation", ResourceLocationServlet::class.java)
            context.addServlet(resourceLocationHolder, "/resource/*")

            server.handler = context
            server.start()
            print("Started Server")
            server.join()
        }
    }
}
