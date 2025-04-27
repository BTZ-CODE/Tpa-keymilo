package pl.btz.tpa;

import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import pl.btz.tpa.command.TPACommand;
import pl.btz.tpa.command.TPADeclineCommand;
import pl.btz.tpa.command.TPAIgnoreCommand;
import pl.btz.tpa.command.TPAcceptCommand;
import pl.btz.tpa.config.MessagesConfig;
import pl.btz.tpa.database.DatabaseHandler;
import eu.okaeri.configs.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public final class Main extends JavaPlugin {

    private DatabaseHandler databaseHandler;
    private MessagesConfig messagesConfig;
    private TPACommand tpaCommand;
    private TPAIgnoreCommand tpaIgnoreCommand;
    private TPAcceptCommand tpaAcceptCommand;
    private TPADeclineCommand tpaDeclineCommand;

    @Override
    public void onEnable() {
        try {
            setupConfig();
            setupDatabase();
            registerCommands();
        } catch (IOException ex) {
            getLogger().severe("I/O error during plugin initialization: " + ex.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        } catch (RuntimeException ex) {
            getLogger().severe("Configuration error: " + ex.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void setupConfig() throws IOException {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            throw new IOException("Nie udało się utworzyć folderu pluginu");
        }
        saveResource("messages.yml", false);

        messagesConfig = ConfigManager.create(MessagesConfig.class, spec -> {
            spec.withConfigurer(new YamlSnakeYamlConfigurer());
            spec.withBindFile(new File(getDataFolder(), "messages.yml"));
            spec.saveDefaults();
            spec.load();
        });

        tpaCommand = new TPACommand(this, messagesConfig);
        tpaIgnoreCommand = new TPAIgnoreCommand(messagesConfig);
        tpaAcceptCommand = new TPAcceptCommand(tpaCommand, messagesConfig);
        tpaDeclineCommand = new TPADeclineCommand(tpaCommand, messagesConfig);
    }

    private void setupDatabase() {
        String dbPath = getDataFolder().getAbsolutePath() + "/tpa.db";
        databaseHandler = new DatabaseHandler(dbPath);
    }

    private void registerCommands() {
        try {
            
            Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(getServer());

            commandMap.register("tpa", new BukkitCommand("tpa") {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return tpaCommand.onCommand(sender, this, label, args);
                }
            });

            commandMap.register("tpahere", new BukkitCommand("tpahere") {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return tpaCommand.onCommand(sender, this, label, args);
                }
            });

            commandMap.register("tpaccept", new BukkitCommand("tpaccept") {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return new TPAcceptCommand(tpaCommand, messagesConfig).onCommand(sender, this, label, args);
                }
            });

            commandMap.register("tpdecline", new BukkitCommand("tpdecline") {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return new TPADeclineCommand(tpaCommand, messagesConfig).onCommand(sender, this, label, args);
                }
            });


            commandMap.register("tpaignore", new BukkitCommand("tpaignore") {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return new TPAIgnoreCommand(messagesConfig).onCommand(sender, this, label, args);
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe("Failed to register commands: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        if (databaseHandler != null) {
            databaseHandler.close();
        }
    }
}
