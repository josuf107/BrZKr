package io.github.josuf107.brzkr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

    @EventHandler
    public void clickEvent(final InventoryClickEvent clickEvent) {
        if(clickEvent.isRightClick()) {
            final int COAL = 263;
            final int id = clickEvent.getCurrentItem().getTypeId();
            final LivingEntity player = clickEvent.getWhoClicked();
            if(id == COAL) {
                coalEffects.addEffect(PotionEffectType.INCREASE_DAMAGE, 5, 20);
                coalEffects.addEffect(PotionEffectType.SLOW, 5, 3);
                coalEffects.addEffect(PotionEffectType.POISON, 5, 1);
                coalEffects.doEffects(player, 20);
            }
        }
    }

    private class EffectStack {
        private final Map<LivingEntity, Long> timeMap;
        private final Collection<PotionEffect> effects;

        public EffectStack() {
            timeMap = new HashMap<LivingEntity, Long>();
            effects = new LinkedList<PotionEffect>();
        }

        public void addEffect( final PotionEffectType type
                                , final int duration
                                , final int amplitude) {
            effects.add(new PotionEffect(type, duration, amplitude));
        }

        public boolean doEffects( final LivingEntity entity
                                , final int cooldown) {
            final long timeNow = System.currentTimeMillis();
            final long lastTime;
            if(timeMap.get(entity) == null) {
                lastTime = 0;
            } else {
                lastTime = timeMap.get(entity);
            }
            final long secondsElapsed = (timeNow - lastTime) / 1000;
            if(secondsElapsed > cooldown) {
                entity.addPotionEffects(effects);
                timeMap.put(entity, timeNow);
                effects.clear();
                if(entity instanceof Player) {
                    ((Player) entity).sendMessage("You're strong! ...but slow");
                }
                return true;
            } else {
                if(entity instanceof Player) {
                    ((Player) entity).sendMessage("Still cooling down! " + (cooldown - secondsElapsed) + " seconds remaining.");
                }
                return false;
            }
        }
    }

    public boolean affect   ( final LivingEntity entity
                            , final PotionEffectType type
                            , final int duration
                            , final int amplitude) {
        final PotionEffect effect;
        effect = new PotionEffect(type, duration, amplitude);
        return entity.addPotionEffect(effect);
    }
}
