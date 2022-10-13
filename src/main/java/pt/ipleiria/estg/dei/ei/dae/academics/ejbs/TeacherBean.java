package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TeacherBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email, long office) {
        Teacher teacher = new Teacher(username, password, name, email, office);
        em.persist(teacher); //saves in database
    }

    public Teacher findTeacher(String username) {
        return em.find(Teacher.class, username);
    }
}