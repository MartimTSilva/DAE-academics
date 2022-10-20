package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import static pt.ipleiria.estg.dei.ei.dae.academics.Codes.*;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private CourseBean courseBean;

    public void create(String username, String password, String name, String email, long courseCode) {
        Course course = courseBean.findCourse(courseCode);
        if (course == null)
            return;

        Student student = new Student(username, password, name, email, course);
        em.persist(student); //saves in database
        course.addStudent(student);
    }

    public List<Student> getAllStudents() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Student>) em.createNamedQuery("getAllStudents").getResultList();
    }

    public Student findStudent(String username) {
        return em.find(Student.class, username);
    }

    public int enrollStudentInSubject(String username, long subjectCode) {
        //Check if user exists
        Student student = findStudent(username);
        if (student == null)
            return USER_NOT_FOUND;

        //Check if subject exists
        Subject subject = em.find(Subject.class, subjectCode);
        if (subject == null)
            return SUBJECT_NOT_FOUND;

        //Check if subject course is the same as the student course
        if (!student.getCourse().equals(subject.getCourse()))
            return SUBJECT_COURSE_NOT_MATCH;

        //Check if student already has the subject
        if (student.getSubjects().contains(subject))
            return USER_ALREADY_ENROLLED;

        student.addSubject(subject);

        //Check if subject already has the student
        if (subject.getStudents().contains(student))
            return USER_ALREADY_ENROLLED;

        subject.addStudent(student);
        return OK;
    }

    public int unrollStudentInSubject(String username, long subjectCode) {
        //Check if user exists
        Student student = findStudent(username);
        if (student == null)
            return USER_NOT_FOUND;

        //Check if subject exists
        Subject subject = em.find(Subject.class, subjectCode);
        if (subject == null)
            return SUBJECT_NOT_FOUND;

        //Check if subject course is the same as the student course
        if (!student.getCourse().equals(subject.getCourse()))
            return SUBJECT_COURSE_NOT_MATCH;

        //Check if student already has the subject
        if (!student.getSubjects().contains(subject))
            return USER_NOT_ENROLLED;

        student.removeSubjects(subject);

        //Check if subject already has the student
        if (!subject.getStudents().contains(student))
            return USER_NOT_ENROLLED;

        subject.removeStudent(student);
        return OK;
    }
}