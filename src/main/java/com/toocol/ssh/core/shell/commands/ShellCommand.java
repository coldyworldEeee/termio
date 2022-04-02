package com.toocol.ssh.core.shell.commands;

import com.toocol.ssh.common.utils.Printer;
import com.toocol.ssh.core.shell.commands.processors.ShellClearCmdProcessor;
import com.toocol.ssh.core.shell.commands.processors.ShellExitCmdProcessor;
import com.toocol.ssh.core.shell.commands.processors.ShellHangCmdProcessor;
import com.toocol.ssh.core.shell.commands.processors.ShellTabCmdProcessor;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ZhaoZhe
 * @email joezane.cn@gmail.com
 * @date 2021/2/22 13:21
 */
public enum ShellCommand {
    /**
     * shell's command enums
     */
    CMD_EXIT("exit", new ShellExitCmdProcessor(), "Exit current shell, close ssh connection and destroy connect channel."),
    CMD_HANG("hang", new ShellHangCmdProcessor(), "Will not close the connection, exit shell with connection running in the background."),
    CMD_TAB("\t", new ShellTabCmdProcessor(), null),
    CMD_CLEAR("clear", new ShellClearCmdProcessor(), null);

    private final String cmd;
    private final ShellCommandProcessor commandProcessor;
    private final String comment;

    ShellCommand(String cmd, ShellCommandProcessor commandProcessor, String comment) {
        this.cmd = cmd;
        this.commandProcessor = commandProcessor;
        this.comment = comment;
    }

    public static Optional<ShellCommand> cmdOf(String cmd) {
        ShellCommand outsideCommand = null;
        for (ShellCommand command : values()) {
            if (command.cmd.equals(cmd)) {
                outsideCommand = command;
            }
        }
        return Optional.ofNullable(outsideCommand);
    }

    public final String processCmd(EventBus eventBus, Future<Long> future, long sessionId, AtomicBoolean isBreak) throws Exception {
        if (this.commandProcessor == null) {
            return "";
        }
        return this.commandProcessor.process(eventBus, future, sessionId, isBreak);
    }

    public String cmd() {
        return cmd;
    }

    public static void printHelp() {
        Printer.println();
        Printer.println("Shell commands:        [param] means optional param");
        for (ShellCommand command : values()) {
            if (command.comment == null) {
                continue;
            }
            Printer.println("\t" + command.cmd + "\t\t-- " + command.comment);
        }
        Printer.println();
    }
}
