package Controller;
import Model.Account;
import Model.Message;
import java.util.List;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::messageIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountHandler);

        return app;
    }
    private void postRegisterHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null){
            context.json(mapper.writeValueAsString(addedAccount));
        }
        else context.status(400);
    }

    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account accessedAccount = accountService.accessAccount(account);
        if (accessedAccount != null){
            context.json(mapper.writeValueAsString(accessedAccount));
        }
        else context.status(401);
    }

    private void postMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null){
            context.json(mapper.writeValueAsString(addedMessage));
        }
        else context.status(400);
    }

    private void getMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void messageIdHandler(Context context) throws JsonProcessingException{
        Message message = messageService.getMessageByID(Integer.parseInt(context.pathParam("message_id")));
        if (message != null) context.json(message);
        else context.status(200);
    }

    private void deleteMessageHandler(Context context) {
        Message message = messageService.deleteMessage(Integer.parseInt(context.pathParam("message_id")));
        if (message!= null) context.json(message);
        else context.status(200);
    }

    private void updateMessageHandler(Context context) {
        context.json("sample text");
    }

    private void getAllMessagesByAccountHandler(Context context) {
        context.json("sample text");
    }

}