package me.flungo.bukkit.tools;

import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class Log {

    public final JavaPlugin plugin;
    public final Logger logger;
    public final String prefix;

    public Log(JavaPlugin instance) {
        plugin = instance;
        logger = plugin.getLogger();
        prefix = "[" + plugin.getDescription().getName() + "] ";
    }

    @Deprecated
    public void logMessage(String msg) {
        logMessage(msg, Level.INFO);
    }

    @Deprecated
    public void logMessage(String msg, Level level) {
        logger.log(level, "[" + plugin.getDescription().getName() + "] " + msg);
    }

    public static final Logger getGlobal() {
        return Logger.getGlobal();
    }

    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }

    public static Logger getLogger(String name, String resourceBundleName) {
        return Logger.getLogger(name, resourceBundleName);
    }

    public static Logger getAnonymousLogger() {
        return Logger.getAnonymousLogger();
    }

    public static Logger getAnonymousLogger(String resourceBundleName) {
        return Logger.getAnonymousLogger(resourceBundleName);
    }

    public ResourceBundle getResourceBundle() {
        return logger.getResourceBundle();
    }

    public String getResourceBundleName() {
        return logger.getResourceBundleName();
    }

    public void setFilter(Filter newFilter) throws SecurityException {
        logger.setFilter(newFilter);
    }

    public Filter getFilter() {
        return logger.getFilter();
    }

    public void log(LogRecord record) {
        String message = record.getMessage();
        if (!message.startsWith("[")) {
            record.setMessage(prefix + message);
        }
        logger.log(record);
    }

    public void log(Level level, String msg) {
        logger.log(level, prefix + msg);
    }

    public void log(Level level, String msg, Object param1) {
        logger.log(level, prefix + msg, param1);
    }

    public void log(Level level, String msg, Object[] params) {
        logger.log(level, prefix + msg, params);
    }

    public void log(Level level, String msg, Throwable thrown) {
        logger.log(level, prefix + msg, thrown);
    }

    public void logp(Level level, String sourceClass, String sourceMethod, String msg) {
        logger.logp(level, sourceClass, sourceMethod, prefix + msg);
    }

    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
        logger.logp(level, sourceClass, sourceMethod, prefix + msg, param1);
    }

    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
        logger.logp(level, sourceClass, sourceMethod, prefix + msg, params);
    }

    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown) {
        logger.logp(level, sourceClass, sourceMethod, prefix + msg, thrown);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg) {
        logger.logrb(level, sourceClass, sourceMethod, bundleName, prefix + msg);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object param1) {
        logger.logrb(level, sourceClass, sourceMethod, bundleName, prefix + msg, param1);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object[] params) {
        logger.logrb(level, sourceClass, sourceMethod, bundleName, prefix + msg, params);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Throwable thrown) {
        logger.logrb(level, sourceClass, sourceMethod, bundleName, prefix + msg, thrown);
    }

    public void entering(String sourceClass, String sourceMethod) {
        logger.entering(sourceClass, sourceMethod);
    }

    public void entering(String sourceClass, String sourceMethod, Object param1) {
        logger.entering(sourceClass, sourceMethod, param1);
    }

    public void entering(String sourceClass, String sourceMethod, Object[] params) {
        logger.entering(sourceClass, sourceMethod, params);
    }

    public void exiting(String sourceClass, String sourceMethod) {
        logger.exiting(sourceClass, sourceMethod);
    }

    public void exiting(String sourceClass, String sourceMethod, Object result) {
        logger.exiting(sourceClass, sourceMethod, result);
    }

    public void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
        logger.throwing(sourceClass, sourceMethod, thrown);
    }

    public void severe(String msg) {
        logger.severe(prefix + msg);
    }

    public void warning(String msg) {
        logger.warning(prefix + msg);
    }

    public void info(String msg) {
        logger.info(prefix + msg);
    }

    public void config(String msg) {
        logger.config(prefix + msg);
    }

    public void fine(String msg) {
        logger.fine(prefix + msg);
    }

    public void finer(String msg) {
        logger.finer(prefix + msg);
    }

    public void finest(String msg) {
        logger.finest(prefix + msg);
    }

    public void setLevel(Level newLevel) throws SecurityException {
        logger.setLevel(newLevel);
    }

    public Level getLevel() {
        return logger.getLevel();
    }

    public boolean isLoggable(Level level) {
        return logger.isLoggable(level);
    }

    public String getName() {
        return logger.getName();
    }

    public void addHandler(Handler handler) throws SecurityException {
        logger.addHandler(handler);
    }

    public void removeHandler(Handler handler) throws SecurityException {
        logger.removeHandler(handler);
    }

    public Handler[] getHandlers() {
        return logger.getHandlers();
    }

    public void setUseParentHandlers(boolean useParentHandlers) {
        logger.setUseParentHandlers(useParentHandlers);
    }

    public boolean getUseParentHandlers() {
        return logger.getUseParentHandlers();
    }

    public Logger getParent() {
        return logger.getParent();
    }

    public void setParent(Logger parent) {
        logger.setParent(parent);
    }

}
