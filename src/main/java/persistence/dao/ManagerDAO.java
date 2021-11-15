package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.PooledDataSource;
import persistence.dto.*;
import view.ManagerView;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;

import java.util.*;

public class ManagerDAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private Scanner sc = new Scanner(System.in);
    private SqlSessionFactory sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
    public void register(Connection conn) {
        System.out.print("id 입력: ");
        String id = sc.next();
        System.out.print("비밀번호 입력: ");
        String password = sc.next();

        register(conn, id, password);
    }

    private void register(Connection conn, String id, String password) {
        PreparedStatement pstmt = null;
        String registerQuery = "insert into manager(id, password) values (?, ?)";
        try {
            pstmt = conn.prepareStatement(registerQuery);
            pstmt.setString(1, id);
            pstmt.setString(2, password);
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


    public boolean login(Connection conn, String id, String password) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "Select password from manager where id = ?";

        boolean isSuccess = false;
        try {
            pstmt = conn.prepareStatement(loginQuery);
            pstmt.setString(1, id);
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

    public void psRegister(Connection conn) {
        int sel;
        while (true) {
            System.out.println("계정 생성");
            System.out.println("1. 교수");
            System.out.println("2. 학생");
            System.out.print("입력: ");
            try {
                sel = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력 형식입니다.");
                sc.next();
                continue;
            }
            break;
        }
        switch (sel) {
            case 1:
                createProfessor(conn);
                break;
            case 2:
                createStudent(conn);
                break;
        }
    }

    private void createProfessor(Connection conn) {
        System.out.print("교번을 입력하세요: ");
        String pNumber = sc.next();
        System.out.print("이름을 입력하세요: ");
        String pName = sc.next();
        System.out.print("비밀번호를 입력하세요: ");
        String password = sc.next();
        System.out.print("전화번호를 입력하세요: (예 010-****-****)");
        String phoneNumber = sc.next();

        ProfessorDTO professorDTO = new ProfessorDTO();
        professorDTO.setPNumber(pNumber);
        professorDTO.setPName(pName);
        professorDTO.setPassword(password);
        professorDTO.setPhoneNumber(phoneNumber);
        createProfessor(conn, professorDTO);
    }

    private boolean checkProfessor(Connection conn, ProfessorDTO professorDTO) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "select 1 as cnt from subject where exists (select 1 from professor where pNumber = ?)";
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

    private void createProfessor(Connection conn, ProfessorDTO professorDTO) {
        PreparedStatement pstmt = null;
        String registerQuery = "insert into professor(pNumber, pName, password, phoneNumber) values (?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(registerQuery);
            pstmt.setString(1, professorDTO.getPNumber());
            pstmt.setString(2, professorDTO.getPName());
            pstmt.setString(3, professorDTO.getPassword());
            pstmt.setString(4, professorDTO.getPhoneNumber());
            if (!checkProfessor(conn, professorDTO)) {
                pstmt.executeUpdate();
                conn.commit();
            } else {
                System.out.println("이미 입력된 교번의 데이터 입니다.");
            }
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

    private void createStudent(Connection conn) {
        int sNumber;
        while (true) {
            System.out.print("학번을 입력하세요: ");
            try {
                sNumber = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("잘못된 형식입니다.");
                sc.next();
                continue;
            }
            break;
        }
        System.out.print("이름을 입력하세요: ");
        String sName = sc.next();
        System.out.print("비밀번호를 입력하세요: ");
        String password = sc.next();
        int grade;
        while (true) {
            System.out.print("학년을 입력하세요: ");
            try {
                grade = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("잘못된 형식입니다.");
                sc.next();
                continue;
            }
            break;
        }
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setSNumber(sNumber);
        studentDTO.setSName(sName);
        studentDTO.setPassword(password);
        studentDTO.setGrade(grade);
        createStudent(conn, studentDTO);
    }

    private boolean checkStudent(Connection conn, StudentDTO studentDTO) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "select 1 as cnt from subject where exists (select 1 from student where sNumber = ?)";
        boolean isExists = false;
        try {
            pstmt = conn.prepareStatement(loginQuery);
            pstmt.setInt(1, studentDTO.getSNumber());
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

    private void createStudent(Connection conn, StudentDTO studentDTO) {
        PreparedStatement pstmt = null;
        String registerQuery = "insert into student(sNumber, sName, password, grade) values (?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(registerQuery);
            pstmt.setInt(1, studentDTO.getSNumber());
            pstmt.setString(2, studentDTO.getSName());
            pstmt.setString(3, studentDTO.getPassword());
            pstmt.setInt(4, studentDTO.getGrade());
            if (!checkStudent(conn, studentDTO)) {
                pstmt.executeUpdate();
                conn.commit();
            } else {
                System.out.println("해당 학번의 데이터가 이미 존재합니다.");
            }
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
    }//완료

    public void psSelect(Connection conn) {
        int sel;
        while (true) {
            System.out.println("정보 조회");
            System.out.println("1. 교수");
            System.out.println("2. 학생");
            System.out.print("입력: ");
            try {
                sel = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력 형식입니다.");
                sc.next();
                continue;
            }
            break;
        }
        switch (sel) {
            case 1:
                selectAllProfessor(conn);
                break;
            case 2:
                selectAllStudent(conn);
                break;
        }
    }

    private void selectAllProfessor(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        String selectAllProfessorQuery = "Select * from professor";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectAllProfessorQuery);
            while (rs.next()) {
                String pNumber = rs.getString("pNumber");
                String pName = rs.getString("pName");
                String password = rs.getString("password");
                String phoneNumber = rs.getString("phoneNumber");
                ProfessorDTO professorDTO = new ProfessorDTO();
                professorDTO.setPNumber(pNumber);
                professorDTO.setPName(pName);
                professorDTO.setPassword(password);
                professorDTO.setPhoneNumber(phoneNumber);
                System.out.println("교수 정보: " + professorDTO.toString());
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

    public void findAllOpenedSubject() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            List<Map<String, SubjectDTO>> result = session.selectList("mapper.SubjectMapper.selectAll");
            printList(result);
        }finally {
            session.close();
        }
    }

    private void printList(List<Map<String, SubjectDTO>> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            System.out.println((list.toArray())[i].toString());
        }
    }

    private void selectAllStudent(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        String selectAllStudentQuery = "Select * from student";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectAllStudentQuery);
            while (rs.next()) {
                int sNumber = rs.getInt("sNumber");
                String sName = rs.getString("sName");
                String password = rs.getString("password");
                int grade = rs.getInt("grade");
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setSNumber(sNumber);
                studentDTO.setSName(sName);
                studentDTO.setPassword(password);
                studentDTO.setGrade(grade);
                System.out.println("교수 정보: " + studentDTO.toString());
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

    public void subjectCUD(Connection conn) {
        int sel = 0;
        while (true) {
            System.out.println("1. 생성");
            System.out.println("2. 변경");
            System.out.println("3. 삭제");
            System.out.println("4. 뒤로가기");
            System.out.print("메뉴 선택: ");
            try {
                sel = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력 형식입니다.");
                sc.next();
                continue;
            }
            switch (sel) {
                case 1:
                case 2:
                    cuSubject(conn, sel);
                    break;
                case 3:
                    deleteSubject(conn);
                    break;
                case 4:
                    System.out.println("뒤로가기");
                    return;
                default:
                    System.out.println("잘못 입력하였습니다.");
                    break;
            }
            break;
        }
    }

    private void showInfoOfProfessor() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectAllProfessorQuery = "Select pNumber, pName from professor";

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectAllProfessorQuery);
            while (rs.next()) {
                String pNumber = rs.getString("pNumber");
                String pName = rs.getString("pName");
                ProfessorDTO professorDTO = new ProfessorDTO();
                professorDTO.setPNumber(pNumber);
                professorDTO.setPName(pName);
                System.out.print("professorDTO.getPNumber() = " + professorDTO.getPNumber());
                System.out.println(" professorDTO.getPName() = " + professorDTO.getPName());
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
    private void showOpened(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        String selectAllSubjectQuery = "Select csCode from openedsubject";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectAllSubjectQuery);
            while (rs.next()) {
                String csCode = rs.getString("csCode");

                System.out.println("과목 코드: "+csCode);
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
    private void cuSubject(Connection conn, int sel) {
        System.out.print("과목 코드를 입력해주세요: ");
        String sCode = sc.next();
        System.out.print("과목 이름을 입력해주세요: ");
        String sName = sc.next();
        int credit = 0;
        int grade = 0;
        int semester = 0;
        while (true) {
            System.out.print("학점을 입력해주세요: ");
            try {
                credit = sc.nextInt();
            }catch (InputMismatchException e) {
                System.out.println("잘못된 형식입니다.");
                sc.next();
                continue;
            }
            break;
        }
        while (true) {
            System.out.print("학년을 입력해주세요: ");
            try {
                grade = sc.nextInt();
            }catch (InputMismatchException e) {
                System.out.println("잘못된 형식입니다.");
                sc.next();
                continue;
            }
            break;
        }
        while (true) {
            System.out.print("학기을 입력해주세요: ");
            try {
                semester = sc.nextInt();
            }catch (InputMismatchException e) {
                System.out.println("잘못된 형식입니다.");
                sc.next();
                continue;
            }
            break;
        }
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setSCode(sCode);
        subjectDTO.setSName(sName);
        subjectDTO.setCredit(credit);
        subjectDTO.setGrade(grade);
        subjectDTO.setSemester(semester);


        if (sel == 1)
            createSubject(subjectDTO);
        else
            updateSubject(subjectDTO);
    }
    public void createSubject(SubjectDTO subjectDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("sCode", subjectDTO.getSCode());
            param.put("sName", subjectDTO.getSName());
            param.put("credit", subjectDTO.getCredit());
            param.put("grade", subjectDTO.getGrade());
            param.put("semester", subjectDTO.getSemester());
            int result = session.insert("mapper.SubjectMapper.insert", param);
            session.commit();
        }finally {
            session.close();
        }
    }
    private void updateSubject(SubjectDTO subjectDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("sCode", subjectDTO.getSCode());
            param.put("sName", subjectDTO.getSName());
            param.put("credit", subjectDTO.getCredit());
            param.put("grade", subjectDTO.getGrade());
            param.put("semester", subjectDTO.getSemester());
            int result = session.update("mapper.SubjectMapper.update", param);
            session.commit();
        }finally {
            session.close();
        }
    }

    private void deleteSubject(Connection conn) {
        System.out.print("삭제하려는 과목코드를 입력하세요: ");
        String csCode = sc.next();
        deleteSubject(conn, csCode);
    }

    private void deleteSubject(Connection conn, String csCode) {
        PreparedStatement pstmt = null;
        String deleteSubjectQuery = "delete from openedSubject where csCode = ?";
        try {
            openedSubjectDTO openedSubjectDTO = new openedSubjectDTO();
            openedSubjectDTO.setCsCode(csCode);

            pstmt = conn.prepareStatement(deleteSubjectQuery);
            pstmt.setString(1, csCode);
            if (checkOpenedSubject(openedSubjectDTO)) {
                pstmt.executeUpdate();
                conn.commit();
            } else {
                System.out.println("해당 데이터가 존재하지 않습니다.");
            }
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

    private boolean checkSubject(SubjectDTO subjectDTO) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "select 1 as cnt from subject where exists (select 1 from subject where sCode = ?)";
        boolean isExists = false;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(loginQuery);
            pstmt.setString(1, subjectDTO.getSCode());
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

    private boolean checkOpenedSubject(openedSubjectDTO openedSubjectDTO) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "select 1 as cnt from openedSubject where exists (select 1 from openedSubject where csCode = ?)";
        boolean isExists = false;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

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

    public void insertCoursedPeriod(Connection conn) {
        System.out.print("원하는 연도를 입력하세요: ");
        int year = sc.nextInt();
        System.out.print("원하는 월을 입력하세요: ");
        int month = sc.nextInt();
        System.out.print("원하는 일을 입력하세요: ");
        int day = sc.nextInt();
        insertCoursedPeriod(conn, transformDate(Integer.toString(year), Integer.toString(month), Integer.toString(day)));
    }

    private Date transformDate(String year, String month, String day) {
        String date = year + "-"+month+"-"+day;
        Date d = Date.valueOf(date);
        return d;
    }

    private void insertCoursedPeriod(Connection conn, Date date) {
        PreparedStatement pstmt = null;
        String createSubjectQuery = "update openedSubject set periodCourseDescription = ?";
        try {
            pstmt = conn.prepareStatement(createSubjectQuery);
            pstmt.setDate(1, date);

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

    public void insertEnrollmentPeriod(Connection conn) {
        System.out.print("원하는 학년을 입력해주세요: ");
        int grade = sc.nextInt();
        System.out.print("원하는 연도를 입력하세요: ");
        int year = sc.nextInt();
        System.out.print("원하는 월을 입력하세요: ");
        int month = sc.nextInt();
        System.out.print("원하는 일을 입력하세요: ");
        int day = sc.nextInt();
        insertEnrollmentPeriod(conn, grade, transformDate(Integer.toString(year), Integer.toString(month), Integer.toString(day)));
    }
    private void insertEnrollmentPeriod(Connection conn, int grade, Date date) {
        PreparedStatement pstmt = null;
        String updateQuery = "update enrollmentperiod set Period = ? where grade = ?";
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setDate(1, date);
            pstmt.setInt(2, grade);

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
}
