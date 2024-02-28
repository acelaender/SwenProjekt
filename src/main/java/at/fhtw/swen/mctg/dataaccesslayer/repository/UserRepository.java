package at.fhtw.swen.mctg.dataaccesslayer.repository;

import at.fhtw.swen.mctg.dataaccesslayer.DataAccessException;
import at.fhtw.swen.mctg.dataaccesslayer.UnitOfWork;
import at.fhtw.swen.mctg.models.Package;
import at.fhtw.swen.mctg.models.Stack;
import at.fhtw.swen.mctg.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserRepository {
    private UnitOfWork unitOfWork;

    public UserRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public String register(User user){
        return "user registered!";
    }

    public User login(User user) {
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from users where username = ?
                """))
        {
            preparedStatement.setString(1, user.username);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<User> userRows = new ArrayList<>();
            while(resultSet.next())
            {
                User resultUser = new User(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        new Stack(),
                        new Stack());
                userRows.add(resultUser);
            }
            if(userRows.size() > 1){
                return null;
            }else{
                return userRows.get(0);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public Package getPackage(int packageID){
        Package pack = new Package("standardPack", 5);
        pack = pack.buy();
        return pack;
    }

}

