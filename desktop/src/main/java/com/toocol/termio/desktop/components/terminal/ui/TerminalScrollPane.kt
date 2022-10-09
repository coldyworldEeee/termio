package com.toocol.termio.desktop.components.terminal.ui

import com.toocol.termio.platform.component.IComponent
import com.toocol.termio.platform.component.IStyleAble
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import org.fxmisc.flowless.VirtualizedScrollPane

/**
 * @author ：JoeZane (joezane.cn@gmail.com)
 * @date: 2022/8/11 23:15
 * @version: 0.0.1
 */
class TerminalScrollPane(terminalEmulatorTextArea: TerminalEmulatorTextArea?) :
    VirtualizedScrollPane<TerminalEmulatorTextArea?>(terminalEmulatorTextArea), IStyleAble, IComponent {
    override fun styleClasses(): Array<String> {
        return arrayOf(
            "terminal-scroll-pane"
        )
    }

    override fun initialize() {
        styled()
        vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
        hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
    }

    override fun sizePropertyBind(major: Pane, widthRatio: Double?, heightRatio: Double?) {

    }
}