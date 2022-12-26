import java.io.Serializable;

public class Message<T> implements Serializable {
    public static final int TYPE_GET_PLAYER_NAME = -1;
    public static final int TYPE_OUT_DELAY = -2;
    public static final int TYPE_NBR_CASE = 0;
    public static final int TYPE_CURRENT_NUM = 1;
    public static final int TYPE_PLAYER_LIST = 2;
    public static final int TYPE_CURRENT_PLAYER_INDEX = 3;
    public static final int TYPE_DELAY = 4;
    public static final int TYPE_CUSTOM_INDEX = 5;
    public static final int REPLY = 100;
    public static final int OK = 101;
    private int typeMsg;
    private T msgContent;

    public int getTypeMsg() {
        return typeMsg;
    }

    public T getMsgContent() {
        return msgContent;
    }

    public void setTypeMsg(int typeMsg) {
        this.typeMsg = typeMsg;
    }


    public Message(int typeMsg, T msgContent) {
        this.typeMsg = typeMsg;
        this.msgContent = msgContent;
    }

    public Message(int typeMsg) {
        this.typeMsg = typeMsg;
    }
}
