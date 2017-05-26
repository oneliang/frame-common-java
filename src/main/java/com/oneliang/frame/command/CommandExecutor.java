package com.oneliang.frame.command;

public abstract interface CommandExecutor {

    /**
     * execute command
     * @param args
     */
    public abstract void executeCommand(String[] args);
}
