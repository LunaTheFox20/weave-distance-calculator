package me.lunathefox20.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.weavemc.loader.api.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class DistanceCalculator extends Command {
    private static final Logger logger = LogManager.getLogger(DistanceCalculator.class);
    private static final String PREFIX = "[" + EnumChatFormatting.BLUE + EnumChatFormatting.BOLD + "Distance" + EnumChatFormatting.RESET + "] ";
    private static final String ERROR_PREFIX = "[" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "Error" + EnumChatFormatting.RESET + "] ";
    private static final int REQUIRED_ARGUMENT_COUNT = 7;

    // Minecraft coordinate limits
    private static final double X_MIN = -30000000;
    private static final double X_MAX = 30000000;
    private static final double Y_MIN = -319;
    private static final double Y_MAX = 319;
    private static final double Z_MIN = -30000000;
    private static final double Z_MAX = 30000000;

    private final ChatComponentText errorMessage = new ChatComponentText(ERROR_PREFIX + "Usage: <x1> <y1> <z1> <x2> <y2> <z2> <euclidean/manhattan>\nExample 1: 392 -43 81 48 293 58 euclidean\nExample 2: 392 -43 81 48 293 58 manhattan");

    public DistanceCalculator() {
        super("distance");
    }

    public void handle(String[] args) {
        // Check if the user provided the correct number of arguments
        if (args.length != REQUIRED_ARGUMENT_COUNT) {
            sendChatMessage(errorMessage.getFormattedText());
            return;
        }

        try {
            double x1 = Double.parseDouble(args[0]);
            double y1 = Double.parseDouble(args[1]);
            double z1 = Double.parseDouble(args[2]);
            double x2 = Double.parseDouble(args[3]);
            double y2 = Double.parseDouble(args[4]);
            double z2 = Double.parseDouble(args[5]);
            String distanceMethod = args[6].toLowerCase();

            // Validate X, Y, and Z coordinates
            if (isValidCoordinate(x1, X_MIN, X_MAX) ||
                    isValidCoordinate(y1, Y_MIN, Y_MAX) ||
                    isValidCoordinate(z1, Z_MIN, Z_MAX) ||
                    isValidCoordinate(x2, X_MIN, X_MAX) ||
                    isValidCoordinate(y2, Y_MIN, Y_MAX) ||
                    isValidCoordinate(z2, Z_MIN, Z_MAX)) {
                sendChatMessage(PREFIX + "Invalid coordinates. Coordinates must be within the allowed Minecraft ranges.");
                return;
            }

            // Define distance methods
            HashMap<String, DistanceMethod> distanceMethods = new HashMap<>();
            distanceMethods.put("euclidean", DistanceMethod.EUCLIDEAN);
            distanceMethods.put("manhattan", DistanceMethod.MANHATTAN);

            if (!distanceMethods.containsKey(distanceMethod)) {
                sendChatMessage(PREFIX + "Invalid distance method. Use 'euclidean' or 'manhattan'.");
                return;
            }

            DistanceMethod method = distanceMethods.get(distanceMethod);
            double distance = switch (method) {
                case EUCLIDEAN -> calculateEuclideanDistance(x1, y1, z1, x2, y2, z2);
                case MANHATTAN -> calculateManhattanDistance(x1, y1, z1, x2, y2, z2);
            };

            // Format the distance with two decimal places
            String formattedDistance = String.format("%.2f", distance);

            // Send the message only to the player who executed the command
            sendChatMessageToPlayer(Minecraft.getMinecraft().thePlayer, PREFIX + "The " + distanceMethod + " distance between (" + x1 + ", " + y1 + ", " + z1 + ") and (" + x2 + ", " + y2 + ", " + z2 + ") is ~" + formattedDistance + " blocks.");
        } catch (NumberFormatException e) {
            // Log the exception for debugging purposes
            logger.error("Invalid input. Please provide valid numbers for coordinates.", e);

            // Provide a more informative error message to the user
            sendChatMessage(ERROR_PREFIX + "Invalid input. Please provide valid numbers for coordinates.");
        }
    }

    private void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    private void sendChatMessageToPlayer(EntityPlayerSP player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    // Helper method that checks if a coordinate is within the valid range
    private boolean isValidCoordinate(double coordinate, double min, double max) {
        return !(coordinate >= min) || !(coordinate <= max);
    }

    private double calculateEuclideanDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private double calculateManhattanDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = Math.abs(x2 - x1);
        double dy = Math.abs(y2 - y1);
        double dz = Math.abs(z2 - z1);

        return dx + dy + dz;
    }

    // Enum for distance methods
    enum DistanceMethod {
        EUCLIDEAN,
        MANHATTAN
    }
}
