package me.exz.nonimmersivehud.core

import me.exz.nonimmersivehud.NonImmersiveHud.logger
import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.MethodNode
import java.nio.file.Files
import java.nio.file.Paths


data class MethodIdentity(val name: String, val desc: String)
class Transformer : IClassTransformer {
    companion object {
        var disableInGameHUD = true


        init {

            val configPath = Paths.get("config/nonimmersivehud.cfg")
            if (Files.exists(configPath)) {
                val configLines = Files.readAllLines(configPath)
                for (configLine in configLines) {
                    if (configLine.contains("B:disableInGameHUD=false")) {
                        disableInGameHUD = false
                        break
                    }
                }
            }
        }

        fun nullifyMethod(method: MethodNode) {
            logger.info("nullifying " + method.name + method.desc)
            method.instructions.insertBefore(method.instructions.first, InsnNode(Opcodes.RETURN))
            // not possible due to https://gitlab.ow2.org/asm/asm/-/issues/317784
//            val first = method.instructions.first
//            val instList = InsnList()
//            val labelA = LabelNode()
//            instList.add(labelA)
//            val lineA = LineNumberNode(1, labelA)
//            instList.add(lineA)
//            val getstatic = FieldInsnNode(Opcodes.GETFIELD, "me/exz/nonimmersivehud/Config", "disableInGameHUD", "Z")
//            instList.add(getstatic)
//            val ifeq = JumpInsnNode(Opcodes.IFEQ, first as LabelNode)
//            instList.add(ifeq)
//            val labelB = LabelNode()
//            instList.add(labelB)
//            val lineB = LineNumberNode(2, labelB)
//            instList.add(lineB)
//            val ret = InsnNode(Opcodes.RETURN)
//            instList.add(ret)
//            method.instructions.insertBefore(first, instList)

        }
    }


    override fun transform(name: String, transformedName: String, basicClass: ByteArray?): ByteArray? {
        if (!disableInGameHUD) {
            return basicClass
        }
        if (basicClass == null) {
            return null
        }

        val methodIdentity = when (transformedName) {
            "mcp.mobius.waila.overlay.OverlayRenderer" -> MethodIdentity(
                "renderOverlay",
                "(Lmcp/mobius/waila/overlay/Tooltip;)V"
            )
//            "mcjty.theoneprobe.rendering.OverlayRenderer" -> MethodIdentity(
//                "renderElements",
//                "(Lmcjty/theoneprobe/apiimpl/ProbeInfo;Lmcjty/theoneprobe/api/IOverlayStyle;DDLmcjty/theoneprobe/api/IElement;)V"
//            )
            "mcjty.theoneprobe.apiimpl.elements.ElementVertical" -> MethodIdentity(
                "render",
                "(II)V"
            )
            "mcjty.theoneprobe.rendering.RenderHelper"-> MethodIdentity(
                "drawThickBeveledBox",
                "(IIIIIIII)V"
            )
            else -> null
        }
        return if (methodIdentity == null) {
            basicClass
        } else {
            val node = ClassNode(Opcodes.ASM5)
            ClassReader(basicClass).accept(node, 0)
            for (method in node.methods) {
                if (method.name == methodIdentity.name && method.desc == methodIdentity.desc) {
                    nullifyMethod(method)
                }
            }
            val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)
            node.accept(writer)
            writer.toByteArray()
        }
//        when (transformedName) {
//            "mcp.mobius.waila.overlay.OverlayRenderer" -> {
//                val node = ClassNode(Opcodes.ASM5)
//                ClassReader(basicClass).accept(node, 0)
//                for (method in node.methods) {
//                    if (method.name == "renderOverlay" && method.desc == "(Lmcp/mobius/waila/overlay/Tooltip;)V") {
//                        nullifyMethod(method)
//                    }
//                }
//                val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)
//                node.accept(writer)
//                return writer.toByteArray()
//            }
//            "mcjty.theoneprobe.rendering.OverlayRenderer" -> {
//                val node = ClassNode(Opcodes.ASM5)
//                ClassReader(basicClass).accept(node, 0)
//                for (method in node.methods) {
//                    if (method.name == "renderElements" && method.desc == "(Lmcjty/theoneprobe/apiimpl/ProbeInfo;Lmcjty/theoneprobe/api/IOverlayStyle;DDLmcjty/theoneprobe/api/IElement;)V") {
//                        nullifyMethod(method)
//                    }
//                }
//                val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)
//                node.accept(writer)
//                return writer.toByteArray()
//            }
//            else -> {
//                return basicClass
//            }
//        }
    }


}
