package com.toocol.ssh.core.shell.core;

import com.toocol.ssh.common.action.AbstractCharAction;
import com.toocol.ssh.common.event.CharEvent;
import com.toocol.ssh.common.utils.CharUtil;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/4/21 20:40
 */
public final class ActionUpDownArrow extends AbstractCharAction {
    @Override
    public CharEvent[] watch() {
        return new CharEvent[]{CharEvent.UP_ARROW, CharEvent.DOWN_ARROW};
    }

    @Override
    public boolean act(Shell shell, CharEvent charEvent, char inChar) {
        shell.status = Shell.Status.NORMAL;

        if (inChar == CharUtil.UP_ARROW) {
            if (!shell.historyCmdHelper.isStart()) {
                if (shell.cmd.length() != 0 && StringUtils.isEmpty(shell.remoteCmd.get())) {
                    shell.historyCmdHelper.pushToDown(shell.cmd.toString());
                } else if (StringUtils.isNotEmpty(shell.remoteCmd.get())) {
                    byte[] write = "\u007F".repeat(shell.remoteCmd.get().length()).getBytes(StandardCharsets.UTF_8);
                    if (write.length > 0) {
                        shell.writeAndFlush(write);
                        String cmdToPush = shell.remoteCmd.get().toString().replaceAll("\u007F", "");
                        shell.historyCmdHelper.pushToDown(cmdToPush);
                    }
                }
            }
            shell.historyCmdHelper.up();
        } else {
            shell.historyCmdHelper.down();
        }
        localLastInputBuffer.delete(0, localLastInputBuffer.length()).append(shell.cmd);
        shell.localLastCmd.set(new StringBuffer());
        return false;
    }
}