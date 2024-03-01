package at.fhtw.swen.mctg.dataaccesslayer.repository;

import at.fhtw.swen.mctg.dataaccesslayer.DataAccessException;
import at.fhtw.swen.mctg.dataaccesslayer.UnitOfWork;
import at.fhtw.swen.mctg.models.*;
import at.fhtw.swen.mctg.models.Package;

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

    public boolean userExists(String user){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from users where username = ?
                """))
        {
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<User> userRows = new ArrayList<>();
            while(resultSet.next())
            {
                User resultUser = new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        new Stack());
                userRows.add(resultUser);
            }
            if(userRows.size() > 0){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public void register(UserCredentials user) {
        int lastPk = lastPK();
        //TODO hash password

        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                insert into users (id, username, password, coins, bio, image) values (?, ?, ?, ?, ?, ?)
                """))
        {
            System.out.println(lastPk + " " + user.getUsername() + " " + user.getUsername() + " " + STARTINGCOINS + " ");
            preparedStatement.setInt(1, lastPk);
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, STARTINGCOINS);
            preparedStatement.setString(5, "");
            preparedStatement.setString(6, "");

            int result = preparedStatement.executeUpdate();
            return;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }

    }

    public UserData getUser(String username){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from users where username = ?
                """))
        {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<UserData> userRows = new ArrayList<>();
            while(resultSet.next())
            {
                UserData resultUser = new UserData(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(4));
                userRows.add(resultUser);
            }
            if(!userRows.isEmpty()){
                return userRows.get(0);
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public UserData getUser(int userid){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from users where id = ?
                """))
        {
            preparedStatement.setInt(1, userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<UserData> userRows = new ArrayList<>();
            while(resultSet.next())
            {
                UserData resultUser = new UserData(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(4));
                userRows.add(resultUser);
            }
            if(!userRows.isEmpty()){
                return userRows.get(0);
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public void updateUser(String username, UserData userData){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update users set bio = ?, image = ? where username = ?
                """))
        {
            preparedStatement.setString(1, userData.getBio());
            preparedStatement.setString(2, userData.getImage());
            preparedStatement.setString(3, username);

            int result = preparedStatement.executeUpdate();
            return;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }
    }

    public boolean login(UserCredentials user) {
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from users where username = ?
                """))
        {
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<UserCredentials> userRows = new ArrayList<>();
            while(resultSet.next())
            {
                UserCredentials resultUser = new UserCredentials(
                        resultSet.getString(2),
                        resultSet.getString(3));
                userRows.add(resultUser);
            }
            if(userRows.size() != 1){
                return false;
            }else{
                UserCredentials resultUser = userRows.get(0);
                //TODO: when pw encoded change this password comparison with th encoding one!
                if(resultUser.getPassword().equals(user.getPassword())){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }


    public boolean updateCoins(UserCredentials user){
        int result;
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select coins from users where username = ?
                """))
        {
            preparedStatement.setString(1, user.getUsername());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getInt(1);
                if(result < 5) {
                    return false;
                }
            }else{
                return false;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }

        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update users set coins = ? where username = ?
                """))
        {
            preparedStatement.setInt(1, result - 5);
            preparedStatement.setString(2, user.getUsername());

            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }
    }

    public void addCardsToInventory(UserCredentials user, ArrayList<Card> cards){
        int id = getUser(user.getUsername()).getId();

        for (int i = 0; i < cards.size(); i++) {
            try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    insert into inventory (rid, userid, cardid, instack) values (?, ?, ?, ?)
                    """)) {
                int lastPk = lastPKInventory();
                preparedStatement.setInt(1, lastPk);
                preparedStatement.setInt(2, id);
                preparedStatement.setInt(3, cards.get(i).getId());
                preparedStatement.setInt(4, 0);

                int result = preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new DataAccessException("Insert nicht erfolgreich ", e);
            }
        }
        return;
    }

    public UserStats getStats(UserData user){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from stats where userid = ?
                """))
        {
            int userId = getUser(user.getName()).getId();
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                UserStats stats = new UserStats(
                        user.getName(),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5));
                return stats;
            }else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public ArrayList<UserStats> getScoreboard(){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from stats order by elo desc
                """))
        {
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<UserStats> scoreboard = new ArrayList<>();

            while(resultSet.next())
            {
                String username = getUser(resultSet.getInt(2)).getName();
                UserStats stats = new UserStats(
                        username,
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5));
                scoreboard.add(stats);
            }

            return scoreboard;

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public ArrayList<TradingDeal> getTradingDeals(){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from tradings
                """))
        {
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<TradingDeal> tradingDeals = new ArrayList<>();

            while(resultSet.next())
            {
                TradingDeal deal = new TradingDeal(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        "",
                        resultSet.getString(3),
                        resultSet.getInt(4));
                tradingDeals.add(deal);
            }

            return tradingDeals;

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public void startTradingDeal(TradingDeal deal){
        int lastPk = lastPKTradings();
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                insert into tradings (tid, cardid, type, minimumdamage) values (?, ?, ?, ?)
                """))
        {
            preparedStatement.setInt(1, lastPKTradings());
            preparedStatement.setInt(2, deal.getCardToDeal());
            preparedStatement.setString(3, deal.getType());
            preparedStatement.setInt(4, deal.getMinimumDamage());

            int result = preparedStatement.executeUpdate();
            return;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }
    }

    public boolean tradeExists(int cardID){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from tradings where cardid = ?
                """))
        {

            preparedStatement.setInt(1, cardID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public void deleteTradingDeal(int dealid){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                delete from tradings where tid = ?
                """))
        {
            preparedStatement.setInt(1, dealid);

            int result = preparedStatement.executeUpdate();
            return;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }
    }

    private int lastPKTradings(){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from tradings order by tid desc
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

    private int lastPKInventory(){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from inventory order by rid desc
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

    private int getElo(int userId){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select elo from stats where userid = ?
                """))
        {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1) + 1;
            }
            return 0;

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public void win(int userid){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update stats set elo = ?, wins = wins + 1 where userid = ?
                """))
        {
            int newElo = getElo(userid);
            if (newElo > 990){
                newElo = 1000;
            }else{
                newElo += 10;
            }
            preparedStatement.setInt(1, newElo);
            preparedStatement.setInt(2, userid);

            int result = preparedStatement.executeUpdate();
            return;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }
    }
    public void loose(int userid){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update stats set elo = ?, losses = losses + 1 where userid = ?
                """))
        {
            int newElo = getElo(userid);
            if (newElo < 10){
                newElo = 0;
            }else{
                newElo -= 10;
            }
            preparedStatement.setInt(1, newElo);
            preparedStatement.setInt(2, userid);

            int result = preparedStatement.executeUpdate();
            return;

        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich ", e);
        }
    }

}

