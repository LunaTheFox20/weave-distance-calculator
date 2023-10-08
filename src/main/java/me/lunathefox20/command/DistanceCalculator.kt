package me.lunathefox20.command

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.weavemc.loader.api.command.Command
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class DistanceCalculator : Command("distance") {
    companion object {
        private val logger = LogManager.getLogger(DistanceCalculator::class.java)
        private const val PREFIX = "[§9§lDistance§r] "
	private const val ERROR_PREFIX = "[§c§lError§r] "
        private const val REQUIRED_ARGUMENT_COUNT = 7

        // Minecraft coordinate limits
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
            sendChatMessage(errorMessage.formattedText)
            return
        }

        try {
            val x1 = args[0].toDouble()
            val y1 = args[1].toDouble()
            val z1 = args[2].toDouble()
            val x2 = args[3].toDouble()
            val y2 = args[4].toDouble()
            val z2 = args[5].toDouble()
            val distanceMethod = args[6].lowercase()

            if (isValidCoordinate(x1, X_MIN, X_MAX) ||
                isValidCoordinate(y1, Y_MIN, Y_MAX) ||
                isValidCoordinate(z1, Z_MIN, Z_MAX) ||
                isValidCoordinate(x2, X_MIN, X_MAX) ||
                isValidCoordinate(y2, Y_MIN, Y_MAX) ||
                isValidCoordinate(z2, Z_MIN, Z_MAX)) {
                sendChatMessage("$PREFIX Invalid coordinates. Coordinates must be within the allowed Minecraft ranges.")
                return
            }

            val distanceMethods: HashMap<String, DistanceMethod> = hashMapOf(
                "euclidean" to DistanceMethod.EUCLIDEAN,
                "manhattan" to DistanceMethod.MANHATTAN)

            if (!distanceMethods.containsKey(distanceMethod)) {
                sendChatMessage("$PREFIX Invalid distance method. Use 'euclidean' or 'manhattan'.")
                return
            }

            val method = distanceMethods[distanceMethod]
	    val distance = when (method) {
    	    DistanceMethod.EUCLIDEAN -> calculateEuclideanDistance(x1, y1, z1, x2, y2, z2)
            DistanceMethod.MANHATTAN -> calculateManhattanDistance(x1, y1, z1, x2, y2, z2)
            else -> {
                    // Handle the case when an unknown method is encountered.
                    sendChatMessage("$PREFIX Unknown distance method: $distanceMethod")
                    return
    }
}


            val formattedDistance = String.format("%.2f", distance)

            sendChatMessageToPlayer(Minecraft.getMinecraft().thePlayer, "$PREFIX The $distanceMethod distance between ($x1, $y1, $z1) and ($x2, $y2, $z2) is ~$formattedDistance blocks.")
        } catch (e: NumberFormatException) {
            logger.error("Invalid input. Please provide valid numbers for coordinates.", e)

            sendChatMessage("$ERROR_PREFIX Invalid input. Please provide valid numbers for coordinates.")
        }
    }

    private fun sendChatMessage(message: String) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(message))
    }

    private fun sendChatMessageToPlayer(player: EntityPlayerSP, message: String) {
        player.addChatMessage(ChatComponentText(message))
    }

    private fun isValidCoordinate(coordinate: Double, min: Double, max: Double): Boolean {
        return !(coordinate >= min) || !(coordinate <= max)
    }

    private fun calculateEuclideanDistance(
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double
    ): Double {
        val dx = x2 - x1
        val dy = y2 - y1
        val dz = z2 - z1

        return Math.sqrt(dx * dx + dy * dy + dz * dz)
    }

    private fun calculateManhattanDistance(
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double
    ): Double {
        val dx = Math.abs(x2 - x1)
        val dy = Math.abs(y2 - y1)
        val dz = Math.abs(z2 - z1)

        return dx + dy + dz
    }
}

// Enum for distance methods
enum class DistanceMethod {
    EUCLIDEAN,
    MANHATTAN
}