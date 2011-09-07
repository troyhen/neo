package org.neo.tool;

import java.util.List;
import java.io.File;
import org.neo.NeoLang;
import static org.neo.Version.VERSION;

/**
 *
 * @author Troy Heninger
 */
public class NeoC {

    public static void main(String[] args) {
        Argv argv = new Argv(args, "Neo Compiler (version " + VERSION + ")", "-j, --java", "Compile to java source", "-?, --help", "Display usage instructions");
        if (argv.has("-?")) {
            argv.help();
            return;
        }
        boolean javaOnly = argv.has("-j");
        NeoLang lang = new NeoLang();
        List<String> files = argv.getFiles();
        String bad = argv.getUnknownOption();
        if (bad != null) {
            argv.help("Unknown Option: " + bad);
            System.exit(1);
        }
        if (files.isEmpty()) {
            argv.help("Missing filename to compile");
            System.exit(1);
        }
        for (String file : files) {
            File neoFile = new File(file);
            int dot = file.lastIndexOf('.');
            if (dot < 0) dot = file.length();
            File javaFile = new File(file.substring(0, dot) + ".java");
            lang.set("file", neoFile.getPath());
            lang.set("save", javaFile.getPath());
            if (!javaOnly) lang.set("class", "javac");
            lang.compile();
        }
        System.exit(0);
    }
}
