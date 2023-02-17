package Week5;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClass {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 1){
            System.out.println(" Please provide One jar file name");
            System.exit(1);
        }
        // get the URL of jarfile for classLoader
        File file = new File(args[0]);
        URL url = file.toURI().toURL();
//        System.out.println(url);
        URL[] urls  = new URL[]{url};
        ClassLoader classLoader = new URLClassLoader(urls);

        // get all classes in jar
        JarFile jarFile = new JarFile(args[0]);
        List<String> classList = new ArrayList<>();
        final Enumeration<JarEntry>jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()){
            JarEntry jarEntry = jarEntries.nextElement();
            if (jarEntry.getName().endsWith(".class")){
                classList.add(jarEntry.getName().replace("/",".").replace(".class",""));
            }
        }
        jarFile.close();

        // sort the classList
        Collections.sort(classList);

        // count result
        for (String className : classList){
            int publicMethods = 0;
            int privateMethods = 0;
            int protectedMethods = 0;
            int staticMethods = 0;
            try{

                Class<?> cls = Class.forName(className,true,classLoader);
                Method[] methods = cls.getDeclaredMethods();
                for (Method method : methods) {
                    if (java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                        publicMethods++;
                    }
                    if (java.lang.reflect.Modifier.isPrivate(method.getModifiers())) {
                        privateMethods++;
                    }
                    if (java.lang.reflect.Modifier.isProtected(method.getModifiers())) {
                        protectedMethods++;
                    }
                    if (java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                        staticMethods++;
                    }
                }

                System.out.printf("----------%s----------%n",className);
                System.out.println("  Public methods: " + publicMethods);
                System.out.println("  Private methods: " + privateMethods);
                System.out.println("  Protected methods: " + protectedMethods);
                System.out.println("  Static methods: " + staticMethods);
                Field[] fieldsArray = cls.getDeclaredFields();
                System.out.println("  Fields: " + fieldsArray.length);
            }catch(ClassNotFoundException e){
                System.out.printf("Class: %s not found",className);
            }


        }
    }
}
