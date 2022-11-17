package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static pt.ipleiria.estg.dei.ei.dae.academics.Codes.*;

@Stateless
public class TeacherBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email, long office) {
        Teacher teacher = new Teacher(username, password, name, email, office);
        em.persist(teacher); //saves in database
    }

    public List<Teacher> getAllTeachers() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Teacher>) em.createNamedQuery("getAllTeachers").getResultList();
    }

    public Teacher findTeacher(String username) {
        return em.find(Teacher.class, username);
    }

    public int associateTeacherInSubject(String username, long subjectCode) {
        //Check if user exists
        Teacher teacher = findTeacher(username);
        if (teacher == null)
            return USER_NOT_FOUND;

        //Check if subject exists
        Subject subject = em.find(Subject.class, subjectCode);
        if (subject == null)
            return SUBJECT_NOT_FOUND;


        //Check if student already has the subject
        if (teacher.getSubjects().contains(subject))
            return USER_ALREADY_ENROLLED;

        teacher.addSubject(subject);

        //Check if subject already has the student
        if (subject.getTeachers().contains(teacher))
            return USER_ALREADY_ENROLLED;

        subject.addTeacher(teacher);
        return OK;
    }

    public int dissociateTeacherInSubject(String username, long subjectCode) {
        //Check if user exists
        Teacher teacher = findTeacher(username);
        if (teacher == null)
            return USER_NOT_FOUND;

        //Check if subject exists
        Subject subject = em.find(Subject.class, subjectCode);
        if (subject == null)
            return SUBJECT_NOT_FOUND;

        //Check if student already has the subject
        if (!teacher.getSubjects().contains(subject))
            return USER_NOT_ENROLLED;

        teacher.removeSubjects(subject);

        //Check if subject already has the student
        if (!subject.getTeachers().contains(teacher))
            return USER_NOT_ENROLLED;

        subject.removeTeacher(teacher);
        return OK;
    }

    public List<Subject> getTeacherSubjects(Teacher teacher) {
        Hibernate.initialize(teacher.getSubjects());
        return teacher.getSubjects();
    }
}