import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class TwentySix {
    static Connection connection = null;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide exactly One argument");
            System.exit(1);
        }
        try{
            // create db connection
            connection = DriverManager.getConnection("jdbc:sqlite:262pTF.db");
            System.out.println("DB connected");

            createDbSchema(connection);
            loadDataIntoDb(args[0],connection);
            queryDb(connection);

        }catch( SQLException e){
            System.out.println(e);
            System.exit(1);
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("DB connection close");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private static void createDbSchema(Connection connection) {
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate("drop table if exists words");
            statement.executeUpdate("create table words (id INTEGER PRIMARY KEY AUTOINCREMENT, value)");
            statement.executeUpdate("drop table if exists characters");
            statement.executeUpdate("create table characters (id INTEGER word_id INTEGER, value)");
            statement.close();
//            DatabaseMetaData meta = connection.getMetaData();
//            ResultSet resultSet = meta.getTables(null, null, "words", new String[] {"TABLE"});
//            System.out.println(resultSet.next());

        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private static void loadDataIntoDb(String filePath, Connection connection){
        List<String> validWords = extractWord(filePath);

        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select MAX(id) from words");

            int row =0;
            while(rs.next()){
                row = rs.getInt("MAX(id)");
            }
            PreparedStatement insertWordStmt = connection.prepareStatement("insert into words VALUES (?, ?)");
            for (String word: validWords){
                insertWordStmt.setInt(1,row);
                insertWordStmt.setString(2,word);
                insertWordStmt.executeUpdate();
                row++;
            }
            statement.close();
        }catch(SQLException e){
            System.out.println(e);
        }

    }
    private static void queryDb(Connection connection){
        try{
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("SELECT value, COUNT(*) as C FROM words GROUP BY value ORDER BY C DESC");
            int i = 0;


            System.out.println("====== Normal Extract and Count ======");
            while (rs.next()){
                if (i == 25) break;
                System.out.println(rs.getString("value") + " - " + rs.getInt("C"));
                i++;
            }
            ResultSet uniqueWordsWithZ = statement.executeQuery("SELECT COUNT(DISTINCT value) as C FROM words WHERE value LIKE '%z%'");
            System.out.println("====== Extract Unique Words with Letter Z ======");
            while (uniqueWordsWithZ.next()){
                System.out.println("Unique Words with Z: " + uniqueWordsWithZ.getInt("C"));
            }
        }catch(SQLException e){
            System.out.println(e);
        }

    }

    private static List<String> extractWord(String filePath){
        List<String> validWords = null;
        try {
            // read the stop words
            Set<String> stopWords = new HashSet<>(Arrays.asList(new Scanner(new File("../../stop_words.txt"))
                    .useDelimiter("\\Z").next().split(",")));
            // read the entire file
            String input = new Scanner(new File(filePath)).useDelimiter("\\Z").next().toLowerCase();

            /* filter the word with lambda expression predicate
             */
            validWords = Arrays.stream(input.split("[^a-z]+"))
                    .filter(word -> word.length() >=2 && !stopWords.contains(word)).toList();
        } catch (IOException e) {
            System.out.println(e);
        }
        return validWords;
    }
}
