package me.exz.nonimmersivehud.core

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion


@IFMLLoadingPlugin.Name("non immersive hud core")
@MCVersion("1.12.2")
class Plugin : IFMLLoadingPlugin {
    override fun getASMTransformerClass(): Array<String> {
        return arrayOf(Transformer::class.java.name)
    }

    override fun getModContainerClass(): String? {
        return null
    }

    override fun getSetupClass(): String? {
        return null
    }

    override fun injectData(data: Map<String, Any>) {}
    override fun getAccessTransformerClass(): String? {
        return null
    }
}
