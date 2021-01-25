package me.exz.nonimmersivehud



import net.minecraftforge.fml.common.FMLModContainer
import net.minecraftforge.fml.common.ILanguageAdapter
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.relauncher.Side
import java.lang.reflect.Field
import java.lang.reflect.Method

class KotlinAdapter : ILanguageAdapter {

    override fun supportsStatics() = false

    override fun setProxy(target: Field?, proxyTarget: Class<*>, proxy: Any?) {
        target?.set(proxyTarget.kotlin.objectInstance, proxy)
    }

//    override fun getNewInstance(mod: FMLModContainer?, modClass: Class<*>?, loader: ClassLoader?, factory: Method?): Any? {
//        return modClass?.getField("INSTANCE")?.get(null)
//    }

    override fun getNewInstance(container: FMLModContainer, objectClass: Class<*>, classLoader: ClassLoader, factoryMarkedAnnotation: Method?): Any {
        return objectClass.kotlin.objectInstance ?: objectClass.newInstance()
    }
    override fun setInternalProxies(mod: ModContainer?, side: Side?, loader: ClassLoader?) = Unit
}
