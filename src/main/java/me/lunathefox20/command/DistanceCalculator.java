package me.lunathefox20.command;

import net.minecraft.util.EnumChatFormatting;
import net.weavemc.loader.api.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import java.util.HashMap;

public class DistanceCalculator extends Command {
    public DistanceCalculator() {
        super("distance");
    }

    static String prefix = "[" + EnumChatFormatting.BLUE + EnumChatFormatting.BOLD + "Distance" + EnumChatFormatting.RESET + "] ";
    static String ErrorPrefix = "[" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "Error" + EnumChatFormatting.RESET + "] ";
    ChatComponentText ErrorMessage = new ChatComponentText(ErrorPrefix + "Usage: <x1> <y1> <z1> <x2> <y2> <z2> <euclidean/manhattan>\nExample 1: 392 -43 81 48 293 58 euclidean\nExample 2: 392 -43 81 48 293 58 manhattan");

    public void handle(String[] args) {
        if (args.length != 7) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(ErrorMessage);
            return;
        }

        try {
            double x1 = Double.parseDouble(args[0]);
            double y1 = Double.parseDouble(args[1]);
            double z1 = Double.parseDouble(args[2]);
            double x2 = Double.parseDouble(args[3]);
            double y2 = Double.parseDouble(args[4]);
            double z2 = Double.parseDouble(args[5]);
            String distance_method = args[6].toLowerCase();

            // Define distance methods
            HashMap<String, DistanceMethod> distanceMethods = new HashMap<>();
            distanceMethods.put("euclidean", DistanceMethod.EUCLIDEAN);
            distanceMethods.put("manhattan", DistanceMethod.MANHATTAN);

            if (!distanceMethods.containsKey(distance_method)) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(prefix + "Invalid distance method. Use 'euclidean' or 'manhattan'."));
                return;
            }

            DistanceMethod method = distanceMethods.get(distance_method);
            double distance = 0;

            switch (method) {
                case EUCLIDEAN:
                    distance = calculateEuclideanDistance(x1, y1, z1, x2, y2, z2);
                    break;
                case MANHATTAN:
                    distance = calculateManhattanDistance(x1, y1, z1, x2, y2, z2);
                    break;
            }

            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(prefix + "The " + distance_method + " distance between (" + x1 + ", " + y1 + ", " + z1 + ") and (" + x2 + ", " + y2 + ", " + z2 + ") is " + distance + "."));
        } catch (NumberFormatException e) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(ErrorMessage);
        }
    }

    private double calculateEuclideanDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;

        return Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz));
    }

    private double calculateManhattanDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = Math.abs(x2 - x1);
        double dy = Math.abs(y2 - y1);
        double dz = Math.abs(z2 - z1);

        return Math.round(dx + dy + dz);
    }

    // Enum for distance methods
    enum DistanceMethod {
        EUCLIDEAN,
        MANHATTAN
    }
}