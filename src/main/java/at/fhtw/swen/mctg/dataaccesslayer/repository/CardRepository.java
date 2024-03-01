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
import java.util.Random;

public class CardRepository {
    private UnitOfWork unitOfWork;

    public CardRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public ArrayList<Card> getPackage(){
        //Possibility to have multiple packs with different cards/prices

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from packages where pid = ?
                """))
        {

            Random rand = new Random();
            int pid = rand.nextInt(lastPKPackages());

            preparedStatement.setInt(1, pid);
            ResultSet resultSet = preparedStatement.executeQuery();

            Package pack = new Package("Package", 5);

            ArrayList<Card> cards = new ArrayList<Card>();
            int[] cids = new int[5];

            while(resultSet.next())
            {
                cids[0] = resultSet.getInt(2);
                cids[1] = resultSet.getInt(3);
                cids[2] = resultSet.getInt(4);
                cids[3] = resultSet.getInt(5);
                cids[4] = resultSet.getInt(6);
            }

            for (int i = 0; i < 5; i++) {
                cards.add(getCard(cids[i]));
            }

            return cards;


        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public Card getCard(int cid){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from cards where cid = ?
                """))
        {
            preparedStatement.setInt(1, cid);
            ResultSet resultSet = preparedStatement.executeQuery();

            Card card = new Card();
            int eid = 0;

            while(resultSet.next())
            {
                card = new Card(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        null,
                        resultSet.getInt(3),
                        resultSet.getString(5),
                        ""
                );
                eid = resultSet.getInt(4);
            }

            card.element = getElement(eid);
            card.setElementName(card.element.elementName);
            return card;

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public Card getCardTypeFromInvCard(int cid){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select cardid from inventory where rid = ?
                """))
        {
            preparedStatement.setInt(1, cid);
            ResultSet resultSet = preparedStatement.executeQuery();

            Card card = new Card();

            if(resultSet.next()){
                card = getCard(resultSet.getInt(1));
            }

            return card;

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public ArrayList<Card> getInventory(UserData user){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from inventory where userid = ?
                """))
        {
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Card> cards = new ArrayList<>();
            while(resultSet.next())
            {
                Card card = getCard(resultSet.getInt(3));
                card.element = null;
                cards.add(card);
            }
            if(!cards.isEmpty()){
                return cards;
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public ArrayList<Card> getStack(UserData user){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from inventory where userid = ?
                """))
        {
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Card> cards = new ArrayList<>();
            while(resultSet.next())
            {
                if(resultSet.getInt(4) == 1){
                    cards.add(getCard(resultSet.getInt(3)));
                }
            }
            if(!cards.isEmpty()){
                return cards;
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    private void discardStack(UserData user) {
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                        update inventory set instack = 0 where userid = ?
                    """)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }

    public void updateStack(UserData user, ArrayList<Card> stack){
        if(getStack(user) != null){
            discardStack(user);
        }else {
            try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                        update inventory set instack = 1 where rid in (?, ?, ?, ?) and userid = ?
                    """)) {
                preparedStatement.setInt(1, stack.get(0).getId());
                preparedStatement.setInt(1, stack.get(1).getId());
                preparedStatement.setInt(1, stack.get(2).getId());
                preparedStatement.setInt(1, stack.get(3).getId());

                preparedStatement.setInt(1, user.getId());

                preparedStatement.executeUpdate();
                ArrayList<Card> cards = new ArrayList<>();

            } catch (SQLException e) {
                throw new DataAccessException("Select nicht erfolgreich ", e);
            }
        }
    }

    private Element getElement(int elementID){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from elements where eid = ?
                """))
        {


            preparedStatement.setInt(1, elementID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return new Element(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
            }else {
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich ", e);
        }
    }



    private int lastPKPackages(){
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select * from packages order by pid desc
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
