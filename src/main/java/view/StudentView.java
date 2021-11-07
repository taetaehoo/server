package view;

import persistence.dao.StudentDAO;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentView {
    private Scanner sc = new Scanner(System.in);
    private StudentDAO studentDAO = new StudentDAO();

    public void start(Connection conn, int sNumber) {
        while (true) {
            int sel = 8;
            System.out.println("1. 개인정보 및 비밀번호 수정");
            System.out.println("2. 수강 신청/취소");
            System.out.println("3. 개설 교과목 목록(전학년) 조회");//학년별, 담당 교수별, 학년 + 담당 교수
            System.out.println("4. 선택 교과목 강의 계획서 조회");
            System.out.println("5. 본인 시간표 조회");
            System.out.println("6. 뒤로 가기");
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
                    studentDAO.updatePrivacy(conn, sNumber);
                    break;
                case 2:
                    studentDAO.enrollment(conn, sNumber);
                    break;
                case 3:
                    studentDAO.findOpenedSubject(conn, sNumber);
                    break;
                case 4:
                    studentDAO.findCoursed(conn);
                    break;
                case 5:
                    studentDAO.selectAllMyEnrollment(conn, sNumber);
                    break;
                case 6:
                    System.out.println("뒤로가기");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}
