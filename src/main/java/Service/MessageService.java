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
        if(message.getMessage_text() == "" || message.getMessage_text().length() >= 255 || !messageDAO.postedIdExists(message)) return null;
        return messageDAO.addMessage(message);
    }
}
