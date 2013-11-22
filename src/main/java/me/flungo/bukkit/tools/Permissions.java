package me.flungo.bukkit.tools;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Permissions {

    private final JavaPlugin plugin;

    private final Log log;

    private final String prefix;

    public Permissions(JavaPlugin instance, Log logger) {
        this(instance, logger, instance.getName().toLowerCase().replaceAll("\\s+", ""));
    }

    public Permissions(JavaPlugin instance, Log logger, String prefix) {
        this.plugin = instance;
        this.log = logger;
        this.prefix = plugin.getName().toLowerCase().replaceAll("\\s+", "");
    }

    private static boolean op;

    private static boolean bukkit;

    private static boolean vault;

    private static Permission vaultPermission = null;

    private void setupOPPermissions() {
        if (plugin.getConfig().getBoolean("permissions.op")) {
            log.info("Attempting to configure OP permissions");
            op = true;
        } else {
            log.info("OP permissions disabled by config");
            op = false;
        }
    }

    private void setupBukkitPermissions() {
        if (plugin.getConfig().getBoolean("permissions.bukkit")) {
            log.info("Attempting to configure Bukkit Super Permissions");
            bukkit = true;
        } else {
            log.info("Bukkit Super Permissions disabled by config");
            bukkit = false;
        }
    }

    private void setupVaultPermissions() {
        if (plugin.getConfig().getBoolean("permissions.vault")) {
            log.info("Attempting to configure Vault permissions");
            if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
                log.severe("Vault could not be found");
                vault = false;
            } else {
                RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
                if (permissionProvider != null) {
                    vaultPermission = permissionProvider.getProvider();
                }
                if (vaultPermission != null) {
                    vault = true;
                } else {
                    vault = false;
                }
            }
        } else {
            log.info("Vault permissions disabled by config");
            vault = false;
        }
    }

    public void setupPermissions(String nodePrefix) {
        setupOPPermissions();
        if (op) {
            log.info("OP permissions set up");
        } else {
            log.warning("OP permissions not set up");
        }
        setupBukkitPermissions();
        if (bukkit) {
            log.info("Bukkit Super Permissions set up");
        } else {
            log.warning("Bukkit Super Permissions not set up");
        }
        setupVaultPermissions();
        if (vault) {
            log.info("Vault permissions set up");
        } else {
            log.warning("Vault permissions not set up");
        }
        if (!vault && !bukkit) {
            log.warning("No permission systems have been set up. Default permissions will be used.");
            if (!op) {
                log.warning("Additionally, OP permissions disabled.");
            }
        }
    }

    public void setupPermissions() {
        String nodePrefix = plugin.getDescription().getClass().toString().toLowerCase();
        setupPermissions(nodePrefix);
    }

    private boolean hasNode(CommandSender cs, String node) {
        if (!(cs instanceof Player)) {
            return true;
        }
        Player p = (Player) cs;
        if (bukkit && p.hasPermission(node)) {
            return true;
        }
        if (vault && vaultPermission.has(p, node)) {
            return true;
        }
        return false;
    }

    public boolean hasPermission(CommandSender cs, String permission) {
        if (!(cs instanceof Player)) {
            return true;
        }
        Player p = (Player) cs;
        if (plugin.getConfig().getBoolean("permissions.default." + permission)) {
            return true;
        }
        String node = prefix + "." + permission;
        return hasNode(p, node);
    }

    public boolean isAdmin(CommandSender cs) {
        if (!(cs instanceof Player)) {
            return true;
        }
        Player p = (Player) cs;
        if (p.isOp() && op) {
            return true;
        }
        String node = "voidwarp.admin";
        return hasNode(p, node);
    }

    public boolean isUser(CommandSender cs) {
        if (!(cs instanceof Player)) {
            return true;
        }
        Player p = (Player) cs;
        if (!plugin.getConfig().getBoolean("enable")) {
            return false;
        }
        String node = "voidwarp.user";
        if (hasNode(p, node)) {
            return true;
        }
        if (isAdmin(p)) {
            return true;
        }
        if (!bukkit && !vault) {
            return true;
        }
        return false;
    }

}
