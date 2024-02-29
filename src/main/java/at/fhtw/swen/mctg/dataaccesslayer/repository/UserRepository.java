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

    final int STARTINGCOINS = 20;

    public UserRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }


    public boolean userExists(User user){
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
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        new Stack(),
                        new Stack());
                userRows.add(resultUser);
            }
            if(userRows.size() > 0){
                return true;
            }else{
                //TODO: when pw encoded change this password comparison with th encoding one!
                return false;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    //TODO: Return value should be int, an Inventory needs to be created and the primary key needs to be a new one
    public void register(User user) {
        int lastPk = lastPK();
        //TODO hash password

        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                insert into users (id, username, password, coins, inventoryID) values (?, ?, ?, ?, ?)
                """))
        {
            preparedStatement.setInt(1, lastPk);
            preparedStatement.setString(2, user.username);
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, STARTINGCOINS);
            preparedStatement.setInt(5, 0);

            int result = preparedStatement.executeUpdate();
            return;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }

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
                        resultSet.getInt(1),
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
                User resultUser = userRows.get(0);
                //TODO: when pw encoded change this password comparison with th encoding one!
                if(resultUser.getPassword().equals(user.getPassword())){
                    return resultUser;
                }else{
                    return null;
                }

            }

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public void updateCoins(User user){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update users set coins = ? where id = ?
                """))
        {
            preparedStatement.setInt(1, user.getCoins());
            preparedStatement.setInt(2, user.getId());

            int result = preparedStatement.executeUpdate();
            return;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }
    }

    private int lastPK(){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from users order by id desc
                """))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            int lastPK = 1;
            if(resultSet.next()){
                lastPK = resultSet.getInt(1) + 1;
            }

            return lastPK;

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

}

