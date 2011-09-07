package org.neo.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Troy Heninger
 */
public class Argv {

    private String description;
    private String[] options;
    private Map<String, String> map = new HashMap<String, String>();
    private List<String> list = new ArrayList<String>();
    private int longestKey;

    public Argv(String[] args, String description, String...options) {
        this.description= description;
        this.options = options;
        list.addAll(Arrays.asList(args));
        Pattern pat = Pattern.compile("([^,]+)(,\\s+([^,]+))?(\\s+(.+))?");
        for (int index = 0, end = options.length; index < end;) {
            String option = options[index++];
            String desc = options[index++];
            longestKey = Math.max(longestKey, option.length());
            Matcher match = pat.matcher(option);
            if (match.matches()) {
                String key1 = match.group(1);
                String key2 = match.group(3);
                String param = match.group(5);
                add(key1, key2, param);
            }
        }
    }

    private void add(String key1, String key2, String param) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String arg = it.next();
            if (arg.equals(key1) || arg.equals(key2)) {
                it.remove();
                String value = "";
                if (param != null && it.hasNext()) {
                    value = it.next();
                    it.remove();
                }
                map.put(key1, value);
                if (key2 != null) map.put(key2, value);
            }
        }
    }

//    public String get(int index) {
//        return args[index];
//    }

    public String get(String key) {
        if (key.startsWith("-")) key = key.substring(1);
        return map.get(key);
    }

    public List<String> getFiles() {
        return list;
    }

    public boolean has(String key) {
        return map.containsKey(key);
    }

    public String getUnknownOption() {
        for (String name : list) {
            if (name.startsWith("-")) return name;
        }
        return null;
    }

    public void help() {
        help(null);
    }

    public void help(String msg) {
        System.out.println(description);
        if (msg != null) {
            System.out.println(msg);
        }
        System.out.println("Usage:");
        for (int index = 0, end = options.length; index < end;) {
            String option = options[index++];
            String desc = options[index++];
            System.out.format("  %-" + longestKey + "s  %s\n", option, desc);
        }
    }
    
}
