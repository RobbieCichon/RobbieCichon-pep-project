package Service;

import java.util.List;
import java.util.ArrayList;
import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message addMessage(Message message){
        if(message.getMessage_text() == "" || message.getMessage_text().length() >= 255 || !messageDAO.postedIdExists(message.getPosted_by())) return null;
        return messageDAO.addMessage(message);
    }

    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    public Message deleteMessage(int id){
        return messageDAO.deleteMessage(id);
    }

    public Message updateMessage(String messageContents, int id){
        if (getMessageByID(id) == null) return null;
        else {
            messageDAO.updateMessage(messageContents, id);
            return getMessageByID(id);
        }
    }

    public List<Message> getAllMessagesFromUser(int id){
        if (!messageDAO.postedIdExists(id)) return new ArrayList<>();
        return messageDAO.getAllMessagesFromUser(id);
    }
}
