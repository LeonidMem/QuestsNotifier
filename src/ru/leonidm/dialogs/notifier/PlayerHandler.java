package ru.leonidm.dialogs.notifier;

import org.bukkit.event.player.PlayerQuitEvent;
import ru.leonidm.dialogs.api.events.DialogAddEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PlayerHandler implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent e) {
        LivingEntity entity = e.getEntity();
        if(entity instanceof Player player) {
            DialogsEventsHandler.checkQuests(player);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(InventoryCloseEvent e) {
        DialogsEventsHandler.checkQuests((Player)e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKill(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player player)) return;
        if(!(e.getEntity() instanceof LivingEntity entity)) return;

        if(entity.getHealth() - e.getDamage() <= 0.0) {
            DialogsEventsHandler.checkQuests(player);
        }
    }
    
    @EventHandler
    public void onDialog(DialogAddEvent e) {
        if(e.getOfflinePlayer().getPlayer() != null) {
            DialogsEventsHandler.checkQuests(e.getOfflinePlayer().getPlayer());
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        DialogsEventsHandler.remove(e.getPlayer());
    }
}
