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
    /**
     * Handler to register a new user, provided that the username and password fulfil the given requirements.
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postRegisterHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null){
            context.json(mapper.writeValueAsString(addedAccount));
        }
        else context.status(400);
    }

    /**
     * Handler to login to the blog using an existing account's credentials.
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account accessedAccount = accountService.accessAccount(account);
        if (accessedAccount != null){
            context.json(mapper.writeValueAsString(accessedAccount));
        }
        else context.status(401);
    }
    /**
     * Handler to post a new message, provided that the provided message text and user ID fulfil the given requirements.
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */

    private void postMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null){
            context.json(mapper.writeValueAsString(addedMessage));
        }
        else context.status(400);
    }

    /**
     * Handler to retrieve all posted messages.
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     */
    private void getMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    /**
     * Handler to retrieve a specific post using its ID, provided the ID and message exist.
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     */
    private void messageIdHandler(Context context){
        Message message = messageService.getMessageByID(Integer.parseInt(context.pathParam("message_id")));
        if (message != null) context.json(message);
        else context.status(200);
    }
    /**
     * Handler to delete a specific post using its ID, provided the ID and message exist.
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     */
    private void deleteMessageHandler(Context context) {
        Message message = messageService.deleteMessage(Integer.parseInt(context.pathParam("message_id")));
        if (message!= null) context.json(message);
        else context.status(200);
    }

    /**
     * Handler to update a specific post with a new message text, subject to the same requirements as a new message would
     *            have.
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = mapper.readValue(context.body(), Message.class);
        String messageContents = message.getMessage_text();

        Message updatedMessage = messageService.updateMessage(messageContents, id);

        if (messageContents.length() >= 255 || messageContents == "" || updatedMessage == null) context.status(400);
        else context.json(updatedMessage);
    }

    /**
     * Handler to retrieve all posted messages from a specific user, provided that the user both exists and have posted.
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     */
    private void getAllMessagesByAccountHandler(Context context) {
        context.json(messageService.getAllMessagesFromUser(Integer.parseInt(context.pathParam("account_id"))));
    }

}