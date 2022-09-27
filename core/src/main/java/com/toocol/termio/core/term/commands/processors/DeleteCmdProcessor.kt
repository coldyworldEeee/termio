package com.toocol.termio.core.term.commands.processors

import com.toocol.termio.core.auth.AuthAddress
import com.toocol.termio.core.cache.CredentialCache
import com.toocol.termio.core.term.commands.TermCommandProcessor
import com.toocol.termio.core.term.core.Term
import com.toocol.termio.core.term.core.Term.Companion.promptLen
import com.toocol.termio.utilities.ansi.Printer.clear
import com.toocol.termio.utilities.utils.Tuple2
import io.vertx.core.AsyncResult
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import org.apache.commons.lang3.StringUtils

/**
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/4/1 18:55
 */
class DeleteCmdProcessor : TermCommandProcessor() {
    private val credentialCache = CredentialCache.Instance

    override fun process(eventBus: EventBus, cmd: String, resultAndMsg: Tuple2<Boolean, String?>): Any? {
        val split = cmd.trim { it <= ' ' }.replace(" {2,}".toRegex(), " ").split(" ").toTypedArray()
        if (split.size != 2) {
            resultAndMsg.first(false).second("Wrong 'delete' command, the correct pattern is 'delete index'.")
            return null
        }
        val indexStr = split[1]
        if (!StringUtils.isNumeric(indexStr)) {
            resultAndMsg.first(false).second("The index must be number.")
            return null
        }
        val index = indexStr.toInt()
        if (credentialCache.credentialsSize() < index) {
            resultAndMsg.first(false).second("The index correspond credential didn't exist.")
            return null
        }
        eventBus.request(AuthAddress.DELETE_CREDENTIAL.address(), index) { _: AsyncResult<Message<Any?>?>? ->
            clear()
            Term.instance.printScene(false)
            Term.instance.printTermPrompt()
            Term.instance.setCursorPosition(promptLen, Term.executeLine)
        }
        resultAndMsg.first(true)
        return null
    }
}