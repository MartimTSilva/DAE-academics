package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

import static pt.ipleiria.estg.dei.ei.dae.academics.Codes.*;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private CourseBean courseBean;

    @Inject
    private Hasher hasher;

    public boolean exists(String username) {
        Query query = em.createQuery(
                "SELECT COUNT(s.username) FROM Student s WHERE s.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;
    }

    public void create(String username, String password, String name, String email, long courseCode)
            throws MyEntityNotFoundException, MyEntityExistsException, MyEntityExistsException.MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityExistsException("Student with username '" + username + "' already exists");
        }

        Course course = courseBean.findCourse(courseCode);
        if (course == null)
            throw new MyEntityNotFoundException("Course with code '" + courseCode + "' not found");

        try {
            Student student = new Student(username, hasher.hash(password), name, email, course);
            em.persist(student); //saves in database
            course.addStudent(student);
        } catch (ConstraintViolationException e) {
            throw new MyEntityExistsException.MyConstraintViolationException(e);
        }

    }

    public void update(String username, String password, String name, String email, long courseCode) {
        Student student = em.find(Student.class, username);
        if (student == null) {
            System.err.println("ERROR_STUDENT_NOT_FOUND: " + username);
            return;
        }
        em.lock(student, LockModeType.OPTIMISTIC);
        student.setPassword(password);
        student.setName(name);
        student.setEmail(email);

        // a "lazy way" that avoids querying the course every time we do an update to the student
        // plus: why we don't check if the other attributes changed too?
        if (!Objects.equals(student.getCourse().getCode(), courseCode)) {
            Course course = em.find(Course.class, courseCode);
            if (course == null) {
                System.err.println("ERROR_COURSE_NOT_FOUND: " + courseCode);
                return;
            }
            student.setCourse(course);
        }
    }

    public List<Student> getAllStudents() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Student>) em.createNamedQuery("getAllStudents").getResultList();
    }

    public Student findStudent(String username) {
        Student student = em.find(Student.class, username);
        Hibernate.initialize(student.getSubjects());
        return student;
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

    public List<Subject> getStudentSubjects(String username) {
        Student student = findStudent(username);

        Hibernate.initialize(student.getSubjects());
        return student.getSubjects();
    }
}