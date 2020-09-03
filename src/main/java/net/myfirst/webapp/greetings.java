package net.myfirst.webapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class greetings {
    private String greeting = "";
    private Connection connection;

    greetings(Connection connection) {
        this.connection = connection;
    }

    public void greet(String name, String language) throws SQLException {
//        addName(name);
        System.out.println(addName(name));
        System.out.println(language + " language value");
        switch (language == null ? "default" : language) {
            case "Isixhosa":
                greeting = "Molo! " + name;
                break;
            case "English":
                greeting = " Hello! " + name;
                break;
            case "default":
                greeting = "Molo nawe" + name;

        }
    }

    public String getGreeting() {
        return greeting;
    }

    public List<String> getNames() throws SQLException {
        List<String> names = new ArrayList<>();
        PreparedStatement get_users = connection.prepareStatement("select * from person");
        ResultSet resultSet = get_users.executeQuery();
        while (resultSet.next()) {
            names.add(resultSet.getString("first_name"));
        }
        System.out.println(names);
        return names;
    }

    public boolean addName(String name) throws SQLException {
        PreparedStatement add_user = connection.prepareStatement("insert into person (first_name, counter) values (?, ?)");
        PreparedStatement find_user = connection.prepareStatement("select * from person where first_name = ?");
        PreparedStatement update_user = connection.prepareStatement("update person set counter = +1 where first_name = ?");
        find_user.setString(1, name);
        ResultSet resultSet = find_user.executeQuery();
        if (resultSet.next()) {
            update_user.setString(1, name);
            update_user.execute();
            return true;
        } else {
            add_user.setString(1, name);
            add_user.setInt(2, 1);
            add_user.execute();
            return false;

        }

    }
}
