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
package net.daboross.bukkitdev.skywars.commands;

import lombok.NonNull;
import net.daboross.bukkitdev.commandexecutorbase.CommandExecutorBase;
import net.daboross.bukkitdev.skywars.api.SkyWars;
import net.daboross.bukkitdev.skywars.api.translations.SkyTrans;
import net.daboross.bukkitdev.skywars.api.translations.TransKey;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.CancelAllCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.CancelCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.ConfigurationDebugCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.ForceStartCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.JoinCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.LeaveCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.LobbyCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.RemoveLastPortalCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.SetLobbyCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.SetPortalCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.StatusCommand;
import net.daboross.bukkitdev.skywars.commands.mainsubcommands.VersionCommand;
import org.bukkit.command.PluginCommand;

public class MainCommand {

    private final SkyWars plugin;
    private final CommandExecutorBase base;

    public MainCommand(@NonNull SkyWars plugin) {
        this.plugin = plugin;
        this.base = new CommandExecutorBase(null);
        this.initCommands();
    }

    private void initCommands() {
        base.addSubCommand(new JoinCommand(plugin));
        base.addSubCommand(new LeaveCommand(plugin));
        base.addSubCommand(new SetLobbyCommand(plugin));
        base.addSubCommand(new SetPortalCommand(plugin));
        base.addSubCommand(new RemoveLastPortalCommand(plugin));
        base.addSubCommand(new CancelCommand(plugin));
        base.addSubCommand(new StatusCommand(plugin));
        base.addSubCommand(new VersionCommand(plugin));
        base.addSubCommand(new LobbyCommand(plugin));
        base.addSubCommand(new CancelAllCommand(plugin));
        base.addSubCommand(new ConfigurationDebugCommand(plugin));
        base.addSubCommand(new ForceStartCommand(plugin));
    }

    public void latchOnto(PluginCommand command) {
        if (command != null) {
            command.setDescription(SkyTrans.get(TransKey.MAIN_CMD_DESCRIPTION));
            command.setExecutor(base);
            command.setUsage("/<command>");
            command.setPermission(null);
        }
    }
}
