package de.pandalogic.elevator.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageUtil {

    private final Map<String, AtomicInteger> classes;
    private final String origin;
    private String message;

    public MessageUtil(String prefix, String message, boolean addPrefix) {
        String build = addPrefix ? (prefix + message) : message;
        this.origin = MessageUtil.translate(build);
        this.message = this.origin;
        this.classes = new HashMap<>();
    }

    public <T> MessageUtil replace(T object) {
        String key = object.getClass().getSimpleName();
        int index = 1;

        if (this.classes.containsKey(key)) {
            index = this.classes.get(key).incrementAndGet();
        } else {
            this.classes.put(key, new AtomicInteger(index));
        }

        String toReplace = "<" + object.getClass().getSimpleName() + index + ">";
        this.message = this.message.replace(toReplace, object.toString());
        return this;
    }

    public String getMessage() {
        String temp = this.message;
        this.message = this.origin;
        this.classes.clear();
        return temp;
    }

    public static String translate(String value) {
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public static String combineMessage(String[] args, int startIndex) {
        return String.join(" ", Arrays.copyOfRange(args, startIndex, args.length));
    }

}
