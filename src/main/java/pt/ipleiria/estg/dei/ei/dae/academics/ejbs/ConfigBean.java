package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    StudentBean studentBean;
    @EJB
    CourseBean courseBean;

    @EJB
    SubjectBean subjectBean;

    @EJB
    AdministratorBean administratorBean;

    @EJB
    TeacherBean teacherBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() throws MyEntityNotFoundException, MyEntityExistsException, MyEntityExistsException.MyConstraintViolationException {
        courseBean.create(1, "Engenharia Informática");

        studentBean.create("martimtsilva", "123", "Martim", "martimtsilva@hotmail.com", 1);

        subjectBean.create(1, "Desenvolvimento de Aplicações Empresariais", "2022", "2023", 1);
        subjectBean.create(2, "Sistemas de Apoio à Decisão", "2022", "2023", 1);
        subjectBean.create(3, "Tópicos Avançados de Engenharia de Software", "2022", "2023", 1);
        subjectBean.create(4, "Desenvolvimento de Aplicações Distribuídas", "2022", "2023", 1);

        studentBean.findStudent("martimtsilva").addSubject(subjectBean.findSubject(1));
        studentBean.findStudent("martimtsilva").addSubject(subjectBean.findSubject(4));

        subjectBean.findSubject(1).addStudent(studentBean.findStudent("martimtsilva"));
        subjectBean.findSubject(4).addStudent(studentBean.findStudent("martimtsilva"));

        courseBean.findCourse(1).addStudent(studentBean.findStudent("martimtsilva"));
        courseBean.findCourse(1).addSubject(subjectBean.findSubject(1));
        courseBean.findCourse(1).addSubject(subjectBean.findSubject(2));
        courseBean.findCourse(1).addSubject(subjectBean.findSubject(3));
        courseBean.findCourse(1).addSubject(subjectBean.findSubject(4));

        administratorBean.create("admin", "admin123", "Administrador", "admin@root.com");

        teacherBean.create("professor", "prof123", "Professor Professorson", "prof@ipleiria.pt", 5);
        teacherBean.findTeacher("professor").addSubject(subjectBean.findSubject(1));
        teacherBean.findTeacher("professor").addSubject(subjectBean.findSubject(3));
        subjectBean.findSubject(1).addTeacher(teacherBean.findTeacher("professor"));
        subjectBean.findSubject(3).addTeacher(teacherBean.findTeacher("professor"));

        try {
            studentBean.create("foo2", "bar", "foo2", "foo2@bar.com", 65656);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}