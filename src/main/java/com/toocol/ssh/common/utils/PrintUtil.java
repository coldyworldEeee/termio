package com.toocol.ssh.common.utils;

/**
 * @author ZhaoZhe
 * @email joezane.cn@gmail.com
 * @date 2021/2/19 16:20
 */
public class PrintUtil {

    public static void println(String msg) {
        System.out.println("<SSH TERMINAL> " + msg);
    }

    public static void printTitle() {
        System.out.println("\n" +
                " _____ _____ _____    _____ _____ _____ _____ _____ _____ _____ __         _____ _____ _____ _____ _____ __    \n" +
                "|   __|   __|  |  |  |_   _|   __| __  |     |     |   | |  _  |  |       |_   _|     |     |     |     |  |   \n" +
                "|__   |__   |     |    | | |   __|    -| | | |-   -| | | |     |  |__    _  | | |  |  |  |  |   --|  |  |  |__ \n" +
                "|_____|_____|__|__|    |_| |_____|__|__|_|_|_|_____|_|___|__|__|_____|  |_| |_| |_____|_____|_____|_____|_____|\n" +
                "\n");
    }

    public static void printPromptScene() {
        printTitle();
        System.out.println("SSH TERMINAL start up success, enter commands to operate. <enter 'help' to get more command>");
    }

    public static void printHelp() {
        System.out.println("SSH TERMINAL HELP: ");
    }
}
