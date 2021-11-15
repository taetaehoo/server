import persistence.MyBatisConnectionFactory;
import persistence.dao.ManagerDAO;
import persistence.dao.ProfessorDAO;
import persistence.dto.openedSubjectDTO;
import view.LoginView;

public class Main {
    public static void main(String args[]){

    LoginView loginView = new LoginView();
    loginView.start();
//        openedSubjectDTO openedSubjectDTO = new openedSubjectDTO();
//        openedSubjectDTO.setCsCode("BA0001");
//        openedSubjectDTO.setPNumber("oh12");
//        openedSubjectDTO.setLectureStartTime(1);
//        openedSubjectDTO.setLectureEndTime(2);
//        openedSubjectDTO.setLectureDay("Fri");
//        openedSubjectDTO.setLectureRoom("D123");
//        openedSubjectDTO.setMaxStudent(openedSubjectDTO.getMAX_STUDENT());
//        openedSubjectDTO openedSubjectDTO2 = new openedSubjectDTO();
//        openedSubjectDTO2.setCsCode("BA0001");
//        openedSubjectDTO2.setPNumber("oh12");
//        openedSubjectDTO2.setLectureStartTime(2);
//        openedSubjectDTO2.setLectureEndTime(3);
//        openedSubjectDTO2.setLectureDay("Tue");
//        openedSubjectDTO2.setLectureRoom("D331");
//        openedSubjectDTO2.setMaxStudent(openedSubjectDTO2.getMAX_STUDENT());
//       ProfessorDAO professorDAO = new ProfessorDAO();
// //      professorDAO.insertData(openedSubjectDTO);
//        professorDAO.updateData(openedSubjectDTO2);
//
//        professorDAO.printList(professorDAO.selectAll());
//        professorDAO.printList(professorDAO.selectGrade(2));
//        professorDAO.printList(professorDAO.selectProfessor("oh12"));
//        professorDAO.printList(professorDAO.selectGradeAndProfessor("oh12", 2));

    }
}