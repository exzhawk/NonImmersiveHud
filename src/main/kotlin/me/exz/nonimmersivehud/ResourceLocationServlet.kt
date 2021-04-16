package me.exz.nonimmersivehud

import com.google.common.io.ByteStreams
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import java.io.FileNotFoundException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class ResourceLocationServlet : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val requestPath = req.pathInfo.substring(1)
        val resourceLocation = ResourceLocation(requestPath)
        try {
            val resourceFile = Minecraft.getMinecraft().resourceManager.getResource(resourceLocation)
            @Suppress("UnstableApiUsage")
            ByteStreams.copy(resourceFile.inputStream, resp.outputStream)
        } catch (e: FileNotFoundException) {
            resp.status = HttpServletResponse.SC_NOT_FOUND
        }
    }
}
