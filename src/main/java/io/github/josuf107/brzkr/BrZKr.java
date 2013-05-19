package io.github.josuf107.brzkr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;

public final class BrZKr extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("BrZKr enabled. Hold on to your britches.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("BrZKr disabled. It was fun while it lasted.");
    }

    @Override
    public boolean onCommand( final CommandSender sender
                            , final Command command
                            , final String label
                            , final String[] args) {
        getLogger().info("Hello Minecrafty World!");
        return true;
    }

    final EffectStack coalEffects = new EffectStack();
    final EffectStack glowStoneEffects = new EffectStack();

    @EventHandler
    public void entityKilled(final EntityDeathEvent deathEvent) {
        final LivingEntity player = deathEvent.getEntity().getKiller();
        if(coalEffects.timeSinceEffect(player) < 20) {
            // This means they killed someone less than twenty seconds
            // since they used the coal effect
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5, 5));
            // Speed them up (just an example)
        }
    }

    @EventHandler
    public void clickEvent(final InventoryClickEvent clickEvent) {
        if(clickEvent.isRightClick()) {
            final int COAL = 263;
            final int GLOWSTONE = 0;
            final int id = clickEvent.getCurrentItem().getTypeId();
            final LivingEntity player = clickEvent.getWhoClicked();
            if(id == COAL) {
                //addEffect( type, duration, amplitude )
                coalEffects.addEffect(player, PotionEffectType.INCREASE_DAMAGE, 5, 20);
                coalEffects.addEffect(player, PotionEffectType.SLOW, 5, 3);
                coalEffects.addEffect(player, PotionEffectType.POISON, 5, 1);
                //doEffects( entity, cooldown, message )
                coalEffects.doEffects(player, 20, "You're strong! ...but slow");
            } else if (id == GLOWSTONE) {
                // Anthony writes his code here using glowStoneEffects
            }
        }
    }

    private class EffectStack {
        private final Map<LivingEntity, Long> timeMap;
        private final Map<LivingEntity, Collection<PotionEffect>> effects;

        public EffectStack() {
            timeMap = new HashMap<LivingEntity, Long>();
            effects = new HashMap<LivingEntity, Collection<PotionEffect>>();
        }

        public void addEffect   ( final LivingEntity entity
                                , final PotionEffectType type
                                , final int duration
                                , final int amplitude) {
            final Collection<PotionEffect> stack;
            if (effects.get(entity) == null) {
                stack = new LinkedList<PotionEffect>();
                effects.put(entity, stack);
            } else {
                stack = effects.get(entity);
            }
            stack.add(new PotionEffect(type, 20 * duration, amplitude));
        }

        public boolean doEffects( final LivingEntity entity
                                , final int cooldown
                                , final String message) {
            if(effects.get(entity) == null) {
                return false;
            }
            final long secondsElapsed = timeSinceEffect(entity);
            final long timeNow = System.currentTimeMillis();
            if(secondsElapsed > cooldown) {
                entity.addPotionEffects(effects.get(entity));
                timeMap.put(entity, timeNow);
                effects.get(entity).clear();
                if(entity instanceof Player) {
                    ((Player) entity).sendMessage(message);
                }
                return true;
            } else {
                if(entity instanceof Player) {
                    ((Player) entity).sendMessage("Still cooling down! " + (cooldown - secondsElapsed) + " seconds remaining.");
                }
                return false;
            }
        }

        public long timeSinceEffect(final LivingEntity entity) {
            final long timeNow = System.currentTimeMillis();
            final long lastTime;
            if(timeMap.get(entity) == null) {
                lastTime = 0;
            } else {
                lastTime = timeMap.get(entity);
            }
            return (timeNow - lastTime) / 1000;
        }
    }
}
