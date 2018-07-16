import javax.xml.transform.Result;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created by condor on 26/02/15.
 * FastTrackIT, 2015
 * <p/>
 * DEMO ONLY PURPOSES, IT MIGHT CONTAINS INTENTIONALLY ERRORS OR ESPECIALLY BAD PRACTICES
 *
 * make sure you refactor it and remove lots of bad practices like loading the driver multiple times or
 * repeating the same common code multiple times
 *
 * BTW, exercise 1: how we reorg this/refactor in small pieces
 */
public class DemoCRUDOperations {


    public static void main(String[] args) {
        System.out.println("Hello database users! We are going to call DB from Java");
        try {

            login();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    private static void posteaza(long id, String mesaj) throws ClassNotFoundException, SQLException {

        // 1. load driver, no longer needed in new versions of JDBC
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/flavius8";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        PreparedStatement pSt = conn.prepareStatement("INSERT INTO post (tweet, fkusername) VALUES (?,?)");
        pSt.setString(1,  mesaj);
        pSt.setLong(2, id);

        pSt.executeUpdate();
        // 6. close the objects
        pSt.close();
        conn.close();
    }

    private static void following(long id) throws ClassNotFoundException, SQLException{
        Class.forName("org.postgresql.Driver");

        final String URL = "jdbc:postgresql://54.93.65.5:5432/flavius8";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        Connection conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);

        PreparedStatement pSt = conn.prepareStatement("SELECT l2.username FROM login l1 INNER JOIN followers f ON f.idsta = l1.id INNER JOIN login l2 ON l2.id = f.idfol WHERE l1.id = ?");
        pSt.setLong(1,id);
        ResultSet rs = pSt.executeQuery();
        String[] name = new String[1000];
        int temp=0;
        while(rs.next()){
            name[temp]=rs.getString("username").trim();
            temp++;
        }

        System.out.print("\nYou're following: " );
        for(int i=0;i<name.length;i++){
            if(name[i]!=null){
                System.out.print(name[i]+", ");
            }
        }
        System.out.println("");

    }

    private static void feed(long id) throws ClassNotFoundException, SQLException{
        Class.forName("org.postgresql.Driver");

        final String URL = "jdbc:postgresql://54.93.65.5:5432/flavius8";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        Connection conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);

        PreparedStatement pSt = conn.prepareStatement("SELECT l2.username, p.tweet FROM login l1 INNER JOIN followers f ON f.idsta = l1.id INNER JOIN login l2 ON l2.id = f.idfol INNER JOIN post p ON P.fkusername = f.idfol WHERE l1.id = ?");
        pSt.setLong(1,id);
        ResultSet rs = pSt.executeQuery();
        String[] name = new String[1000];
        String[] tweet = new String[1000];
        int temp=0;
        while (rs.next()){
            name[temp]=rs.getString("username").trim();
            tweet[temp]=rs.getString("tweet").trim();
            temp++;
        }

        for(int i = 0; i<name.length;i++){
            if(name[i]!=null){
                System.out.println("User: "+name[i]+" | Tweet: "+tweet[i]);
            }
        }
        System.out.println("");

    }

    private static void menu(long id) throws ClassNotFoundException, SQLException {
        int option = 0;
        while (option != 4) {
            System.out.print("\n\t\t1.Tweet\n\t\t2.Following\n\t\t3.Feed\n\t\t4.EXIT\n");
            System.out.print("\nPlease select an option: ");
            option = new Scanner(System.in).nextByte();
            System.out.println("");
            switch (option) {
                case 1:
                    System.out.print("Tweet: ");
                    String mesaj = new Scanner(System.in).nextLine();
                    posteaza(id, mesaj);
                    break;
                case 2:
                    following(id);
                    break;
                case 3:
                    feed(id);
                    break;
                case 4:
                    System.out.println("Goodbye.");break;
                default:
                    System.out.println("Option not valid.");
                    break;
            }
        }
    }

    private static void login() throws ClassNotFoundException, SQLException{
        long read = 0;
        boolean result = false;


        do{
            Scanner input = new Scanner(System.in);
            if(read == 0) {
                System.out.print("Username: ");
                String u = input.nextLine().trim();
                System.out.print("Password: ");
                String p = input.next().trim();

                read = demoRead(u, p);
            }
            if(read!=0){
                menu(read);
            }
        }while(read==0);
    }

    private static long demoRead(String u, String p) throws ClassNotFoundException, SQLException {
               // 1. load driver, no longer needed in new versions of JDBC
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/flavius8";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        PreparedStatement pst = conn.prepareStatement("SELECT id FROM login WHERE username=? AND password=?");
        pst.setString(1, u);
        pst.setString(2, p);

        // 5. execute a query
        ResultSet rs = pst.executeQuery();
        // 6. iterate the result set and print the values
        long id = 0;
        while (rs.next()) {
            id = rs.getLong("id");
            if(id!=0){
                break;
            }
            id=0;
        }

            // 7. close the objects
            rs.close();
            pst.close();
            conn.close();
            return id;
        }


    private static void demoUpdate() throws ClassNotFoundException, SQLException {

                // 1. load driver, no longer needed in new versions of JDBC
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/flavius8";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        PreparedStatement pSt = conn.prepareStatement("UPDATE login SET username=?, password=? WHERE id=?"); //so we have 3 params
        pSt.setString(1, "ionelcondor");
        pSt.setString(2, "password1");
        pSt.setLong(3, 3);

        // 5. execute a prepared statement
        int rowsUpdated = pSt.executeUpdate();

        // 6. close the objects
        pSt.close();
        conn.close();
    }


    private static void demoDelete() throws ClassNotFoundException, SQLException {

               // 1. load driver, no longer needed in new versions of JDBC
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/flavius8";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        PreparedStatement pSt = conn.prepareStatement("DELETE FROM login WHERE username=?");
        pSt.setString(1,"marius" );

        // 5. execute a prepared statement
        int rowsDeleted = pSt.executeUpdate();
        System.out.println(rowsDeleted + " rows were deleted.");
        // 6. close the objects
        pSt.close();
        conn.close();
    }
}

