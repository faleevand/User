package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }
    public static long iid ;
    Util ut = new Util();
    Connection con = ut.getConnection();

    public void createUsersTable() {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Users (Id INT PRIMARY KEY , name VARCHAR(20), lastName VARCHAR(20), age int)");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void dropUsersTable() {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS Users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        user.setId((iid++));
        try {
            PreparedStatement prpstatement = con.prepareStatement("INSERT INTO Users (Id,name, lastName,age) VALUES (?,?,?,?)");
            prpstatement.setLong(1, user.getId());
            prpstatement.setString(2, user.getName());
            prpstatement.setString(3, user.getLastName());
            prpstatement.setByte(4, user.getAge());
            prpstatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("User с именем – " + name + " добавлен в базу данных");
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement prpstatement = con.prepareStatement("DELETE FROM Users WHERE id = ?");
            prpstatement.setLong(1, id);
            prpstatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {


        List<User> userslist = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Id, name, lastName, age FROM Users");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                userslist.add(user);
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
        return userslist;

    }


    public void cleanUsersTable() {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("TRUNCATE TABLE  Users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

