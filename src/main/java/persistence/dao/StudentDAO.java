package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.*;
import view.ProfessorView;
import view.StudentView;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentDAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private Scanner sc = new Scanner(System.in);

    public boolean login(Connection conn, int sNumber, String password) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "Select password from student where sNumber = ?";

        boolean isSuccess = false;
        try {
            pstmt = conn.prepareStatement(loginQuery);
            pstmt.setInt(1, sNumber);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).contentEquals(password))
                    isSuccess = true;
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return isSuccess;
    }//완료

    public void updatePrivacy(Connection conn, int sNumber) {
        System.out.println("변경할 이름을 입력하세요: ");
        String sName = sc.next();
        System.out.println("변경할 비밀번호를 입력하세요: ");
        String password = sc.next();
        System.out.println("변경할 학년를 입력하세요: ");
        int grade = sc.nextInt();

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setSNumber(sNumber);
        studentDTO.setSName(sName);
        studentDTO.setPassword(password);
        studentDTO.setGrade(grade);
        updatePrivacy(conn, studentDTO);
    }


    private void updatePrivacy(Connection conn, StudentDTO studentDTO) {
        PreparedStatement pstmt = null;
        String updatePrivacyQuery = "update student set sName = ?, password = ? , grade = ? where sNumber = ?";
        try {
            pstmt = conn.prepareStatement(updatePrivacyQuery);
            pstmt.setString(1, studentDTO.getSName());
            pstmt.setString(2, studentDTO.getPassword());
            pstmt.setInt(3, studentDTO.getGrade());
            pstmt.setInt(4, studentDTO.getSNumber());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void findOpenedSubject(Connection conn, int sNumber) {
        int sel = 0;
        while(true) {
            System.out.println("1. 전체 조회");
            System.out.println("2. 학년별 조회");
            System.out.println("3. 교수별 조회");
            System.out.println("4. 학년 + 교수별 조회");
            System.out.println("5. 뒤로가기");
            System.out.print("메뉴 선택: ");
            try {
                sel = sc.nextInt();
            }catch(InputMismatchException e) {
                System.out.println("잘못된 입력 형식입니다.");
                sc.next();
                continue;
            }
            switch(sel) {
                case 1:
                    findAllOpenedSubject(conn, sNumber);
                    break;
                case 2:
                    findGradeOpenedSubject(conn, sNumber);
                    break;
                case 3:
                    findProfessorOpenedSubject(conn, sNumber);
                    break;
                case 4:
                    findGPOpenedSubject(conn, sNumber);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("다시 입력해주시기 바랍니다.");
                    break;
            }
        }
    }

    private void findGPOpenedSubject(Connection conn, int sNumber) {
        System.out.print("원하는 학년을 입력하세요(5는 종료): ");
        int grade = 0;
        while(true) {
            try {
                grade = sc.nextInt();
            }catch (InputMismatchException e) {
                System.out.println("다시 입력해주시기 바랍니다.");
                sc.next();
                continue;
            }
            switch(grade) {
                case 1:
                case 2:
                case 3:
                case 4:
                    findPOpenedSubject(conn, grade, sNumber);
                case 5:
                    return;
                default:
                    System.out.println("다시 입력해주시기 바랍니다.");
                    break;
            }
        }
    }
    private void findPOpenedSubject(Connection conn, int grade, int sNumber) {
        System.out.println("선택 가능 교수 번호와 이름");
        showProfessor(conn);
        System.out.print("원하는 교수 번호를 입력하세요: ");
        String pNumber = sc.next();
        ProfessorDTO professorDTO = new ProfessorDTO();
        professorDTO.setPNumber(pNumber);
        if (checkProfessor(conn, professorDTO)) {
            findGPOpenedSubject(conn, professorDTO, grade, sNumber);
        }
        else {
            System.out.println("해당 교수 번호는 존재하지 않습니다.");
        }
    }
    private void findGPOpenedSubject(Connection conn, ProfessorDTO professorDTO, int grade, int sNumber) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectAllSubjectQuery = "SELECT * FROM union.openedsubject where pNumber = ? and csCode in (select sCode from union.subject where grade = ?)";

        try {
            pstmt = conn.prepareStatement(selectAllSubjectQuery);
            pstmt.setString(1, professorDTO.getPNumber());
            pstmt.setInt(2, grade);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String csCode = rs.getString("csCode");
                String pNumber = rs.getString("pNumber");
                int maxStudent = rs.getInt("maxStudent");
                int lectureStartTime = rs.getInt("lectureStartTime");
                int lectureEndTime = rs.getInt("lectureEndTime");
                String lectureDay = rs.getString("lectureDay");
                String lecutreRoom = rs.getString("lectureRoom");

                openedSubjectDTO openedSubject = new openedSubjectDTO();
                openedSubject.setCsCode(csCode);
                openedSubject.setPNumber(pNumber);
                openedSubject.setMaxStudent(maxStudent);
                openedSubject.setLectureStartTime(lectureStartTime);
                openedSubject.setLectureEndTime(lectureEndTime);
                openedSubject.setLectureDay(lectureDay);
                openedSubject.setLectureRoom(lecutreRoom);
                System.out.println("과목 이름: "+getSubjectName(conn, openedSubject.getCsCode())+" 수강신청 가능 여부: " + checkDate(conn, sNumber));
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    private void findGradeOpenedSubject(Connection conn, int sNumber) {
        System.out.print("원하는 학년을 입력하세요(5는 종료): ");
        int grade = 0;
        while(true) {
            try {
                grade = sc.nextInt();
            }catch (InputMismatchException e) {
                System.out.println("다시 입력해주시기 바랍니다.");
                sc.next();
                continue;
            }
            switch(grade) {
                case 1:
                case 2:
                case 3:
                case 4:
                    findGradeOpenedSubject(conn, grade, sNumber);
                case 5:
                    return;
                default:
                    System.out.println("다시 입력해주시기 바랍니다.");
                    break;
            }
        }
    }

    private void findGradeOpenedSubject(Connection conn, int grade, int sNumber) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectAllSubjectQuery = "SELECT * FROM union.openedsubject where csCode in (select sCode from union.subject where grade = ?)";

        try {
            pstmt = conn.prepareStatement(selectAllSubjectQuery);
            pstmt.setInt(1, grade);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String csCode = rs.getString("csCode");
                openedSubjectDTO openedSubject = new openedSubjectDTO();
                openedSubject.setCsCode(csCode);
                System.out.println("과목 이름: "+getSubjectName(conn, openedSubject.getCsCode())+" 수강신청 가능 여부: " + checkDate(conn, sNumber));
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void findProfessorOpenedSubject(Connection conn, int sNumber) {
        System.out.println("선택 가능 교수 번호와 이름");
        showProfessor(conn);
        System.out.print("원하는 교수 번호를 입력하세요: ");
        String pNumber = sc.next();
        ProfessorDTO professorDTO = new ProfessorDTO();
        professorDTO.setPNumber(pNumber);
        if (checkProfessor(conn, professorDTO)) {
            findProfessorOpenedSubject(conn, professorDTO, sNumber);
        }
        else {
            System.out.println("해당 교수 번호는 존재하지 않습니다.");
        }

    }

    private void findProfessorOpenedSubject(Connection conn, ProfessorDTO professorDTO, int sNumber) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectAllSubjectQuery = "Select * from openedsubject where pNumber = ?";

        try {
            pstmt = conn.prepareStatement(selectAllSubjectQuery);
            pstmt.setString(1, professorDTO.getPNumber());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String csCode = rs.getString("csCode");

                openedSubjectDTO openedSubject = new openedSubjectDTO();
                openedSubject.setCsCode(csCode);
                System.out.println("과목 이름: "+getSubjectName(conn, openedSubject.getCsCode())+" 수강신청 가능 여부: " + checkDate(conn, sNumber));
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    private boolean checkProfessor(Connection conn, ProfessorDTO professorDTO) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "select 1 as cnt from professor where exists (select 1 from professor where pNumber = ?)";
        boolean isExists = false;
        try {
            pstmt = conn.prepareStatement(loginQuery);
            pstmt.setString(1, professorDTO.getPNumber());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    isExists = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return isExists;
    }
    private void showProfessor(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        String selectAllSubjectQuery = "Select pNumber, pName from professor";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectAllSubjectQuery);
            while (rs.next()) {
                String pNumber = rs.getString("pNumber");
                String pName = rs.getString("pName");

                System.out.println("교수 번호: " + pNumber + " 교수 이름: "+pName);
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !stmt.isClosed()) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    private void findAllOpenedSubject(Connection conn, int sNumber) {
        Statement stmt = null;
        ResultSet rs = null;
        String selectAllSubjectQuery = "Select csCode from openedsubject";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectAllSubjectQuery);
            while (rs.next()) {
                String csCode = rs.getString("csCode");

                openedSubjectDTO openedSubject = new openedSubjectDTO();
                openedSubject.setCsCode(csCode);

                System.out.println("과목 이름: "+getSubjectName(conn, openedSubject.getCsCode())+" 가능 여부: " + checkDate(conn, sNumber));
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !stmt.isClosed()) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void findCoursed(Connection conn) {
        showCourse(conn);
        System.out.print("열람하고 싶은 강의의 코드를 입력하세요: ");
        String csCode = sc.next();
        openedSubjectDTO openedSubjectDTO = new openedSubjectDTO();
        openedSubjectDTO.setCsCode(csCode);
        if (checkCource(conn, openedSubjectDTO)) {
            findCoursed(conn, openedSubjectDTO);
        }
    }

    private void showCourse(Connection conn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectSubjectQuery = "Select csCode from openedSubject";
        try {
            pstmt = conn.prepareStatement(selectSubjectQuery);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String csCode = rs.getString("csCode");
                System.out.println("개설 교과목 코드: "+csCode);
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void findCoursed(Connection conn, openedSubjectDTO openedSubjectDTO) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectQuery = "Select * from courseDescription where subjectCode = ?";

        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, openedSubjectDTO.getCsCode());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("week: " + rs.getInt("week") + " plan: " + rs.getString("plan"));
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    private boolean checkCource(Connection conn, openedSubjectDTO openedSubjectDTO) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "select 1 as cnt from coursedescription where exists (select 1 from coursedescription where subjectCode = ?)";
        boolean isExists = false;
        try {
            pstmt = conn.prepareStatement(loginQuery);
            pstmt.setString(1, openedSubjectDTO.getCsCode());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    isExists = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return isExists;
    }

    public void selectAllMyEnrollment(Connection conn, int sNumber) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectQuery = "select sCode from enrollment where sNumber = ?";

        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setInt(1, sNumber);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String sCode = rs.getString("sCode");
                printTable(conn, sCode);
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    private void printTable(Connection conn, String csCode) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectQuery = "select lectureDay, lectureStartTime, lectureEndTime, lectureRoom from union.openedsubject where csCode = ?";

        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, csCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String lectureDay = rs.getString("lectureDay");
                int lectureStartTime = rs.getInt("lectureStartTime");
                int lectureEndTime = rs.getInt("lectureEndTime");
                String lectureRoom = rs.getString("lectureRoom");

                System.out.println("강의 이름: "+ getSubjectName(conn, csCode) + " 강의 요일: " + lectureDay + " 강의 시작 교시: " + lectureStartTime + " 교시 강의 끝 교시: "+ lectureEndTime + " 강의 실: "+lectureRoom);
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    public void enrollment(Connection conn, int sNumber) {
        int sel = 0;
        if (checkDate(conn, sNumber)) {
            while (true) {
                System.out.println("1. 수강신청");
                System.out.println("2. 수강신청 삭제");
                System.out.println("3. 뒤로 가기");
                System.out.print("메뉴를 선택하세요: ");
                try {
                    sel = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("다시 입력하여 주세요");
                    sc.next();
                    continue;
                }
                switch (sel) {
                    case 1:
                        insertEnrollment(conn, sNumber);
                        break;
                    case 2:
                        deleteEnrollment(conn, sNumber);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                        break;
                }
            }
        } else {
            System.out.println("수강신청 가능 기간이 아닙니다.");
            return;
        }
    }

    private openedSubjectDTO getThis(Connection conn, String csCode) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectQuery = "Select lectureStartTime, lectureEndTime, lectureDay from openedsubject where csCode = ?";
        openedSubjectDTO openedSubjectDTO = new openedSubjectDTO();
        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, csCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                openedSubjectDTO.setLectureDay(rs.getString("lectureDay"));
                openedSubjectDTO.setLectureStartTime(rs.getInt("lectureStartTime"));
                openedSubjectDTO.setLectureEndTime(rs.getInt("lectureEndTime"));
                }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return openedSubjectDTO;
        }
    }
    private String getSubjectName(Connection conn, String sCode) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectQuery = "Select sName from subject where sCode = ?";
        String sName = null;
        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, sCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                sName = rs.getString("sName");
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return sName;
        }
    }
    private boolean checkDate(Connection conn, int sNumber) {
        Date getDate = getDate(conn, sNumber);
        Date curDate = getCurDate(conn);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = fmt.format(getDate);
        String cDate = fmt.format(curDate);

        if (sDate.equals(cDate)) {
            return true;
        }
        return false;
    }

    private Date getDate(Connection conn, int sNumber) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EnrollmentPeriodDTO enrollmentPeriodDTO = new EnrollmentPeriodDTO();
        String selectQuery = "select period from enrollmentperiod where grade = (select grade from student where sNumber = ?)";
        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setInt(1, sNumber);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                enrollmentPeriodDTO.setPeriod(rs.getDate("period"));
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }

                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return enrollmentPeriodDTO.getPeriod();
    }

    private Date getCurDate(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        Date date = null;
        String getDateQuery = "select date_format(now(), '%Y-%m-%d')";
        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(getDateQuery);
            while (rs.next()) {
                date = rs.getDate("date_format(now(), '%Y-%m-%d')");
            }

        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {

                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }

                if (conn != null && !stmt.isClosed()) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return date;
    }

    private void insertEnrollment(Connection conn, int sNumber) {
        System.out.print("수강하고 싶은 과목 코드를 입력하세요: ");
        String sCode = sc.next();
        enrollmentDTO enrollmentDTO = new enrollmentDTO();
        enrollmentDTO.setSCode(sCode);
        enrollmentDTO.setSNumber(sNumber);
        insertEnrollment(conn, enrollmentDTO);
    }
    private int showcount(Connection conn, String sCode) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        String selectQuery = "select count(sCode) from union.enrollment where sCode = ?";

        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, sCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count(sCode)");
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return count;
        }
    }
    private boolean canStudy(Connection conn, enrollmentDTO enrollmentDTO) {//이게 내가 지금 넣으려고 하는 수강신청
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectQuery = "select sCode from enrollment where sNumber = ?";
        boolean result = true;
        try {
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setInt(1, enrollmentDTO.getSNumber());
            openedSubjectDTO openedSubjectDTO = getThis(conn, enrollmentDTO.getSCode());//내가 넣으려고 하는 개설 교과목의 일, 시작, 끝
            rs = pstmt.executeQuery();
            while (rs.next()) {//내가 넣어둔 수강신청 찾기
                String sCode = rs.getString("sCode");
                if(!checkTable(openedSubjectDTO, getThis(conn, sCode))) {
                    result = false;
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private boolean checkTable(openedSubjectDTO openedSubjectDTO, openedSubjectDTO openedSubjectDTO1) {//앞이 내가 넣으려고 하는거 뒤가 원래 들어가있던 거
        String thisDay = openedSubjectDTO.getLectureDay();
        String otherDay = openedSubjectDTO1.getLectureDay();
        int thisStart = openedSubjectDTO.getLectureStartTime();
        int otherStart = openedSubjectDTO1.getLectureStartTime();
        int thisEnd = openedSubjectDTO.getLectureEndTime();
        int otherEnd = openedSubjectDTO1.getLectureEndTime();
        if (!thisDay.equalsIgnoreCase(otherDay)) {
            return true;
        }
        if (otherStart >= thisStart && thisEnd >= otherEnd) {
            return false;
        }
        else if (otherStart < thisStart && thisEnd >= otherEnd && (otherEnd > thisStart)) {
            return false;
        }
        else if (otherStart >= thisStart && (thisEnd < otherEnd) && (thisEnd < otherStart)) {
            return false;
        }
        else if (otherStart > thisStart && thisEnd > otherEnd) {
            return false;
        }
        return true;
    }
    private void insertEnrollment(Connection conn, enrollmentDTO enrollmentDTO) {
        PreparedStatement pstmt = null;
        String createSubjectQuery = "insert into enrollment(sCode, sNumber, regdate) values (?, ?, now())";
        openedSubjectDTO openedSubjectDTO = new openedSubjectDTO();
        try {
            pstmt = conn.prepareStatement(createSubjectQuery);
            pstmt.setString(1, enrollmentDTO.getSCode());
            pstmt.setInt(2, enrollmentDTO.getSNumber());
            if (showcount(conn, enrollmentDTO.getSCode()) == openedSubjectDTO.getMAX_STUDENT()) {
                System.out.println("최대 수강 인원을 초과하였습니다.");
                return;
            }
            System.out.println("counting 완료");
            if (!canStudy(conn, enrollmentDTO)) {
                System.out.println("가능한 시간이 아닙니다.");
                return;
            }
            System.out.println("시간표 확인 완료");

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteEnrollment(Connection conn, int sNumber) {
        System.out.print("삭제하고 싶은 과목 코드를 입력하세요: ");
        String sCode = sc.next();
        enrollmentDTO enrollmentDTO = new enrollmentDTO();
        enrollmentDTO.setSCode(sCode);
        enrollmentDTO.setSNumber(sNumber);
        deleteEnrollment(conn, enrollmentDTO);
    }

    private void deleteEnrollment(Connection conn, enrollmentDTO enrollmentDTO) {
        PreparedStatement pstmt = null;
        String deleteSubjectQuery = "delete from enrollment where sCode = ? and sNumber = ?";
        try {
            pstmt = conn.prepareStatement(deleteSubjectQuery);
            pstmt.setString(1, enrollmentDTO.getSCode());
            pstmt.setInt(2, enrollmentDTO.getSNumber());
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            System.out.println("error : " + e);
        } finally {
            try {
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
