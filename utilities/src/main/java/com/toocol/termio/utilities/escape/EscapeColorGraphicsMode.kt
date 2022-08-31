package com.toocol.termio.utilities.escape

enum class EscapeColorGraphicsMode(val code: Int) : IEscapeMode {
    RESET_ALL_MODE(0),
    BOLD_MODE(1),
    DIM_FAINT_MODE(2),
    RESET_BOLD_DIM_FAINT(22),
    ITALIC_MODE(3),
    RESET_ITALIC(23),
    UNDERLINE_MODE(4),
    RESET_UNDERLINE(24),
    BLINKING_MODE(5),
    RESET_BLINKING(25),
    INVERSE_REVERSE_MODE(7),
    RESET_INVERSE_REVERSE(27),
    HIDDEN_VISIBLE_MODE(8),
    RESET_HIDDEN_VISIBLE(28),
    STRIKETHROUGH_MODE(9),
    RESET_STRIKETHROUGH(29);


    companion object {
        fun codeOf(code: Int): EscapeColorGraphicsMode? {
            for (mode in values()) {
                if (mode.code == code) {
                    return mode
                }
            }
            return null
        }
    }
}