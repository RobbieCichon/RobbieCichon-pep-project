package DAO;

import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Message;

public class MessageDAO {
    /**
     * Grabs all messages from the SQL database.
     * @return an ArrayList of all messages.
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }
    /**
     * Checks to see if there exists posts that were created by a specific user.
     * @param id integer id of user to be checked.
     * @return boolean, false if there are no messages by the user and true if there exists at least one.
     * 
     */
    public boolean postedIdExists(int id){
        boolean result = false;
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "Select account_id FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                if (rs.getInt("account_id") == id) result = true;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return result;
    }

    /**
     * Adds a new post to the SQL database.
     * @param message Message object to be added.
     * @return The message that was added, with the newly given message_id if successful, null if not.
     */
    public Message addMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves a specific post by its message_id.
     * @param id the ID of the message.
     * @return The Message object that was retrieved from the SQL database, null if not found.
     */
    public Message getMessageByID(int id){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        }
        catch (SQLException e){
            e.getMessage();
        }

        return null;
    }

    /**
     * Deletes a message from the SQL database using its given ID.
     * @param id the ID of the message.
     * @return The Message object that was deleted if successful, null if not.
     */
    public Message deleteMessage(int id){
        Message message = getMessageByID(id);

        if (message != null){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
        }
        catch (SQLException e){
            e.getMessage();
        }
        return message;
    }

        else return null;
    }

    /**
     * Updates an existing post with a new message in the SQL database.
     * @param messageContents String of the new message to replace the old one.
     * @param id The ID of the message to be updated.
     */
    public void updateMessage(String messageContents, int id){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, messageContents);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.getMessage();
        }
    }
    /**
     * Retrieves from the SQL database all messages that were posted by a particular user.
     * @param id The ID of the user.
     * @return An ArrayList of all messages that were posted by the user, empty if none found.
     */
    public List<Message> getAllMessagesFromUser(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            e.getMessage();
        }
        return messages;
    }
}
