package me.lunathefox20

import me.lunathefox20.command.DistanceCalculator
import net.weavemc.loader.api.ModInitializer
import net.weavemc.loader.api.command.CommandBus

class DistanceMain : ModInitializer {
    override fun preInit() {
        CommandBus.register(DistanceCalculator())
    }
}