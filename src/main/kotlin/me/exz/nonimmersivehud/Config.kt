package me.exz.nonimmersivehud

import net.minecraftforge.common.ForgeConfigSpec


object Config {
    @JvmStatic
    val spec: ForgeConfigSpec

    @JvmStatic
    val host: ForgeConfigSpec.ConfigValue<String>

    @JvmStatic
    val port: ForgeConfigSpec.IntValue

    init {
        val builder = ForgeConfigSpec.Builder()
        builder.comment("web server settings").push("webserver")
        host = builder.comment("listening host").define("host", "0.0.0.0")
        port = builder.comment("listening port").defineInRange("port", 18082, 1, 65535)
        builder.pop()
        spec = builder.build()
    }
}
