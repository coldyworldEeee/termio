package com.toocol.termio.console.term.handlers;

import com.toocol.termio.core.term.TermAddress;
import com.toocol.termio.core.term.commands.TermCommand;
import com.toocol.termio.core.term.core.Term;
import com.toocol.termio.utilities.ansi.AnsiStringBuilder;
import com.toocol.termio.utilities.ansi.Printer;
import com.toocol.termio.utilities.module.IAddress;
import com.toocol.termio.utilities.module.NonBlockingMessageHandler;
import com.toocol.termio.utilities.utils.Tuple2;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/3/30 11:09
 */
public final class ExecuteCommandHandler extends NonBlockingMessageHandler {

    private final Term term = Term.instance;

    public ExecuteCommandHandler(Vertx vertx, Context context) {
        super(vertx, context);
    }

    @NotNull
    @Override
    public IAddress consume() {
        return TermAddress.EXECUTE_OUTSIDE;
    }

    @Override
    public <T> void handleInline(@NotNull Message<T> message) {
        String cmd = String.valueOf(message.body());

        Tuple2<Boolean, String> resultAndMessage = new Tuple2<>();
        AtomicBoolean isBreak = new AtomicBoolean();
        boolean isCommand = TermCommand.cmdOf(cmd)
                .map(termCommand -> {
                    try {
                        termCommand.processCmd(eventBus, cmd, resultAndMessage);
                        if ((TermCommand.CMD_NUMBER.equals(termCommand) || TermCommand.CMD_MOSH.equals(termCommand))
                                && resultAndMessage._1()) {
                            isBreak.set(true);
                        }
                    } catch (Exception e) {
                        Printer.printErr("Execute command failed, message = " + e.getMessage());
                    }
                    return true;
                }).orElse(false);

        String msg = resultAndMessage._2();
        if (StringUtils.isNotEmpty(msg)) {
            term.printDisplay(msg);
        } else {
            term.cleanDisplayBuffer();
        }

        if (!isCommand && StringUtils.isNotEmpty(cmd)) {
            AnsiStringBuilder builder = new AnsiStringBuilder()
                    .background(Term.theme.displayBackGroundColor.color)
                    .front(Term.theme.commandHighlightColor.color)
                    .append(cmd)
                    .deFront()
                    .append(": command not found.");
            term.printDisplay(builder.toString());
        }

        message.reply(isBreak.get());
    }
}
