/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.bukkitdev.skywars.kits;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import net.daboross.bukkitdev.skywars.api.SkyStatic;
import net.daboross.bukkitdev.skywars.api.SkyWars;
import net.daboross.bukkitdev.skywars.api.config.SkyConfigurationException;
import net.daboross.bukkitdev.skywars.api.kits.SkyKit;
import net.daboross.bukkitdev.skywars.api.kits.SkyKits;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SkyKitConfiguration implements SkyKits {

    private final SkyWars plugin;
    private final HashMap<String, SkyKit> kits = new HashMap<>();

    public SkyKitConfiguration(SkyWars plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        SkyStatic.debug("Loading kits");
        File kitFile = new File(plugin.getDataFolder(), "kits.yml");
        if (!kitFile.exists()) {
            plugin.saveResource("kits.yml", true);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(kitFile);
        for (String key : config.getKeys(false)) {
            if (config.isConfigurationSection(key)) {
                try {
                    kits.put(key, SkyKitDecoder.decodeKit(config.getConfigurationSection(key)));
                } catch (SkyConfigurationException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Couldn't decode kit with name " + key + " in file " + kitFile.getAbsolutePath() + "! You may encounter errors later on because of this. Error:", ex);
                }
            } else {
                plugin.getLogger().log(Level.WARNING, "There is a non-kit value in the kits.yml file ''{0}''.", config.get(key));
            }
        }
        for (SkyKit kit : kits.values()) {
            SkyStatic.debug("Loaded kit: " + kit);
        }
    }

    @Override
    public Set<String> getKitNames() {
        return Collections.unmodifiableSet(kits.keySet());
    }

    @Override
    public SkyKit getKit(String name) {
        return kits.get(name);
    }
}
