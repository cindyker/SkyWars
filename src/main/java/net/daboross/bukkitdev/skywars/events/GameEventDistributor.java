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
package net.daboross.bukkitdev.skywars.events;

import java.util.logging.Level;
import lombok.NonNull;
import net.daboross.bukkitdev.skywars.SkyWarsPlugin;
import net.daboross.bukkitdev.skywars.api.SkyStatic;
import net.daboross.bukkitdev.skywars.api.events.ArenaPlayerDeathEvent;
import net.daboross.bukkitdev.skywars.api.events.ArenaPlayerKillPlayerEvent;
import net.daboross.bukkitdev.skywars.api.events.GameEndEvent;
import net.daboross.bukkitdev.skywars.api.events.GameStartEvent;
import net.daboross.bukkitdev.skywars.api.events.LeaveGameEvent;
import net.daboross.bukkitdev.skywars.api.events.RespawnAfterLeaveGameEvent;

public class GameEventDistributor {

    private final SkyWarsPlugin plugin;

    public GameEventDistributor(@NonNull SkyWarsPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void distribute(@NonNull GameStartInfo info) {
        try {
            // -- Normal --
            plugin.getIDHandler().onGameStart(info);
            plugin.getCurrentGameTracker().onGameStart(info);
            plugin.getWorldHandler().onGameStart(info);
            plugin.getResetHealth().onGameStart(info); // Should be after WorldHandler
            plugin.getInventorySave().onGameStart(info);
            plugin.getBroadcaster().broadcastStart(info);
            plugin.getTeamListener().onGameStart(info);
            // -- After --
            plugin.getServer().getPluginManager().callEvent(new GameStartEvent(plugin, info.getGame(), info.getPlayers()));
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't broadcast GameStart in " + SkyStatic.getPluginName() + " version " + SkyStatic.getImplementationVersion(), t);
        }
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void distribute(@NonNull GameEndInfo info) {
        try {
            // -- Initial --
            plugin.getIDHandler().onGameEnd(info);
            // -- Normal --
            plugin.getBroadcaster().broadcastEnd(info);
            plugin.getTeamListener().onGameEnd(info);
            if (plugin.getPoints() != null) {
                plugin.getPoints().onGameEnd(info);
            }
            if (plugin.getEcoRewards() != null) {
                plugin.getEcoRewards().onGameEnd(info);
            }
            // -- High --
            plugin.getWorldHandler().onGameEnd(info);
            // -- After --
            plugin.getServer().getPluginManager().callEvent(new GameEndEvent(plugin, info.getGame(), info.getAlivePlayers()));
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't broadcast GameEnd in " + SkyStatic.getPluginName() + " version " + SkyStatic.getImplementationVersion(), t);
        }
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void distribute(@NonNull PlayerLeaveGameInfo info) {
        try {
            // -- Normal --
            plugin.getCurrentGameTracker().onPlayerLeaveGame(info);
            plugin.getAttackerStorage().onPlayerLeaveGame(info);
            plugin.getTeamListener().onPlayerLeaveGame(info);
            // -- After --
            plugin.getServer().getPluginManager().callEvent(new LeaveGameEvent(plugin, info.getId(), info.getPlayer()));
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't broadcast PlayerLeaveGame in " + SkyStatic.getPluginName() + " version " + SkyStatic.getImplementationVersion(), t);
        }
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void distribute(@NonNull PlayerRespawnAfterGameEndInfo info) {
        try {
            // -- Normal --
            plugin.getResetHealth().onPlayerRespawn(info);
            plugin.getInventorySave().onPlayerRespawn(info);
            // -- After --
            plugin.getServer().getPluginManager().callEvent(new RespawnAfterLeaveGameEvent(plugin, info.getPlayer()));
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't broadcast PlayerRespawnAfterGameEnd in " + SkyStatic.getPluginName() + " version " + SkyStatic.getImplementationVersion(), t);
        }
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void distribute(@NonNull PlayerKillPlayerInfo info) {
        try {
            // -- Normal --
            if (plugin.getPoints() != null) {
                plugin.getPoints().onKill(info);
            }
            if (plugin.getEcoRewards() != null) {
                plugin.getEcoRewards().onPlayerKillPlayer(info);
            }
            // -- After --
            plugin.getServer().getPluginManager().callEvent(new ArenaPlayerKillPlayerEvent(plugin, info.getGameId(), info.getKillerName(), info.getKilled()));
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't broadcast PlayerKillPlayer in " + SkyStatic.getPluginName() + " version " + SkyStatic.getImplementationVersion(), t);
        }
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void distribute(@NonNull PlayerDeathInArenaInfo info) {
        try {
            // -- Normal --
            if (plugin.getPoints() != null) {
                plugin.getPoints().onDeath(info);
            }
            // -- After --
            plugin.getServer().getPluginManager().callEvent(new ArenaPlayerDeathEvent(plugin, info.getGameId(), info.getKilled()));
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't broadcast PlayerDeathInArena in " + SkyStatic.getPluginName() + " version " + SkyStatic.getImplementationVersion(), t);
        }
    }
}
