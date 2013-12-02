package me.flungo.bukkit.tools;

import java.util.logging.Level;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Permissions {

    private final JavaPlugin plugin;
    private final String prefix;

    public Permissions(JavaPlugin instance) {
        this(instance, instance.getName().toLowerCase().replaceAll("\\s+", ""));
    }

    public Permissions(JavaPlugin instance, String prefix) {
        this.plugin = instance;
        this.prefix = prefix;
        setupPermissions();
    }

    private static boolean op;
    private static boolean bukkit;
    private static boolean vault;
    private static Permission vaultPermission = null;

    private boolean setupOPPermissions() {
        if (getConfig().getBoolean("op")) {
            plugin.getLogger().info("Attempting to configure OP permissions");
            op = true;
        } else {
            plugin.getLogger().info("OP permissions disabled by config");
            op = false;
        }
        return true;
    }

    private boolean setupBukkitPermissions() {
        if (getConfig().getBoolean("bukkit")) {
            plugin.getLogger().info("Attempting to configure Bukkit Super Permissions");
            bukkit = true;
        } else {
            plugin.getLogger().info("Bukkit Super Permissions disabled by config");
            bukkit = false;
        }
        return true;
    }

    private boolean setupVaultPermissions() {
        if (getConfig().getBoolean("vault")) {
            plugin.getLogger().info("Attempting to configure Vault permissions");
            if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
                plugin.getLogger().severe("Vault could not be found");
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
            plugin.getLogger().info("Vault permissions disabled by config");
            vault = false;
        }
        return true;
    }

    private void setupPermissions() {
        if (setupOPPermissions()) {
            plugin.getLogger().info("Set up OP permissions");
        } else {
            plugin.getLogger().warning("Failed to set up OP permissions");
        }
        if (setupBukkitPermissions()) {
            plugin.getLogger().info("Set up Bukkit Super Permissions");
        } else {
            plugin.getLogger().warning("Failed to set up Bukkit Super Permissions");
        }
        if (setupVaultPermissions()) {
            plugin.getLogger().info("Set up Vault permissions");
        } else {
            plugin.getLogger().warning("Failed to set up Vault permissions");
        }
        if (!vault && !bukkit) {
            plugin.getLogger().info("No permission systems have been set up. Default permissions will be used.");
            if (!op) {
                plugin.getLogger().warning("Additionally, OP permissions disabled.");
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
            debug("Bukkit permissions enabled and " + cs.getName() + " has node - permission granted.");
            return true;
        }
        if (vault && vaultPermission.has(p, node)) {
            debug("Vault permissions enabled and " + cs.getName() + " has node - permission granted.");
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
        String node = prefix + ".user";
        if (hasNode(cs, node)) {
            return true;
        }
        if (isAdmin(cs)) {
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
