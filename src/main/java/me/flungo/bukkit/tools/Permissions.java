package me.flungo.bukkit.tools;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Permissions {

    private final JavaPlugin plugin;
    private final String prefix;
    private final Logger log;

    public Permissions(JavaPlugin instance) {
        this(instance, instance.getName().toLowerCase().replaceAll("\\s+", ""));
    }

    public Permissions(JavaPlugin instance, String prefix) {
        this.plugin = instance;
        this.prefix = prefix;
        this.log = plugin.getLogger();
        setupPermissions();
    }

    private static boolean op;
    private static boolean bukkit;
    private static boolean vault;
    private static Permission vaultPermission = null;

    private boolean setupOPPermissions() {
        if (getConfig().getBoolean("op")) {
            log.info("Attempting to configure OP permissions");
            op = true;
        } else {
            log.info("OP permissions disabled by config");
            op = false;
        }
        return true;
    }

    private boolean setupBukkitPermissions() {
        if (getConfig().getBoolean("bukkit")) {
            log.info("Attempting to configure Bukkit Super Permissions");
            bukkit = true;
        } else {
            log.info("Bukkit Super Permissions disabled by config");
            bukkit = false;
        }
        return true;
    }

    private boolean setupVaultPermissions() {
        if (getConfig().getBoolean("vault")) {
            log.info("Attempting to configure Vault permissions");
            if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
                log.severe("Vault could not be found");
                vault = false;
                return false;
            } else {
                RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
                if (permissionProvider != null) {
                    vaultPermission = permissionProvider.getProvider();
                }
                if (vaultPermission != null) {
                    vault = true;
                } else {
                    vault = false;
                    return false;
                }
            }
        } else {
            log.info("Vault permissions disabled by config");
            vault = false;
        }
        return true;
    }

    private void setupPermissions() {
        if (setupOPPermissions()) {
            log.info("Set up OP permissions");
        } else {
            log.warning("Failed to set up OP permissions");
        }
        if (setupBukkitPermissions()) {
            log.info("Set up Bukkit Super Permissions");
        } else {
            log.warning("Failed to set up Bukkit Super Permissions");
        }
        if (setupVaultPermissions()) {
            log.info("Set up Vault permissions");
        } else {
            log.warning("Failed to set up Vault permissions");
        }
        if (!vault && !bukkit) {
            log.info("No permission systems have been set up. Default permissions will be used.");
            if (!op) {
                log.warning("Additionally, OP permissions disabled.");
            }
        }
    }

    private ConfigurationSection getConfig() {
        return plugin.getConfig().getConfigurationSection("permissions");
    }

    private void debug(String message) {
        if (getConfig().getBoolean("debug", false)) {
            plugin.getLogger().log(Level.INFO, "[Permissions Debug] {0}", message);
        }
    }

    private boolean hasNode(CommandSender cs, String node) {
        debug("Checking if " + cs.getName() + " has node " + node);
        if (!(cs instanceof Player)) {
            debug(cs.getName() + " isn't a player (probably console) - permission granted.");
            return true;
        }
        Player p = (Player) cs;
        if (op && p.isOp()) {
            debug("Op permissions enabled and " + cs.getName() + " is op - permission granted.");
            return true;
        }
        if (bukkit && p.hasPermission(node)) {
            debug("Bukkit permissions enabled and " + cs.getName() + " is has node - permission granted.");
            return true;
        }
        if (vault && vaultPermission.has(p, node)) {
            debug("Vault permissions enabled and " + cs.getName() + " is has node - permission granted.");
            return true;
        }
        debug("No permissions matched for " + cs.getName() + " - permission denied.");
        return false;
    }

    public boolean hasPermission(CommandSender cs, String permission) {
        debug("Checking if " + cs.getName() + " has plugin permission " + permission);
        if (!(cs instanceof Player)) {
            debug(cs.getName() + " isn't a player (probably console) - permissions granted.");
            return true;
        }
        Player p = (Player) cs;
        if (getConfig().getBoolean("default." + permission, false)) {
            debug("All players have plugin permission " + permission + " by default - permissions granted.");
            return true;
        }
        String node = prefix + "." + permission;
        return hasNode(p, node);
    }

    public boolean isAdmin(CommandSender cs) {
        debug("Checking if " + cs.getName() + " is admin");
        if (!(cs instanceof Player)) {
            debug(cs.getName() + " isn't a player (probably console) - permission granted.");
            return true;
        }
        Player p = (Player) cs;
        if (p.isOp() && op) {
            debug("Op permissions enabled and " + cs.getName() + " is op - permission granted.");
            return true;
        }
        String node = prefix + ".admin";
        return hasNode(p, node);
    }

    public boolean isUser(CommandSender cs) {
        debug("Checking if " + cs.getName() + " is user");
        if (!(cs instanceof Player)) {
            debug(cs.getName() + " isn't a player (probably console) - permission granted.");
            return true;
        }
        Player p = (Player) cs;
        if (!plugin.getConfig().getBoolean("enable")) {
            return false;
        }
        String node = prefix + ".user";
        if (hasNode(p, node)) {
            return true;
        }
        if (isAdmin(p)) {
            return true;
        }
        if (!bukkit && !vault) {
            debug("Bukkit and Vault disabled - permission granted.");
            return true;
        }
        debug(cs.getName() + " is not a user - permission denied.");
        return false;
    }
}
