package view;

import persistence.dao.ManagerDAO;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;
public class ManagerView {
    private Scanner sc = new Scanner(System.in);
    private ManagerDAO managerDAO = new ManagerDAO();

    public void start(Connection conn, String id) {

        while (true) {
            int sel = 8;
            System.out.println("1. 교수 학생 계정 생성");
            System.out.println("2. 교과목 생성/수정/삭제");
            System.out.println("3. 강의 계획서 입력 기간 설정");
            System.out.println("4. 학년별 수강 신청 기간 설정");
            System.out.println("5. 교수/학생 정보 조회");
            System.out.println("6. 교과목 정보 조회");
            System.out.println("7. 뒤로 가기");
            System.out.print("메뉴 선택: ");
            while (true) {

                try {
                    sel = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("잘못된 입력 형식입니다.");
                    sc.next();
                }
                break;
            }
            switch (sel) {
                case 1:
                    managerDAO.psRegister(conn);
                    break;
                case 2:
                    //xml coding
                    managerDAO.subjectCUD(conn);
                    break;
                case 3:
                    managerDAO.insertCoursedPeriod(conn);
                    break;
                case 4:
                    managerDAO.insertEnrollmentPeriod(conn);
                    break;
                case 5:
                    managerDAO.psSelect(conn);
                    break;
                case 6:
                    managerDAO.findAllOpenedSubject();
                    break;
                case 7:
                    System.out.println("뒤로가기");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }

        }
    }
}