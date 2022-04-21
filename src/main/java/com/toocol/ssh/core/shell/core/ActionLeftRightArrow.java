package com.toocol.ssh.core.shell.core;

import com.toocol.ssh.common.action.AbstractCharAction;
import com.toocol.ssh.common.event.CharEvent;
import com.toocol.ssh.common.utils.CharUtil;

/**
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/4/21 20:42
 */
public final class ActionLeftRightArrow extends AbstractCharAction {
    @Override
    public CharEvent[] watch() {
        return new CharEvent[]{CharEvent.LEFT_ARROW, CharEvent.RIGHT_ARROW};
    }

    @Override
    public boolean act(Shell shell, CharEvent charEvent, char inChar) {
        int cursorX = shell.term.getCursorPosition()._1();
        if (inChar == CharUtil.LEFT_ARROW) {
            if (cursorX > shell.prompt.get().length()) {
                shell.term.cursorLeft();
            }
        } else {
            if (cursorX < (shell.currentPrint.get().length() + shell.prompt.get().length())) {
                shell.term.cursorRight();
            }
        }
        return false;
    }
}