package me.lunathefox20

import me.lunathefox20.command.DistanceCalculator
import net.weavemc.loader.api.ModInitializer
import net.weavemc.loader.api.command.CommandBus
import org.apache.logging.log4j.LogManager

class DistanceMain : ModInitializer {
    private val logger = LogManager.getLogger(DistanceCalculator::class.java)

    override fun preInit() {
        // Log the "ready" message in preInit()
        logger.info("DistanceCalculator is loading...")

        try {
            CommandBus.register(DistanceCalculator())
            logger.info("DistanceCalculator is ready!")
        } catch (e: Exception) {
            logger.error("An error occurred during initialization:", e)
        }
    }
}
