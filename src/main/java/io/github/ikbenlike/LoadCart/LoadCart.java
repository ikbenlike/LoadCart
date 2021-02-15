package io.github.ikbenlike.LoadCart;

import java.util.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.Chunk;

import org.bukkit.entity.Entity;
import org.bukkit.entity.minecart.*;
import org.bukkit.event.Event;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.Listener;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoadCart extends JavaPlugin implements Listener
{
    public void onEnable() {
        /*if (this.getConfig().getBoolean("Load chunks")) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 6000L, 6000L);
        }*/
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        getLogger().info("Loaded LoadCart!");
    }

    /*Consider all minecarts which are not rideable utility carts.*/
    public boolean isUtilityCart(Minecart cart) {
        return !(cart instanceof RideableMinecart);
    }

    /*Consider all utility carts as not being "empty".*/
    public boolean isCartEmpty(Minecart cart) {
        return cart.isEmpty() && !isUtilityCart(cart);
    }

    public boolean chunkContainsCart(Chunk chunk) {
        Entity[] entities = chunk.getEntities();
        for (Entity entity : entities) {
            if (entity instanceof Minecart) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void VehicleHandler(final VehicleMoveEvent vehicleMoveEvent) {
        if (vehicleMoveEvent.getVehicle() instanceof Minecart) {
            Location from = vehicleMoveEvent.getFrom();
            Location to = vehicleMoveEvent.getTo();
            Chunk fromChunk = from.getChunk();
            Chunk toChunk = to.getChunk();
            if (toChunk.getX() != fromChunk.getX() || toChunk.getZ() != fromChunk.getZ()) {
                toChunk.addPluginChunkTicket(this);
                getLogger().info("Added ticket to chunk (X: " + toChunk.getX() + " Z: " + toChunk.getZ() + ")" + " from chunk (X: " + fromChunk.getX() + " Z: " + fromChunk.getZ() + ")");
                if (!chunkContainsCart(fromChunk)) {
                    fromChunk.removePluginChunkTicket(this);
                    getLogger().info("Removed ticket from chunk (X: " + fromChunk.getX() + " Z: " + fromChunk.getZ() + ")");
                }
            }
            /*if (vehicleMoveEvent.getVehicle() instanceof StorageMinecart) {
                ((Minecart)vehicleMoveEvent.getVehicle()).setSlowWhenEmpty(!this.getConfig().getBoolean("Affect storage carts"));
            }
            if (!(vehicleMoveEvent.getVehicle() instanceof StorageMinecart) && !(vehicleMoveEvent.getVehicle() instanceof PoweredMinecart)) {
                ((Minecart)vehicleMoveEvent.getVehicle()).setSlowWhenEmpty(!this.getConfig().getBoolean("Affect empty carts"));
            }
            if (this.getConfig().getBoolean("Load chunks")) {
                for (int i = -3; i <= 3; ++i) {
                    for (int j = -3; j <= 3; ++j) {
                        vehicleMoveEvent.getTo().getWorld().loadChunk(vehicleMoveEvent.getTo().getChunk().getX() + i, vehicleMoveEvent.getTo().getChunk().getZ() + j);
                    }
                }
            }*/
        }
    }

    /*public void run() {
        final Iterator<World> iterator = this.getServer().getWorlds().iterator();
        while (iterator.hasNext()) {
            Chunk[] loadedChunks;
            for (int length = (loadedChunks = iterator.next().getLoadedChunks()).length, i = 0; i < length; ++i) {
                final Chunk chunk = loadedChunks[i];
                final ChunkUnloadEvent chunkUnloadEvent = new ChunkUnloadEvent(chunk);
                this.getServer().getPluginManager().callEvent((Event)chunkUnloadEvent);
                chunk.unload(true); // TODO: replace with chunk ticket system!
            }
        }
    }*/

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        final ArrayList<Minecart> list = new ArrayList<>();
        for (World world : this.getServer().getWorlds()) {
            list.addAll(world.getEntitiesByClass(Minecart.class));
        }
        if (command.getName().equalsIgnoreCase("countminecarts") && commandSender.hasPermission("LoadCart.count")) {
            commandSender.sendMessage(list.size() + " Minecart" + ((list.size() != 1) ? "s" : ""));
        }
        else if (command.getName().equalsIgnoreCase("countemptycarts") && commandSender.hasPermission("LoadCart.count")) {
            int count = 0;
            for (Minecart cart : list) {
                if (isCartEmpty(cart)) {
                    count += 1;
                }
            }
            commandSender.sendMessage(count + " Minecart" + ((count != 1) ? "s" : ""));
        }
        else if (command.getName().equalsIgnoreCase("countutilitycarts") && commandSender.hasPermission("LoadCart.count")) {
            int count = 0;
            for (Minecart cart : list) {
                if (isUtilityCart(cart)) {
                    count += 1;
                }
            }
            commandSender.sendMessage(count + " Minecart" + ((count != 1) ? "s" : ""));
        }
        else if (command.getName().equalsIgnoreCase("removeminecarts") && commandSender.hasPermission("LoadCart.remove")) {
            commandSender.sendMessage(list.size() + " Minecart" + ((list.size() != 1) ? "s" : "") + " removed");
            for (Minecart minecart : list) {
                minecart.remove();
            }
        }
        else if (command.getName().equalsIgnoreCase("removeemptycarts") && commandSender.hasPermission("LoadCart.remove")) {
            int count = 0;
            for (Minecart cart : list) {
                if (isCartEmpty(cart)) {
                    cart.remove();
                    count +=1 ;
                }
            }
            commandSender.sendMessage(count + " Minecart" + ((count != 1) ? "s" : "") + " removed");
        }
        else if (command.getName().equalsIgnoreCase("removeutilitycarts") && commandSender.hasPermission("LoadCart.remove")) {
            int count = 0;
            for (Minecart cart : list) {
                if (isUtilityCart(cart)) {
                    cart.remove();
                    count +=1 ;
                }
            }
            commandSender.sendMessage(count + " Minecart" + ((count != 1) ? "s" : "") + " removed");
        }
        else if (command.getName().equalsIgnoreCase("removetickets") && commandSender.hasPermission("LoadCart.ticket")) {
            final List<World> worlds = getServer().getWorlds();
            int count = 0;
            for (World world : worlds ) {
                Map<Plugin, Collection<Chunk>> pluginMap = world.getPluginChunkTickets();
                Collection<Chunk> chunks = pluginMap.get(this);
                if (chunks != null) {
                    for (Chunk chunk : chunks) {
                        chunk.removePluginChunkTicket(this);
                        count += 1;
                    }
                }
            }
            commandSender.sendMessage(count + " Ticket" + ((count != 1) ? "s" : "") + " removed");
        }
        return true;
    }

    public void onDisable() {
    }
}