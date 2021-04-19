package me.exz.nonimmersivehud

import com.google.gson.*
import mcjty.theoneprobe.api.IElement
import mcjty.theoneprobe.api.IIconStyle
import mcjty.theoneprobe.api.IProgressStyle
import mcjty.theoneprobe.apiimpl.ProbeInfo
import mcjty.theoneprobe.apiimpl.client.ElementTextRender
import mcjty.theoneprobe.apiimpl.elements.*
import mcjty.theoneprobe.rendering.OverlayRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.tuple.Pair
import java.lang.reflect.Type
import kotlin.reflect.KClass


//@Mod.EventBusSubscriber
class TOPHandler {
    companion object {
        init {
            println("TOPHandler init")
        }

//        val lastPair = ReflectionHelper.findField(OverlayRenderer::class.java, "lastPair")

        var lastProbeInfo = ProbeInfo()
        val gson = GsonBuilder().registerTypeHierarchyAdapter(IElement::class.java, ElementSerializer()).create()
        val stylifyStringMethod = getStaticMethod(ElementTextRender::class, "stylifyString", String::class.java)
        var lastJson = ""
        val isTOPAddonLoaded = Loader.isModLoaded("topaddons")

        const val hud = "top"

        @Suppress("unused", "UNUSED_PARAMETER")
        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun renderGameOverlayEvent(event: RenderGameOverlayEvent.Pre) {
            val mouseOver = Minecraft.getMinecraft().objectMouseOver
            if (mouseOver == null || (mouseOver.typeOfHit != RayTraceResult.Type.ENTITY && mouseOver.typeOfHit != RayTraceResult.Type.BLOCK)) {
                val json = gson.toJson(null)
                if (json != lastJson) {
                    WebSocket.broadcast(json, hud)
                    lastJson = json
                }
                return
            }
            val lastPairField = OverlayRenderer::class.java.getDeclaredField("lastPair")
            lastPairField.isAccessible = true
            val lastPair = lastPairField.get(null)
            if (lastPair != null) {
                val probeInfo = (lastPair as Pair<*, *>).right as ProbeInfo
                if (probeInfo != lastProbeInfo) {
                    lastProbeInfo = probeInfo
                    val json = gson.toJson(lastProbeInfo)
                    if (json != lastJson) {
                        WebSocket.broadcast(json, hud)
                        lastJson = json
                    }
                }
            }
        }

        fun getField(instance: Any, fieldName: String, clazz: KClass<*>? = null): Any? {
            val cls: KClass<*> = clazz ?: instance::class
            val field = cls.java.getDeclaredField(fieldName)
            field.isAccessible = true
            return field.get(instance)
        }

        fun getStaticMethod(clazz: KClass<*>, methodName: String, vararg params: Class<*>): (Any?) -> Any {
            val method = clazz.java.getDeclaredMethod(methodName, *params)
            method.isAccessible = true
            return { a -> method.invoke(null, a) }

        }

        @SubscribeEvent
        @JvmStatic
        @SideOnly(Side.CLIENT)
        fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
            val entity = event.entity
            if (entity is EntityPlayerSP) {
                Utils.sendUrlToChat(entity, hud)
            }
        }

    }

    class ElementSerializer : JsonSerializer<IElement> {
        override fun serialize(src: IElement, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            val element = JsonObject()
            when (src) {
                is ElementText -> {
                    element.addProperty("type", "Text")
                    val text = getField(src, "text")
                    element.addProperty("text", stylifyStringMethod(text) as String)
                }
                is ElementItemStack -> {
                    element.addProperty("type", "ItemStack")
                }
                is ElementProgress -> {
                    element.addProperty("type", "Progress")
                    element.addProperty("current", getField(src, "current") as Long)
                    element.addProperty("max", getField(src, "max") as Long)
                    val progressStyle = getField(src, "style", ElementProgress::class) as IProgressStyle
                    val style = JsonObject()
                    style.addProperty("borderColor", Integer.toHexString(progressStyle.borderColor))
                    style.addProperty("backgroundColor", Integer.toHexString(progressStyle.backgroundColor))
                    style.addProperty("filledColor", Integer.toHexString(progressStyle.filledColor))
                    style.addProperty("alternatefilledColor", Integer.toHexString(progressStyle.alternatefilledColor))
                    style.addProperty("showText", progressStyle.isShowText)
                    style.addProperty("prefix", progressStyle.prefix)
                    style.addProperty("suffix", progressStyle.suffix)
                    style.addProperty("width", progressStyle.width)
                    style.addProperty("height", progressStyle.height)
                    style.addProperty("lifeBar", progressStyle.isLifeBar)
                    style.addProperty("armorBar", progressStyle.isArmorBar)
                    element.add("style", style)
                }
                is ElementHorizontal -> {
                    element.addProperty("type", "Horizontal")
                    element.add("children", context.serialize(getField(src, "children", AbstractElementPanel::class)))
                    val borderColor = getField(src, "borderColor", AbstractElementPanel::class)
                    if (borderColor != null) {
                        element.addProperty(
                            "borderColor",
                            Integer.toHexString(borderColor as Int)
                        )
                    }
                }
                is ElementVertical -> {
                    element.addProperty("type", "Vertical")
                    element.add("children", context.serialize(getField(src, "children", AbstractElementPanel::class)))
                    val borderColor = getField(src, "borderColor", AbstractElementPanel::class)
                    if (borderColor != null) {
                        element.addProperty(
                            "borderColor",
                            Integer.toHexString(borderColor as Int)
                        )
                    }
                }
                is ElementEntity -> {
                    element.addProperty("type", "Entity")
                }
                is ElementIcon -> {
                    element.addProperty("type", "Icon")
                    element.addProperty(
                        "icon",
                        (getField(src, "icon", ElementIcon::class) as ResourceLocation).toString()
                    )
                    element.addProperty("u", getField(src, "u", ElementIcon::class) as Int)
                    element.addProperty("v", getField(src, "v", ElementIcon::class) as Int)
                    element.addProperty("w", getField(src, "w", ElementIcon::class) as Int)
                    element.addProperty("h", getField(src, "h", ElementIcon::class) as Int)
                    val iconStyle = getField(src, "style", ElementIcon::class) as IIconStyle
                    element.addProperty("tw", iconStyle.textureWidth)
                    element.addProperty("th", iconStyle.textureHeight)
                }
                is ElementItemLabel -> {
                    element.addProperty("type", "ItemLabel")
                    val itemStack = getField(src, "itemStack") as ItemStack
                    val label = itemStack.displayName
                    element.addProperty("text", stylifyStringMethod(label) as String)
                }
                else -> {
                    if (isTOPAddonLoaded) {
                        TOPAddonHandler.updateElementJson(src, element)
                    }

                }
            }
            return element
        }
    }
}
