package Network;

import persistence.dao.ManagerDAO;
import persistence.dao.ProfessorDAO;
import persistence.dao.StudentDAO;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ServerThread extends Thread{
    private int id;
    private Socket socket;
    private Connection conn;
    private Protocol p;
    private IO io;
    private ManagerDAO managerDAO = new ManagerDAO();
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private StudentDAO studentDAO = new StudentDAO();

    private boolean isLogin = true;
    private boolean isManager = false;
    private boolean isProfessor = false;
    private boolean isStudent = false;

    public ServerThread(Socket socket, Connection conn) throws IOException {
        id = socket.getPort();
        this.socket = socket;
        this.conn = conn;
        io = new IO(socket);
    }
    public int getID() {
        return id;
    }
    @Override
    public void run() {
        while(true) {
            try {
                p = io.read();
                handle(p);
            }catch (Exception e) {
                this.stop();
            }
        }
    }

    public void handle(Protocol p) throws Exception {
        int packetType = p.getType();
        String arr = p.getBody().toString();
        StringTokenizer st = new StringTokenizer(arr, "\n");
        switch(packetType) {
            case Protocol.T5_EXIT:
                if (isLogin) {
                    Server.remove(this.id);
                }
            case Protocol.T1_MANAGER_LOGIN_REQ:
                if(isLogin && managerDAO.login(conn, st.nextToken(), st.nextToken())) {
                    io.send(new Protocol(Protocol.T6_MANAGER_LOGIN_RES));
                    isLogin = false;
                    isManager = true;
                }
                else {
                    io.send(new Protocol(Protocol.T6_MANAGER_LOGIN_RES , Protocol.CD1_FAIL));
                }
            case Protocol.T3_PROFESSOR_LOGIN_REQ:
                if (isLogin && professorDAO.login(conn, st.nextToken(), st.nextToken())) {
                    io.send(new Protocol(Protocol.T8_PROFESSOR_LOGIN_RES));
                    isLogin = false;
                    isProfessor = true;
                }
                else {
                    io.send(new Protocol(Protocol.T8_PROFESSOR_LOGIN_RES, Protocol.CD1_FAIL));
                }
            case Protocol.T4_STUDENT_LOGIN_REQ:
                if (isLogin && studentDAO.login(conn, Integer.parseInt(st.nextToken()), st.nextToken())) {
                    io.send(new Protocol(Protocol.T9_STUDENT_LOGIN_RES));
                    isLogin = false;
                    isProfessor = true;
                }
            case Protocol.T6_0_BACK_TO:
                if (isManager) {
                    isLogin = true;
                    isManager = false;
                }
                else if (isProfessor) {
                    isLogin = true;
                    isProfessor = false;
                }
                else if (isStudent) {
                    isLogin = true;
                    isStudent = false;
                }
        }
    }

    public IO getIO() {
        return io;
    }

    public void setIO(IO io) {
        this.io = io;
    }


    public void setId(int id) {
        this.id = id;
    }
}
