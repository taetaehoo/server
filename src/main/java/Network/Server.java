package Network;

import persistence.PooledDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

public class Server {
    private static Object SQLException;
    private ServerSocket serverSocket;
    private static ServerThread clients[];
    private static int clientCount;
    private final DataSource ds = PooledDataSource.getDataSource();

    public static void main(String [] args) throws Exception {
        Server server = new Server();
        server.run();
    }
    public Server() throws IOException {
        clients = new ServerThread[10];
        clientCount = 0;
        serverSocket = new ServerSocket(9999);
    }

    public void run() throws Exception {
        while (serverSocket != null) {
            Socket socket = serverSocket.accept();
            Connection conn = ds.getConnection();
            System.out.println("DB 연결");
            addThread(socket, conn);
        }
    }

    public synchronized void addThread(Socket socket, Connection conn) throws Exception {
        if (clientCount < clients.length) {
            clients[clientCount] = new ServerThread(socket, conn);
            clients[clientCount].start();
            clientCount++;
        } else {
            System.out.println("할당할 수 없는 쓰레드가 없습니다.");
        }
    }

    public static int findClient(int ID) {
        for (int i = 0; i < clientCount; i++)
            if (clients[i].getID() == ID)
                return i;
        return -1;
    }

    public synchronized static void remove(int ID) throws IOException {
        int pos = findClient(ID);
        if (pos >= 0) {
            ServerThread toTerminate = clients[pos];
            if (pos < clientCount - 1)
                for (int i = pos + 1; i < clientCount; i++)
                    clients[i - 1] = clients[i];
            clientCount--;
            System.out.println(clientCount);
            toTerminate.getIO().close();
            toTerminate.stop();
        }
    }
}
