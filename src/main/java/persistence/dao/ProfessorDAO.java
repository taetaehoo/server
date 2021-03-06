package persistence.dao;

import org.apache.commons.dbcp2.DelegatingResultSet;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.PooledDataSource;
import persistence.dto.ProfessorDTO;
import persistence.dto.StudentDTO;
import persistence.dto.SubjectDTO;
import persistence.dto.openedSubjectDTO;
import service.SqlMapConfig;
import service.openedMapperInter;
import view.ManagerView;
import view.ProfessorView;

import javax.sql.DataSource;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ProfessorDAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private Scanner sc = new Scanner(System.in);
    private SqlSessionFactory factory = SqlMapConfig.getSqlSession();
    public void printList(List<openedSubjectDTO> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            System.out.println(list.toArray()[i].toString());
        }
    }
    public List<openedSubjectDTO> selectAll() {
        SqlSession sqlSession = factory.openSession();
        List<openedSubjectDTO> list = null;

        try {
            openedMapperInter inter = (openedMapperInter)sqlSession.getMapper(openedMapperInter.class);

            list = inter.selectAll();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            sqlSession.close();
        }
        return list;
    }

    public List<openedSubjectDTO> selectGrade(int grade) {
        SqlSession sqlSession = factory.openSession();
        List <openedSubjectDTO> list = null;

        try {
            openedMapperInter inter = (openedMapperInter) sqlSession.getMapper(openedMapperInter.class);

            list = inter.selectGrade(grade);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return list;
    }

    public List<openedSubjectDTO> selectProfessor(String pNumber) {
        SqlSession sqlSession = factory.openSession();
        List <openedSubjectDTO> list = null;

        try {
            openedMapperInter inter = (openedMapperInter) sqlSession.getMapper(openedMapperInter.class);

            list = inter.selectProfessor(pNumber);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return list;
    }

    public List<openedSubjectDTO> selectGradeAndProfessor(String pNumber, int grade) {
        SqlSession sqlSession = factory.openSession();
        List <openedSubjectDTO> list = null;

        try {
            openedMapperInter inter = (openedMapperInter) sqlSession.getMapper(openedMapperInter.class);
            list = inter.selectGradeAndProfessor(grade, pNumber);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return list;
    }
    public boolean updateData(openedSubjectDTO openedSubjectDTO) {
        boolean b = false;
        SqlSession sqlSession = factory.openSession();
        try {
            openedMapperInter inter = (openedMapperInter) sqlSession.getMapper(openedMapperInter.class);
            if (inter.updateData(openedSubjectDTO) > 0) {
                b = true;
                sqlSession.commit();
            }
        }catch(Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
        return b;
    }
    public boolean insertData(openedSubjectDTO openedSubjectDTO) {
        boolean b = false;
        SqlSession sqlSession = factory.openSession();
        try {
            openedMapperInter inter = (openedMapperInter) sqlSession.getMapper(openedMapperInter.class);
            if (inter.insertData(openedSubjectDTO) > 0) {
                    b= true;
                sqlSession.commit();
            }
        }catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
        return b;
    }


    public boolean login(Connection conn, String pNumber, String password) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "Select password from professor where pNumber = ?";

        boolean isSuccess = false;
        try {
            pstmt = conn.prepareStatement(loginQuery);
            pstmt.setString(1, pNumber);
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
    }//??????

    public void updatePrivacy(Connection conn, String pNumber) {
        System.out.println("????????? ????????? ???????????????: ");
        String pName = sc.next();
        System.out.println("????????? ??????????????? ???????????????: ");
        String password = sc.next();
        System.out.println("????????? ??????????????? ??????????????? (??? 010-****-****): ");
        String phoneNumber = sc.next();

        ProfessorDTO professorDTO = new ProfessorDTO();
        professorDTO.setPNumber(pNumber);
        professorDTO.setPName(pName);
        professorDTO.setPassword(password);
        professorDTO.setPhoneNumber(phoneNumber);
        updatePrivacy(conn, professorDTO);
    }

    private void updatePrivacy(Connection conn, ProfessorDTO professorDTO) {
        PreparedStatement pstmt = null;
        String updatePrivacyQuery = "update professor set pName = ?, password = ? , phoneNumber = ? where pNumber = ?";
        try {
            pstmt = conn.prepareStatement(updatePrivacyQuery);
            pstmt.setString(1, professorDTO.getPName());
            pstmt.setString(2, professorDTO.getPassword());
            pstmt.setString(3, professorDTO.getPhoneNumber());
            pstmt.setString(4, professorDTO.getPNumber());
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

    public void selRange(Connection conn) {
        int range;
        while (true) {
            System.out.println("1. ?????? ??????");
            System.out.println("2. ????????? ??????");
            System.out.println("3. ????????????");
            System.out.print("?????? ??????: ");
            try {
                range = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("????????? ?????? ???????????????.");
                sc.next();
                continue;
            }
            break;
        }
        if (range == 1)
            findAllSubject(conn);
        else {
            while (true) {
                System.out.print("????????? ????????? ??????????????????: ");
                try {
                    range = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("????????? ?????? ???????????????.");
                    sc.next();
                    continue;
                }
                switch (range) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        findSubject(conn, range);
                        break;
                    default:
                        break;
                }
                break;
            }
        }
    }

    private void findAllSubject(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        String selectAllSubjectQuery = "Select * from subject";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectAllSubjectQuery);
            while (rs.next()) {
                String sCode = rs.getString("sCode");
                String sName = rs.getString("sName");
                int credit = rs.getInt("credit");
                int grade = rs.getInt("grade");
                int semester = rs.getInt("semester");
                SubjectDTO subjectDTO = new SubjectDTO();
                subjectDTO.setSCode(sCode);
                subjectDTO.setSName(sName);
                subjectDTO.setCredit(credit);
                subjectDTO.setGrade(grade);
                subjectDTO.setSemester(semester);
                System.out.println("?????? ??????: " + subjectDTO.toString());
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

    private void findSubject(Connection conn, int range) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectSubjectQuery = "Select * from subject where grade = ?";

        try {
            pstmt = conn.prepareStatement(selectSubjectQuery);
            pstmt.setInt(1, range);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String sCode = rs.getString("sCode");
                String sName = rs.getString("sName");
                int credit = rs.getInt("credit");
                int grade = rs.getInt("grade");
                int semester = rs.getInt("semester");
                SubjectDTO subjectDTO = new SubjectDTO();
                subjectDTO.setSCode(sCode);
                subjectDTO.setSName(sName);
                subjectDTO.setCredit(credit);
                subjectDTO.setGrade(grade);
                subjectDTO.setSemester(semester);
                System.out.println("?????? ??????: " + subjectDTO.toString());
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

    public void selCourseDes(Connection conn) {
        int sel = 0;
        while (true) {
            System.out.println("1. ??????????????? ??????");
            System.out.println("2. ??????????????? ??????");
            System.out.println("3. ????????????");
            System.out.print("?????? ??????: ");
            try {
                sel = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("????????? ?????? ???????????????.");
                sc.next();
                continue;
            }
            switch (sel) {
                case 1:
                    insertCourse(conn);
                    break;
                case 2:
                    updateCourse(conn);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("?????? ?????????????????????.");
                    break;
            }
        }
    }

    private void insertCourse(Connection conn) {
        System.out.println("??????????????? ???????????????: ");
        String csCode = sc.next();
        openedSubjectDTO openedSubjectDTO = new openedSubjectDTO();
        openedSubjectDTO.setCsCode(csCode);
        if (checkOpenedSubject(conn, openedSubjectDTO)) {
            insertCourse(conn, openedSubjectDTO);
        } else {
            System.out.println("???????????? ?????? ?????? ?????? ?????????.");
        }

    }

    private void insertCourse(Connection conn, openedSubjectDTO openedSubjectDTO) {
        PreparedStatement pstmt = null;
        String insertQuery = "insert into coursedescription(subjectCode, week, plan) values (?, ?, ?)";
        try {
            if (!checkCource(conn, openedSubjectDTO)) {
                pstmt = conn.prepareStatement(insertQuery);
                for (int i = 1; i <= 15; i++) {
                    System.out.print(i + "????????? ?????? ????????? ???????????????: ");
                    if (i == 1)
                        sc.nextLine();
                    String newPlan = sc.nextLine();
                    pstmt.setString(1, openedSubjectDTO.getCsCode());
                    pstmt.setInt(2, i);
                    pstmt.setString(3, newPlan);
                    pstmt.executeUpdate();
                    conn.commit();
                }
            } else {
                System.out.println("??? ????????? ?????? ?????? ???????????? ???????????? ????????????.");
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

    private void updateCourse(Connection conn) {
        System.out.print("??????????????? ???????????????: ");
        String csCode = sc.next();
        openedSubjectDTO openedSubjectDTO = new openedSubjectDTO();
        openedSubjectDTO.setCsCode(csCode);
        int week;
        if (checkOpenedSubject(conn, openedSubjectDTO)) {
            while (true) {
                System.out.print("???????????? ?????? ????????? ???????????????: ");
                try {
                    week = sc.nextInt();
                    if (week >= 1 && week <= 15)
                        break;
                } catch (InputMismatchException e) {
                    System.out.println("?????? ????????? ?????????.");
                }
            }
            updateCourse(conn, openedSubjectDTO, week);
        } else {
            System.out.println("???????????? ?????? ?????? ?????? ?????????.");
        }
    }

    private void updateCourse(Connection conn, openedSubjectDTO openedSubjectDTO, int week) {
        PreparedStatement pstmt = null;
        String updateQuery = "update courseDescription set plan = ? where subjectCode = ? and week = ?";
        try {
            if (checkCource(conn, openedSubjectDTO)) {
                pstmt = conn.prepareStatement(updateQuery);
                System.out.print(week + "????????? ????????? ?????? ????????? ???????????????: ");
                sc.nextLine();
                String newPlan = sc.nextLine();
                pstmt.setString(1, newPlan);
                pstmt.setString(2, openedSubjectDTO.getCsCode());
                pstmt.setInt(3, week);
                pstmt.executeUpdate();
                conn.commit();
            } else {
                System.out.println("???????????????.");
            }
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

    private boolean checkOpenedSubject(Connection conn, openedSubjectDTO openedSubjectDTO) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loginQuery = "select 1 as cnt from openedSubject where exists (select 1 from openedSubject where csCode = ?)";
        boolean isExists = false;
        try {
            pstmt = conn.prepareStatement(loginQuery);
            pstmt.setString(1, openedSubjectDTO.getCsCode());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    isExists = true;
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

        }
        return isExists;
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

    public void findCoursed(Connection conn) {
        System.out.print("???????????? ?????? ????????? ????????? ???????????????: ");
        String csCode = sc.next();
        openedSubjectDTO openedSubjectDTO = new openedSubjectDTO();
        openedSubjectDTO.setCsCode(csCode);
        if (checkCource(conn, openedSubjectDTO)) {
            findCoursed(conn, openedSubjectDTO);
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

    public void selectTimeTable(Connection conn, String pNumber) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectSubjectQuery = "Select csCode, lectureDay, lectureStartTime, lectureEndTime from openedSubject where pNumber = ?";
        try {
            pstmt = conn.prepareStatement(selectSubjectQuery);
            pstmt.setString(1, pNumber);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String csCode = rs.getString("csCode");
                String sCode = selectSubjectName(conn, csCode);
                String lectureDay = rs.getString("lectureDay");
                String lectureStartTime = rs.getString("lectureStartTime");
                String lectureEndTime = rs.getString("lectureEndTime");
                System.out.println("?????? ??????: "+sCode+" ?????? ??????: " + lectureDay + " ?????? ?????? ??????: " + lectureStartTime + "?????? ?????? ??? ??????: " + lectureEndTime+"??????");
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

    private String selectSubjectName(Connection conn, String sCode) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sName = null;
        String selectSubjectQuery = "Select sName from Subject where sCode = ?";
        try {
            pstmt = conn.prepareStatement(selectSubjectQuery);
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

        }
        return sName;
    }
    public void selectEnrollmentStudent(Connection conn, String pNumber) {
        showOpenedCourseList(conn, pNumber);
        System.out.print("????????? ??????????????? ??????????????????: ");
        String csCode = sc.next();
        selectEnrollmentStudent(conn, pNumber , csCode);
    }
    private void selectEnrollmentStudent(Connection conn, String pNumber, String sCode) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList<String> list = new LinkedList<>();
        String selectSubjectQuery = "select sNumber from union.enrollment where sCode = ?";
        try {
            pstmt = conn.prepareStatement(selectSubjectQuery);
            pstmt.setString(1, sCode);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int sNumber = rs.getInt("sNumber");
                list.add(getInfo(conn, sNumber));
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
            showList(list);
        }
    }

    private void showList(LinkedList<String> list) {
        String [] arr = new String[list.size()];
        for (int i = 0; i< arr.length; i++) {
            arr[i] = list.pop();
        }

        int i = 0;
        while (true) {
            if (arr.length == 3) {
                if (i == 0) {
                    System.out.println(arr[0]);
                    System.out.println(arr[1]);
                }
                else {
                    System.out.println(arr[2]);
                }
                System.out.println(i + "page");
                if (i == 0)
                    System.out.println("1. ?????? ?????????");
                else
                    System.out.println("1. ?????? ?????????");
                System.out.println("2. ??????");
                System.out.print("?????? ??????: ");
                int sel = 0;
                inner:
                while (true) {
                    try {
                        sel = sc.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("????????? ????????? ?????????????????????.");
                        sc.next();
                        continue;
                    }
                    switch (sel) {
                        case 1:
                            if (i == 1) i = 0;
                            else i = 1;
                            break inner;
                        case 2:
                            return;
                        default:
                            System.out.println("??? ??????");
                            break;
                    }
                }
            }
            else if (arr.length < 3 && arr.length > 0) {
                for (int j = 0; j < list.size(); j++) {
                    System.out.println(arr[j]);
                }
                System.out.println("1 ???????????? ????????????.");
                return;
            }
            else {
                System.out.println("????????? ????????????.");
                return;
            }
        }
    }

    private void showOpenedCourseList(Connection conn, String pNumber) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectSubjectQuery = "Select csCode from openedsubject where pNumber = ?";
        try {
            pstmt = conn.prepareStatement(selectSubjectQuery);
            pstmt.setString(1, pNumber);

            rs = pstmt.executeQuery();
            System.out.println("?????? ????????? ?????? ??????");
            while (rs.next()) {
                String csCode = rs.getString("csCode");
                System.out.println(csCode);
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
    private String getInfo(Connection conn, int sNumber) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StudentDTO studentDTO = new StudentDTO();
        String selectSubjectQuery = "Select * from Student where sNumber = ?";
        try {
            pstmt = conn.prepareStatement(selectSubjectQuery);
            pstmt.setInt(1, sNumber);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                int studentNumber = rs.getInt("sNumber");
                String sName = rs.getString("sName");
                int grade = rs.getInt("grade");
                studentDTO.setSNumber(studentNumber);
                studentDTO.setSName(sName);
                studentDTO.setPassword("**********");
                studentDTO.setGrade(grade);
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
            return studentDTO.toString();
        }
    }
}