package com.toocol.termio.desktop.ui.executor

import com.toocol.termio.desktop.ui.panel.WorkspacePanel
import com.toocol.termio.platform.console.MetadataPrinterOutputStream
import com.toocol.termio.platform.console.MetadataReaderInputStream
import com.toocol.termio.platform.ui.TBorderPane
import com.toocol.termio.utilities.ansi.Printer.setPrinter
import com.toocol.termio.utilities.log.Loggable
import com.toocol.termio.utilities.utils.StrUtil
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets

/**
 * @author ：JoeZane (joezane.cn@gmail.com)
 * @date: 2022/8/12 0:42
 * @version: 0.0.1
 */
class CommandExecutorPanel(id: Long) : TBorderPane(id), Loggable {
    private val executorOutputService = ExecutorOutputService()
    private val commandExecutorInput: CommandExecutorInput
    private val commandExecutorResultTextArea: CommandExecutorResultTextArea
    private val commandExecutorResultScrollPane: CommandExecutorResultScrollPane

    init {
        commandExecutorInput = CommandExecutorInput(id)
        commandExecutorResultTextArea = CommandExecutorResultTextArea(id)
        commandExecutorResultScrollPane = CommandExecutorResultScrollPane(id, commandExecutorResultTextArea)
    }

    override fun styleClasses(): Array<String> {
        return arrayOf(
            "command-executor-panel"
        )
    }

    override fun initialize() {
        apply {
            styled()
            val workspacePanel = findComponent(WorkspacePanel::class.java, id)
            workspacePanel.bottom = this
            prefWidthProperty().bind(workspacePanel.prefWidthProperty().multiply(1))
            prefHeightProperty().bind(workspacePanel.prefHeightProperty().multiply(0.3))

            focusedProperty().addListener { _: ObservableValue<out Boolean>?, _: Boolean?, newVal: Boolean ->
                if (newVal) {
                    setPrinter(commandExecutorPrintStream)
                    println("Executor get focus")
                } else {
                    println("Executor lose focus")
                }
            }
        }

        executorOutputService.apply { start() }
        commandExecutorResultTextArea.apply { initialize() }
        commandExecutorResultScrollPane.apply { initialize() }
        commandExecutorInput.apply {
            initialize()
            addEventFilter(KeyEvent.KEY_TYPED) { event: KeyEvent ->
                if (StrUtil.isNewLine(event.character)) {
                    try {
                        executorReaderInputStream.write((commandExecutorInput.text + StrUtil.LF).toByteArray(StandardCharsets.UTF_8))
                        executorReaderInputStream.flush()
                    } catch (e: IOException) {
                        error("Write to reader failed, msg = ${e.message}")
                    }
                    event.consume()
                }
            }
            focusedProperty()
                .addListener { _: ObservableValue<out Boolean>?, _: Boolean?, newVal: Boolean ->
                    if (newVal) {
                        setPrinter(commandExecutorPrintStream)
                        println("Executor input get focus")
                    } else {
                        println("Executor input lose focus")
                    }
                }
            onMouseClicked = EventHandler { commandExecutorInput.requestFocus() }
        }
    }

    override fun actionAfterShow() {
        commandExecutorInput.requestFocus()
    }

    private inner class ExecutorOutputService : Loggable {
        fun start() {
            val thread = Thread({
                while (true) {
                    try {
                        if (commandExecutorPrinterOutputStream.available() > 0) {
                            val text = commandExecutorPrinterOutputStream.read()
                            Platform.runLater { commandExecutorResultTextArea.append(text) }
                        }
                        Thread.sleep(1)
                    } catch (e: Exception) {
                        warn("TerminalOutputService catch exception, e = ${e.javaClass.name}, msg = ${e.message}")
                    }
                }
            }, "terminal-output-service")
            thread.isDaemon = true
            thread.start()
        }
    }

    companion object {
        /**
         * CommandExecutorPanel has only one MetadataReaderInputStream:
         * Get user's input data.
         */
        @JvmField
        val executorReaderInputStream = MetadataReaderInputStream()

        /**
         * CommandExecutorPanel has only one MetadataPrinterOutputStream and PrintStream correspondent:
         * Feedback data.
         */
        val commandExecutorPrinterOutputStream = MetadataPrinterOutputStream()

        @JvmField
        val commandExecutorPrintStream = PrintStream(commandExecutorPrinterOutputStream)
    }
}