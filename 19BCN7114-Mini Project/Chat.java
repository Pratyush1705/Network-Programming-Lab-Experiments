import java.io.*;
//Class which sets the option type and sets the message to be passed
//It consists the option type and sets message
//Serializable interface is implemented to make it easier to access objects and variables and it makes storing 
//and sending objects easy
public class Chat implements Serializable{
    static final int WHOISIN =0, MSG=1, UserCount=2, Logout=3;
    //WHOISIN - shows the active users in the chat room
    // MSG- is used whenever we intend to send a message
    // UserCount- Prints the number of users connected to server.
    //Logout- disconnects user from chat when selected
    private int type;
    private String msg;
    
    Chat(int type,String msg)
    {
        this.type=type;
        this.msg=msg;
    }

    int getType()
    {
        return type;
    }

    String getMSG()
    {
        return msg;
    }
}