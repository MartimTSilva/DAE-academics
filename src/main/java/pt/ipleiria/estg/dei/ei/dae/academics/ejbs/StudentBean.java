package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email, int courseCode) {
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

    public Student find(String username) {
        return em.find(Student.class, username);
    }
}