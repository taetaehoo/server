package view;

import persistence.dao.ProfessorDAO;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProfessorView {
    private Scanner sc = new Scanner(System.in);
    private ProfessorDAO professorDAO = new ProfessorDAO();
    public void start(Connection conn, String pNumber) {
        while (true) {
            int sel = 8;
            System.out.println("1. 개인정보 및 비밀번호 수정");
            System.out.println("2. 강의 계획서 입력/수정");
            System.out.println("3. 교과목 목록 조회");
            System.out.println("4. 교과목 강의 계획서 조회");
            System.out.println("5. 교과목 수강 신청 학생 목록 조회");
            System.out.println("6. 교과목 시간표 조회");
            System.out.println("7. 개설 교과목 관리");
            System.out.println("8. 뒤로 가기");
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
                    professorDAO.updatePrivacy(conn, pNumber);
                    break;
                case 2:
                    professorDAO.selCourseDes(conn);
                    break;
                case 3:
                    professorDAO.selRange(conn);
                    break;
                case 4:
                    professorDAO.findCoursed(conn);
                    break;
                case 5:
                    professorDAO.selectEnrollmentStudent(conn, pNumber);
                    break;
                case 6:
                    professorDAO.selectTimeTable(conn, pNumber);
                    break;
                case 7:
                    //mapper coding
                case 8:
                    System.out.println("뒤로가기");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}
