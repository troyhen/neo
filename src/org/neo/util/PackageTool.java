package org.neo.util;

//package org.neo.tool;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.List;
//
//import org.neo.Compiler;
//import org.neo.ClassDef;
//
///**
// *
// * @author Troy Heninger
// */
//public class PackageTool {
//
//    /**
//     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
//     *
//     * @param packageName The base package
//     * @return The classes
//     * @throws ClassNotFoundException
//     * @throws IOException
//     */
//    public static ClassDef[] getClasses(String packageName)
//            throws ClassNotFoundException, IOException, URISyntaxException {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        assert classLoader != null;
//        String path = packageName.replace('.', '/');
//        Enumeration<URL> resources = classLoader.getResources(path);
//        List<File> dirs = new ArrayList<File>();
//        while (resources.hasMoreElements()) {
//            URL resource = resources.nextElement();
//            //dirs.add(new File(resource.getFile()));
//            dirs.add(new File(resource.toURI()));
//        }
//        ArrayList<ClassDef> classes = new ArrayList<ClassDef>();
//        for (File directory : dirs) {
//            classes.addAll(findClasses(directory, packageName));
//        }
//        return classes.toArray(new ClassDef[classes.size()]);
//    }
//
//    /**
//     * Recursive method used to find all classes in a given directory and subdirs.
//     *
//     * @param directory   The base directory
//     * @param packageName The package name for classes found inside the base directory
//     * @return The classes
//     * @throws ClassNotFoundException
//     */
//    private static List<ClassDef> findClasses(File directory, String packageName)
//            throws ClassNotFoundException {
//        List<ClassDef> classes = new ArrayList<ClassDef>();
//        if (!directory.exists()) {
//            return classes;
//        }
//        File[] files = directory.listFiles();
//        for (File file : files) {
//            /*if (file.isDirectory()) {
//                assert !file.getName().contains(".");
//                classes.addAll(findClasses(file, packageName + "." + file.getName()));
//            } else*/ if (file.isFile() && file.getName().endsWith(".class")) {
//                final String shortName = file.getName().substring(0, file.getName().length() - 6);
//                String className = packageName + '.' + shortName;
//                Class type = Compiler.compiler().loadClass(className);
//                ClassDef def = ClassDef.get(type);
//                classes.add(def);
//                Compiler.compiler().symbolAdd(shortName, def);
//            }
//        }
//        return classes;
//    }
//
//}
