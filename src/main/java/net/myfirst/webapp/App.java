package net.myfirst.webapp;

import spark.ModelAndView;
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
    public static void main(String[] args) {
//          port(8080);
        try {
            Connection connection = getDatabaseConnection("jdbc:postgres://zciqobeldegqhj:4d13c7b3a96005b298ca74370d6af36691cdd1742d63023166a6b5e79fb63bad@ec2-184-72-236-57.compute-1.amazonaws.com:5432/dcql5u59n1c5v5\n");
            port(getHerokuAssignedPort());
            List<String> allusers = new ArrayList<>();
            staticFiles.location("/public");
            get("/greet", (req, res) -> "Molo!");
            get("/greet/:username", (req, res) -> "Molo!" + req.queryParams("username"));
            get("/greet/:username/language/:language", (req, res) -> "Cairo " + req.queryParams("language"));
            post("/greet", (req, res) -> "Hello! " + req.queryParams("username"));


            get("/hello", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("counter",allusers.size());
                return new ModelAndView(map, "hello.handlebars");

            }, new HandlebarsTemplateEngine());

            post("/hello", (req, res) -> {


                Map<String, Object> map = new HashMap<>();

                // create the greeting message
                String user = req.queryParams("username");
                String language = req.queryParams("language");
                String greeting = "";

                switch (language == null ? "default" : language ) {
                    case "Isixhosa":
                        greeting = "Molo! " + user;
                        break;
                    case "English":
                        greeting = " Hello! " + user;
                        break;
                    case "default":
                        greeting = "Molo nawe" + user;

                }
                if (!allusers.contains(user) && !user.isEmpty()) {
                    allusers.add(user);
                }
//            // put it in the map which is passed to the template - the value will be merged into the template
                map.put("greeting", greeting);
                map.put("counter",allusers.size());
                map.put("users",allusers);


                return new ModelAndView(map, "hello.handlebars");

            }, new HandlebarsTemplateEngine());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (URISyntaxException e) {
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
    static Connection getDatabaseConnection(String defualtJdbcUrl) throws URISyntaxException, SQLException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String database_url = processBuilder.environment().get("DATABASE_URL");
        if (database_url != null) {

            URI uri = new URI(database_url);
            String[] hostParts = uri.getUserInfo().split(":");
            String username = hostParts[0];
            String password = hostParts[1];
            String host = uri.getHost();

            int port = uri.getPort();

            String path = uri.getPath();
            String url = String.format("jdbc:postgresql://%s:%s%s", host, port, path);

            return DriverManager.getConnection(url, username, password);

        }

        return DriverManager.getConnection(defualtJdbcUrl);

    }
}

