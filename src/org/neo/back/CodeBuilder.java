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

    public String getTabs() {
        return tabs.toString();
    }
    
    public void insert(int offset, CharSequence string) {
        buff.insert(offset, string);
    }

    public boolean isAtBol() {
        final int last = buff.length() - 1;
        if (last < 0) return true;
        char ch = buff.charAt(last);
        return ch == '\r' || ch == '\n';
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

    public int length() {
        return buff.length();
    }

    public void setTabs(String tabs) {
        this.tabs.setLength(0);
        this.tabs.append(tabs);
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
