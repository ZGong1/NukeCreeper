package com.javaminecraft;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;

public class NukeCreeper extends JavaPlugin implements Listener {
    
    Player me;
    World world;
    Location spot;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this, this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1 && label.equalsIgnoreCase("CreeperNuke") && args[0].equals("reload")) {
            this.reloadConfig();
            sender.sendMessage("The config file was successfully reloaded");
            return true;
        }
        
        return false;
    }
    
    
    @EventHandler
    public void ExplosionPrimeEvent(ExplosionPrimeEvent event) {
        //checks if exploding thing is a creeper
        if(event.getEntity().getName().equals("C4EEP3–R")) {
            if(event.getEntity().getTicksLived() < 1200) {
                if(event.getEntity().getCustomName() != null) {
                    event.setRadius(this.getConfig().getInt("radius"));
                    event.setFire(true);
                }
            }
        }
    }
    
    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager().getName().equals("C4EEP3–R")) {
            event.setCancelled(true);
        }
    } 
    
}