package view;

import org.apache.ibatis.jdbc.SQL;
import persistence.PooledDataSource;
import persistence.dao.ManagerDAO;
import persistence.dao.ProfessorDAO;
import persistence.dao.StudentDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LoginView {
    private Scanner sc = new Scanner(System.in);

    private ManagerDAO managerDAO = new ManagerDAO();
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private final DataSource ds = PooledDataSource.getDataSource();
    private Connection conn = null;

    private void closedConn(Connection conn) {
        try {
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public void start() {
        while (true) {
            try {
                conn = ds.getConnection();
                conn.setAutoCommit(false);
            }catch (SQLException e) {
                e.printStackTrace();
            }
            int sel = 0;
            System.out.println("1. 관리자 로그인");
            System.out.println("2. 관리자 회원가입");
            System.out.println("3. 교수 로그인");
            System.out.println("4. 학생 로그인");
            System.out.println("5. 종료");
            System.out.print("번호 선택: ");
            while (true) {
                try {
                    sel = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("잘못된 입력입니다.");
                    sc.next();
                }
                break;
            }
            switch (sel) {
                case 1:
                    //managerDAO.login(conn);
                    break;
                case 2:
                    managerDAO.register(conn);
                    break;
                case 3:
                    //professorDAO.login(conn);
                    break;
                case 4:
                    //studentDAO.login(conn);
                    break;
                case 5:
                    System.out.println("종료합니다.");
                    closedConn(conn);
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}

