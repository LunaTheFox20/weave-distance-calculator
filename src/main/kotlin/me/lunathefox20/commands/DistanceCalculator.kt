package me.lunathefox20.command

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import org.apache.logging.log4j.LogManager
import net.weavemc.loader.api.command.Command
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.pow

class DistanceCalculator : Command("distance") {
    companion object {
        private val logger = LogManager.getLogger(DistanceCalculator::class.java)

        // Constants for chat message prefixes and required argument count
        private const val PREFIX = "[§9§lDistance§r] "
        private const val ERROR_PREFIX = "[§c§lError§r] "
        private const val REQUIRED_ARGUMENT_COUNT = 7

        // Limits for Minecraft coordinates
        private const val X_MIN = -30000000.0
        private const val X_MAX = 30000000.0
        private const val Y_MIN = -319.0
        private const val Y_MAX = 319.0
        private const val Z_MIN = -30000000.0
        private const val Z_MAX = 30000000.0
    }

    // Error message for incorrect command usage
    private val errorMessage = ChatComponentText("$ERROR_PREFIX Usage: <x1> <y1> <z1> <x2> <y2> <z2> <euclidean/manhattan>\nExample 1: 392 -43 81 48 293 58 euclidean\nExample 2: 392 -43 81 48 293 58 manhattan")

    override fun handle(args: Array<String>) {
        if (args.size != REQUIRED_ARGUMENT_COUNT) {
            // Display an error message when the number of arguments is incorrect
            sendChatMessage(errorMessage.formattedText)
            return
        }

        try {
            // Parse input coordinates and distance method
            val coordinates = args.take(6).map { it.toDouble() }
            val distanceMethod = DistanceMethod.valueOf(args[6].uppercase())

            if (coordinates.any { it < X_MIN || it > X_MAX }) {
                // Check if any coordinates are outside of Minecraft's allowed ranges
                sendChatMessage("$PREFIX Invalid coordinates. Coordinates must be within the allowed Minecraft ranges.")
                return
            }

            val distance = distanceMethod.calculate(coordinates)
            val formattedDistance = String.format("%.2f", distance)
            // Display the calculated distance
            sendChatMessage("$PREFIX The ${distanceMethod.name.lowercase()} distance between (${coordinates[0]}, ${coordinates[1]}, ${coordinates[2]}) and (${coordinates[3]}, ${coordinates[4]}, ${coordinates[5]}) is ~$formattedDistance blocks.")
        } catch (e: NumberFormatException) {
            logger.error("Invalid input. Please provide valid numbers for coordinates.", e)
            sendChatMessage("$ERROR_PREFIX Invalid input. Please provide valid numbers for coordinates.")
        } catch (e: IllegalArgumentException) {
            // Handle an invalid distance method
            sendChatMessage("$PREFIX Invalid distance method. Use 'euclidean' or 'manhattan'.")
        }
    }

    private fun sendChatMessage(message: String) {
        // Helper function to send a chat message to the player
        Minecraft.getMinecraft().thePlayer?.addChatMessage(ChatComponentText(message))
    }
}

enum class DistanceMethod {
    EUCLIDEAN {
        override fun calculate(coordinates: List<Double>): Double {
            val x1 = coordinates[0]
            val y1 = coordinates[1]
            val z1 = coordinates[2]
            val x2 = coordinates[3]
            val y2 = coordinates[4]
            val z2 = coordinates[5]
            // Calculate Euclidean distance
            return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2) + (z2 - z1).pow(2))
        }
    },
    MANHATTAN {
        override fun calculate(coordinates: List<Double>): Double {
            val x1 = coordinates[0]
            val y1 = coordinates[1]
            val z1 = coordinates[2]
            val x2 = coordinates[3]
            val y2 = coordinates[4]
            val z2 = coordinates[5]
            // Calculate Manhattan distance
            return abs(x2 - x1) + abs(y2 - y1) + abs(z2 - z1)
        }
    };

    // Abstract function to calculate distance
    abstract fun calculate(coordinates: List<Double>): Double
}
