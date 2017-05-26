package com.oneliang.test.command;

import com.oneliang.frame.command.CommandExecutor;
import com.oneliang.frame.command.CommandLine;

public class TestCommand {

    private static final String COMMAND_GIT = "git";
    private static final String COMMAND_GIT_PULL = "git pull";

    public static void main(String[] args) {
        String command = "git pull -t a";
        CommandLine commandLine = new CommandLine(2);
        args = CommandLine.parse(command);
        commandLine.addCommandExecutor(COMMAND_GIT, new CommandExecutor() {
            public void executeCommand(String[] args) {
                System.out.println("git");
            }
        });
        commandLine.addCommandExecutor(COMMAND_GIT_PULL, new CommandExecutor() {
            public void executeCommand(String[] args) {
                System.out.println("git pull");
            }
        });
        commandLine.execute(args);
    }
}
