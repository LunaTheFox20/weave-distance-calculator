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

        private const val PREFIX = "§9§l[Distance]§r "
        private const val ERROR_PREFIX = "§c§l[Error]§r "
        private const val REQUIRED_ARGUMENT_COUNT = 7

        private const val X_MIN = -30000000.0
        private const val X_MAX = 30000000.0
        private const val Y_MIN = -319.0
        private const val Y_MAX = 319.0
        private const val Z_MIN = -30000000.0
        private const val Z_MAX = 30000000.0
    }

    private val errorMessage = ChatComponentText("$ERROR_PREFIX Usage: <x1> <y1> <z1> <x2> <y2> <z2> <euclidean/manhattan>\nExample 1: 392 -43 81 48 293 58 euclidean\nExample 2: 392 -43 81 48 293 58 manhattan")

    override fun handle(args: Array<String>) {
        if (args.size != REQUIRED_ARGUMENT_COUNT) {
            sendChatMessage(Minecraft.getMinecraft(), errorMessage.formattedText)
            return
        }

        try {
            val coordinates = args.take(6).map { it.toDouble() }.toTypedArray()

            if (!isValidCoordinateRange(coordinates.asList())) {
                sendChatMessage(Minecraft.getMinecraft(), "$PREFIX Invalid coordinates. Coordinates must be within the allowed Minecraft ranges.")
                return
            }

            val distanceMethod = DistanceMethod.valueOf(args[6].uppercase())
            val startX = coordinates[0]
            val startY = coordinates[1]
            val startZ = coordinates[2]
            val endX = coordinates[3]
            val endY = coordinates[4]
            val endZ = coordinates[5]

            val distance = distanceMethod.calculate(startX, startY, startZ, endX, endY, endZ)
            val formattedDistance = String.format("%.2f", distance)

            sendChatMessage(Minecraft.getMinecraft(), "$PREFIX The ${distanceMethod.name.lowercase()} distance between " +
                    "($startX, $startY, $startZ) and ($endX, $endY, $endZ) is ~$formattedDistance blocks.")
        } catch (e: NumberFormatException) {
            logger.error("Invalid input. Please provide valid numbers for coordinates.", e)
            sendChatMessage(Minecraft.getMinecraft(), "$ERROR_PREFIX Invalid input. Please provide valid numbers for coordinates.")
        } catch (e: IllegalArgumentException) {
            sendChatMessage(Minecraft.getMinecraft(), "$PREFIX Invalid distance method. Use 'euclidean' or 'manhattan'.")
        }
    }

    private fun sendChatMessage(minecraft: Minecraft, message: String) {
        minecraft.thePlayer?.addChatMessage(ChatComponentText(message))
    }

    private fun isValidCoordinateRange(coordinates: List<Double>): Boolean {
        return coordinates.all { it in X_MIN..X_MAX } &&
                coordinates.all { it in Y_MIN..Y_MAX } &&
                coordinates.all { it in Z_MIN..Z_MAX }
    }
}

enum class DistanceMethod {
    EUCLIDEAN {
        override fun calculate(startX: Double, startY: Double, startZ: Double, endX: Double, endY: Double, endZ: Double): Double {
            return sqrt((endX - startX).pow(2) + (endY - startY).pow(2) + (endZ - startZ).pow(2))
        }
    },
    MANHATTAN {
        override fun calculate(startX: Double, startY: Double, startZ: Double, endX: Double, endY: Double, endZ: Double): Double {
            return abs(endX - startX) + abs(endY - startY) + abs(endZ - startZ)
        }
    };

    abstract fun calculate(startX: Double, startY: Double, startZ: Double, endX: Double, endY: Double, endZ: Double): Double
}
