package org.neo.back;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.neo.Log;

/**
 *
 * @author Troy Heninger
 */
public class Loader extends ClassLoader {

    private final File filePath;


    public Loader(String path) {
        filePath = new File(path);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = name.replaceAll("\\.", "/") + ".class";
        try {
            File file = new File(filePath, fileName);
            if (!file.isFile()) {
                throw new ClassNotFoundException(name);
            }
            InputStream in = new FileInputStream(file);
            try {
                byte[] buff = new byte[(int) file.length()];
                int index = 0;
                while (index < buff.length) {
                    int read = in.read(buff, index, buff.length - index);
                    if (read <= 0) break;
                    index += read;
                }
                return defineClass(name, buff, 0, index);
            } finally {
                in.close();
            }
        } catch (IOException ex) {
            Log.logger.severe(ex.toString());
            throw new ClassNotFoundException(name);
        }
    }

}
