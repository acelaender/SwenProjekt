package at.fhtw.swen.mctg.dataaccesslayer.repository;

import at.fhtw.swen.mctg.dataaccesslayer.DataAccessException;
import at.fhtw.swen.mctg.dataaccesslayer.UnitOfWork;
import at.fhtw.swen.mctg.models.*;
import at.fhtw.swen.mctg.models.Package;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class CardRepository {
    private UnitOfWork unitOfWork;

    public CardRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public Package getPackage(int packID){
        //Possibility to have multiple packs with different cards/prices

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from cards
                """))
        {
            ResultSet resultSet = preparedStatement.executeQuery();

            Package pack = new Package("standardPack", 5);

            ArrayList<Card> possibleCards = new ArrayList<Card>();
            while(resultSet.next())
            {
                //name element damage type
                Card resultCard = new Card(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        getElement(resultSet.getInt(4)),
                        resultSet.getInt(3),
                        resultSet.getString(5));
                possibleCards.add(resultCard);
            }

            pack.instantiateCards(possibleCards);

            return pack;


        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    private Element getElement(int elementID){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from elements where eid = ?
                """))
        {

            preparedStatement.setInt(1, elementID);
            ResultSet resultSet = preparedStatement.executeQuery();

            return new Element(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public void addCardsToInventory(User user, ArrayList<Card> cards){

        for (int i = 0; i < cards.size(); i++) {
            try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    insert into inventory (rid, userid, cardid, instack) values (?, ?, ?, ?)
                    """)) {
                int lastPk = lastPKInventory();
                preparedStatement.setInt(1, lastPk);
                preparedStatement.setInt(2, user.getId());
                preparedStatement.setInt(3, cards.get(i).getId());
                preparedStatement.setInt(4, 0);

                int result = preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new DataAccessException("Insert nicht erfolgreich ", e);
            }
        }
        return;
    }

    private int lastPKInventory(){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from inventory order by id desc
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
