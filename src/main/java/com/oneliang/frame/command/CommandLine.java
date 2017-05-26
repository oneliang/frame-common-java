package com.oneliang.frame.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oneliang.util.common.StringUtil;

public class CommandLine {

    private int maxCommandKeyLength = 1;
    private final Map<String, CommandExecutor> commandExecutorMap = new ConcurrentHashMap<String, CommandExecutor>();

    public CommandLine(int maxCommandKeyLength) {
        this.maxCommandKeyLength = maxCommandKeyLength;
    }

    /**
     * add command executor
     * 
     * @param commandKey
     * @param commandExecutor
     */
    public void addCommandExecutor(String commandKey, CommandExecutor commandExecutor) {
        this.commandExecutorMap.put(commandKey, commandExecutor);
    }

    /**
     * execute
     * 
     * @param args
     */
    public void execute(String[] args) {
        if (args == null || args.length == 0) {
            return;
        }

        for (int i = this.maxCommandKeyLength; i > 0; i--) {
            String[] commandArray = new String[i];
            for (int j = 0; j < i; j++) {
                commandArray[j] = args[j];
            }
            String commandKey = toCommandKey(commandArray);
            if (!this.commandExecutorMap.containsKey(commandKey)) {
                continue;
            }
            CommandExecutor commandExecutor = this.commandExecutorMap.get(commandKey);
            commandExecutor.executeCommand(args);
            break;
        }
    }

    /**
     * destroy
     */
    public void destroy() {
        this.commandExecutorMap.clear();
    }

    /**
     * parse
     * 
     * @param command
     * @return String[]
     */
    public static String[] parse(String command) {
        if (StringUtil.isBlank(command)) {
            return null;
        }
        List<String> argumentList = new ArrayList<String>();
        String[] array = command.split(StringUtil.SPACE);
        for (String string : array) {
            if (StringUtil.isBlank(string)) {
                continue;
            }
            argumentList.add(string.trim());
        }
        return argumentList.toArray(new String[0]);
    }

    /**
     * to command key
     * 
     * @param commandArray
     * @return String
     */
    public static String toCommandKey(String[] commandArray) {
        if (commandArray == null || commandArray.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String command : commandArray) {
            stringBuilder.append(command.trim());
            stringBuilder.append(StringUtil.SPACE);
        }
        int length = stringBuilder.length();
        if (length > 0) {
            stringBuilder.delete(length - 1, length);
        }
        return stringBuilder.toString();
    }
}
