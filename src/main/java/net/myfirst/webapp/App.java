package net.myfirst.webapp;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static public Connection getConnectionFromDb() throws Exception {
        String dbDiskURL = "jdbc:h2:file:./greeter";
        Jdbi jdbi = Jdbi.create(dbDiskURL, "sa", "");
        Handle handle = jdbi.open();
        handle.execute("create table if not exists person ( id integer identity, first_name text not null, counter int )");
        return DriverManager.getConnection(dbDiskURL, "sa", "");
    }

    public static void main(String[] args) {
//        port(8080);
//        try {
//            port(getHerokuAssignedPort());
//            //  Connection connection = getDatabaseConnection("postgres://uykckjdgwqzwgf:11738ace13c9d7600c1538335c9ce2260a65324142e8c412fbb0ee53b0608cba@ec2-54-157-78-113.compute-1.amazonaws.com:5432/d1qdis0thvaicr\n");
//
//            try (Connection connection = getDatabaseConnection("jdbc:postgresql://localhost/greeter?user=khanyiso&password=cairo123")) {
//
//
//            }
//            greetings greetings = new greetings(getDatabaseConnection("jdbc:postgresql://localhost/greeter?user=khanyiso&password=cairo123"));
//            List<String> allusers = new ArrayList<>();
////            staticFiles.location("/public");
//            get("/greet", (req, res) -> "Molo!");
//            get("/greet/:username", (req, res) -> greetings.getGreeting());
//            get("/greet/:username/language/:language", (req, res) -> "Cairo " + req.queryParams("language"));
//            post("/greet", (req, res) -> "Hello! " + req.queryParams("username"));
//
//
//            get("/hello", (req, res) -> {
//                Map<String, Object> map = new HashMap<>();
//                map.put("counter", allusers.size());
//                return new ModelAndView(map, "hello.handlebars");
//
//            }, new HandlebarsTemplateEngine());
//
//            post("/hello", (req, res) -> {
//
//                // create the greeting message
//                String user = req.queryParams("username");
//                String language = req.queryParams("language");
//
//                greeter.greet(user, language);
//
////            // put it in the map which is passed to the template - the value will be merged into the template
//                map.put("greeting", greeter.getGreeting());
//                map.put("counter", allusers.size());
//                map.put("users", allusers);
//
//                res.redirect("/hello");
//
//                return new ModelAndView(map, "hello.handlebars");
//
//            }, new HandlebarsTemplateEngine());
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

        try {
            Class.forName("org.h2.Driver");
            port(getHerokuAssignedPort());
            Map<String, Object> map = new HashMap<>();
            greetings greeter = new greetings(getConnectionFromDb());


            get("/hello", (request, response) -> new ModelAndView(map, "hello.handlebars"), new HandlebarsTemplateEngine());

            post("/greet", (request, response) -> {
                String user = request.queryParams("username");
                String language = request.queryParams("language");
                greeter.greet(user, language);

                map.put("greeting", greeter.getGreeting());
                map.put("counter", greeter.getNames().size());
                map.put("users", greeter.getNames());
                return new ModelAndView(map, "hello.handlebars");
            }, new HandlebarsTemplateEngine());


        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

//    static Connection getDatabaseConnection(String defualtJdbcUrl) throws URISyntaxException, SQLException {
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        String database_url = processBuilder.environment().get("DATABASE_URL");
//        if (database_url != null) {
//
//            URI uri = new URI(database_url);
//            String[] hostParts = uri.getUserInfo().split(":");
//            String username = hostParts[0];
//            String password = hostParts[1];
//            String host = uri.getHost();
//
//            int port = uri.getPort();
//
//            String path = uri.getPath();
//            String url = String.format("jdbc:postgresql://%s:%s%s", host, port, path);
//
//            return DriverManager.getConnection(url, username, password);
//
//        }
//
//        return DriverManager.getConnection(defualtJdbcUrl);
//
//    }
}

