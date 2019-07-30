package com.javaminecraft;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.inventory.meta.*;

//put custom creeper name in the config file
//pug amount of ticks lived in config file


public class NukeCreeper extends JavaPlugin implements Listener {
    
    
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
        
        if(sender instanceof Player) {
            if(args.length == 1 && label.equalsIgnoreCase("CreeperNuke") && args[0].equals("give")) {
                Player me = ((Player) sender);
                ItemMeta im = me.getServer().getItemFactory().getItemMeta(Material.CREEPER_SPAWN_EGG);
                im.setDisplayName(this.getConfig().getString("name"));
                ItemStack is = new ItemStack(Material.CREEPER_SPAWN_EGG);
                is.setItemMeta(im);
                me.getInventory().addItem(is);
                return true;
            }   
        }
        
        return false;
    }
    
    
    @EventHandler
    public void ExplosionPrimeEvent(ExplosionPrimeEvent event) {
        //checks if exploding thing is a creeper
        if(event.getEntity().getName().equals(this.getConfig().getString("name"))) {
            Location loc = event.getEntity().getLocation();
            if(event.getEntity().getTicksLived() < 1200) {
                if(event.getEntity().getCustomName() != null) {
                    event.setRadius(this.getConfig().getInt("radius"));
                    event.setFire(true);
                    event.getEntity().getServer().broadcastMessage("A nuke was set off at " + loc.getX() + ", " + loc.getY() + ", " + loc.getX() + ".");
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
    
    @EventHandler
    public void EntitySpawnEvent(EntitySpawnEvent event) {
        Entity target = event.getEntity();
        World world = target.getWorld();
        Location loc = target.getLocation();
        if(target.getName().equals(this.getConfig().getString("name"))) {
            PotionEffect pot = new PotionEffect(PotionEffectType.SLOW, this.getConfig().getInt("secondsToExplode"), 255);
            world.strikeLightningEffect(loc);
            Creeper cr = (Creeper)target;
            cr.setPowered(true);
            pot.apply(cr);
            cr.setMaxFuseTicks(this.getConfig().getInt("secondsToExplode") * 20);
            cr.ignite();
        }
    }
    
}