//package org.neo.tool;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.ArrayList;
//import java.util.List;
//import org.neo.Log;
//import org.xeustechnologies.jcl.JarClassLoader;
//
///**
// *
// * @author Troy Heninger
// */
//public class NeoClassLoader extends JarClassLoader {
//
//    private static NeoClassLoader loader;
//
//    public NeoClassLoader(Object[] sources) {
//        super(sources);
//    }
//
//    public NeoClassLoader(List sources) {
//        super(sources);
//    }
//
////    public NeoClassLoader(URL[] urls) {
////        super(urls);
////    }
//
//    public static NeoClassLoader getInstance() {
//        if (loader == null) {
//            String classPath = System.getProperty("java.class.path");
////            List<URL> list = new ArrayList<URL>();
//            List list = new ArrayList();
//            String home = System.getProperty("java.home");
////            try {
////                list.add(new URL("file://" + home + "/lib/rt.jar"));
////                list.add(new URL("file://" + home + "/jre/lib/rt.jar"));
//                list.add(home + "/lib/rt.jar");
//                list.add(home + "/jre/lib/rt.jar");
////            } catch (MalformedURLException ex) {
////                Log.config("Invalid path in java.home: " + home);
////            }
//            for (String path : classPath.split(File.pathSeparator)) {
////                try {
////                    list.add(new URL("file://" + path));
//                    list.add(path);
////                } catch (MalformedURLException ex) {
////                    Log.config("Invalid path in classpath: " + path);
////                }
//            }
////            loader = new NeoClassLoader(list.toArray(new URL[list.size()]));
//            loader = new NeoClassLoader(list.toArray(new Object[list.size()]));
//        }
//        return loader;
//    }
//
//}
