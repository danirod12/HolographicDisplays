/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import org.bukkit.command.CommandSender;

public class AlignCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public AlignCommand(InternalHologramEditor hologramEditor) {
        super("align");
        setMinArgs(3);
        setUsageArgs("<X | Y | Z | XZ> <hologramToAlign> <referenceHologram>");
        setDescription("Aligns a hologram to another along the specified axis.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[1]);
        InternalHologram referenceHologram = hologramEditor.getExistingHologram(args[2]);

        CommandValidate.check(hologram != referenceHologram, "The holograms must not be the same.");

        ImmutablePosition referencePosition = referenceHologram.getPosition();
        ImmutablePosition newPosition = hologram.getPosition();

        String axis = args[0];
        if (axis.equalsIgnoreCase("x")) {
            newPosition = newPosition.withX(referencePosition.getX());
        } else if (axis.equalsIgnoreCase("y")) {
            newPosition = newPosition.withY(referencePosition.getY());
        } else if (axis.equalsIgnoreCase("z")) {
            newPosition = newPosition.withZ(referencePosition.getZ());
        } else if (axis.equalsIgnoreCase("xz")) {
            newPosition = newPosition.withX(referencePosition.getX());
            newPosition = newPosition.withZ(referencePosition.getZ());
        } else {
            throw new CommandException("You must specify either X, Y, Z or XZ, " + axis + " is not a valid axis.");
        }

        hologram.setPosition(newPosition);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_POSITION);

        sender.sendMessage(ColorScheme.PRIMARY + "Hologram \"" + hologram.getName() + "\""
                + " aligned to the hologram \"" + referenceHologram.getName() + "\""
                + " on the " + axis.toUpperCase() + " axis.");
    }

}
