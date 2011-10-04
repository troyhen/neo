package org.neo.back;

/**
 *
 * @author Troy Heninger
 */
public class CodeBuilder {
    public static final String TAB = "    ";
    public static final String EOL = "\n";

    private StringBuilder tabs = new StringBuilder();
    private StringBuilder buff = new StringBuilder();

    public CodeBuilder() {
    }

    public CodeBuilder(char obj) {
        buff.append(obj);
    }

    public CodeBuilder(Object obj) {
        buff.append(obj);
    }

    public CodeBuilder append(char obj) {
        buff.append(obj);
        return this;
    }

    public CodeBuilder append(Object obj) {
        buff.append(obj);
        return this;
    }

    public CodeBuilder eol() {
        buff.append(EOL);
        return this;
    }
    
    public void insert(int offset, CharSequence string) {
        buff.insert(offset, string);
    }

    public int lastIndexOf(char ch) {
        int ix = buff.length() - 1;
        while (ix >= 0) {
            if (buff.charAt(ix) == ch) break;
        }
        return ix;
    }

    public CodeBuilder println(CharSequence text) {
        return tab().append(text).eol();
    }
    
    public CodeBuilder println(Object obj) {
        return tab().append(obj).eol();
    }

    public CodeBuilder tab() {
        buff.append(tabs);
        return this;
    }

    public CharSequence tabs() {
        return tabs;
    }
    
    public CodeBuilder tabMore() {
        tabs.append(TAB);
        return this;
    }

    public CodeBuilder tabLess() {
        if (tabs.length() >= TAB.length()) {
            tabs.setLength(tabs.length() - TAB.length());
        }
        return this;
    }

    @Override
    public String toString() {
        return buff.toString();
    }


}
