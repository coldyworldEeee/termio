package com.toocol.ssh.core.term.core;

import com.toocol.ssh.utilities.utils.StrUtil;

/**
 * @author ：JoeZane (joezane.cn@gmail.com)
 * @date: 2022/4/22 22:03
 * @version: 0.0.1
 */
public record TermPrinter(Term term) {

    public static volatile String displayBuffer = StrUtil.EMPTY;
    public static volatile String commandBuffer = StrUtil.EMPTY;

    synchronized void cleanDisplayZone() {
        term.setCursorPosition(0, Term.executeLine + 1);
        int windowWidth = term.getWidth();
        while (term.getCursorPosition()._2() < term.displayZoneBottom) {
            Printer.println(" ".repeat(windowWidth));
        }
    }

    synchronized void printDisplay(String msg) {
        displayBuffer = msg;
        term.hideCursor();
        cleanDisplayZone();
        term.setCursorPosition(0, Term.executeLine + 2);
        Printer.print(msg);
        term.displayZoneBottom = term.getCursorPosition()._2() + 1;
        term.setCursorPosition(Term.PROMPT.length(), Term.executeLine);
        term.showCursor();
    }

    synchronized void printDisplayBuffer() {
        cleanDisplayZone();
        term.setCursorPosition(0, Term.executeLine + 2);
        Printer.print(displayBuffer);
        term.displayZoneBottom = term.getCursorPosition()._2() + 1;
        term.setCursorPosition(0, Term.executeLine);
    }

    synchronized void printCommandBuffer() {
        term.setCursorPosition(Term.PROMPT.length(), Term.executeLine);
        Printer.print(HighlightHelper.assembleColorBackground(commandBuffer, Term.theme.executeLineBackgroundColor));
    }

    synchronized void printExecution(String msg) {
        commandBuffer = msg;
        term.hideCursor();
        term.setCursorPosition(Term.PROMPT.length(), Term.executeLine);
        Printer.print(HighlightHelper.assembleColorBackground(" ".repeat(term.getWidth() - Term.PROMPT.length()), Term.theme.executeLineBackgroundColor));
        term.setCursorPosition(Term.PROMPT.length(), Term.executeLine);
        Printer.print(HighlightHelper.assembleColorBackground(msg, Term.theme.executeLineBackgroundColor));
        term.setCursorPosition(term.executeCursorOldX.get(), Term.executeLine);
        term.showCursor();
    }
}