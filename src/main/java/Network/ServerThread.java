package Network;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

public class ServerThread extends Thread {
    private int ID;
    private Socket socket;
    private Connection conn;
    private Protocol p;
    private IO io;

    public ServerThread(Socket socket, Connection conn) throws IOException {
        ID = socket.getPort();
        this.socket = socket;
        this.conn = conn;
        io = new IO(socket);
    }

    @Override
    public void run() {
        while (true) {
            try {
                p = io.read();
                handle(p);
            } catch (Exception e) {
                this.stop();
            }
        }
    }

    public void handle(Protocol p) throws Exception {
        int packetType = p.getType();

        switch (packetType) {
            case Protocol.LOGIN_REQ: //로그인
                String[] s = (String[]) p.getBody();
                String SQL = "SELECT user_section FROM USERS WHERE user_id = ?";
                break;

        }
    }
}
