import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;

public class framework {

    public static void main(String[] args) {

        Properties props = new Properties();
        try {
            //loading Plugins
            props.load(new FileReader("config.properties"));
            String extractPluginName = props.getProperty("extractPlugin");
            String countPluginName = props.getProperty("countPlugin");

            // get URL pointer
            URL extractURL = new File(extractPluginName + ".jar").toURI().toURL();
            URL countURL = new File(countPluginName + ".jar").toURI().toURL();
            URL[] urls = new URL[]{extractURL,countURL};

            // Reflection
            ClassLoader clsLoader = new URLClassLoader(urls);
            Class<?> extractCls = clsLoader.loadClass(extractPluginName);
            Class<?> countCls = clsLoader.loadClass(countPluginName);

            IExtract extractType = (IExtract) extractCls.getDeclaredConstructor().newInstance();
            ICount countType = (ICount) countCls.getDeclaredConstructor().newInstance();

            // extract count word
            Map<String, Integer> termFreq = countType.count(extractType.extract(args[0]));

            // print result
            termFreq.entrySet().stream()
                    .sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
                    .limit(25)
                    .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));


        }catch(IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
               InvocationTargetException e){
            System.out.println(e);
    }


    }
}
