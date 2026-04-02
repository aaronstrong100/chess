package serveraccess;

import websocket.messages.ServerMessage;

public interface ServerMessageObserver {

    void sendMessage(ServerMessage serverMessage);
}
