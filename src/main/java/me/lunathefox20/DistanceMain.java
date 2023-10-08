package me.lunathefox20;

import me.lunathefox20.command.DistanceCalculator;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.CommandBus;

public class DistanceMain implements ModInitializer {
    @Override
    public void preInit() {
        CommandBus.register(new DistanceCalculator());
    }
}
