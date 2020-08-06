package net.myfirst.webapp;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        //  port(8080);

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

    }
}
