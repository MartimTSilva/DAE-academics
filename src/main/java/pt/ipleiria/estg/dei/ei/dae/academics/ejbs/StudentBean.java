package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email, long courseCode) {
        Course c = em.find(Course.class, courseCode);
        if (c == null)
            return;

        Student student = new Student(username, password, name, email, c);
        em.persist(student); //saves in database
    }

    public List<Student> getAllStudents() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Student>) em.createNamedQuery("getAllStudents").getResultList();
    }

    public Student findStudent(String username) {
        return em.find(Student.class, username);
    }

    public void enrollStudentInSubject(String username, long subjectCode) {
        //Check if user exists
        Student student = findStudent(username);
        if (student == null)
            return;

        //Check if subject exists
        Subject subject = em.find(Subject.class, subjectCode);
        if (subject == null)
            return;

        //Check if subject belongs to course
        if (!student.getCourse().equals(subject.getCode()))
            return;

        //Check if student already has the subject
        if (!student.getSubjects().contains(subject))
            student.addSubject(subject);

        //Check if subject already has the student
        if (!subject.getStudents().contains(subject))
            subject.addStudent(student);
    }
}