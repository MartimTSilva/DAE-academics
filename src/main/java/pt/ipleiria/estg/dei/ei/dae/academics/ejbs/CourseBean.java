package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CourseBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long code, String name) {
        Course course = new Course(code, name);
        em.persist(course); //saves in database
    }

    public List<Course> getAllCourses() {
        return (List<Course>) em.createNamedQuery("getAllCourses").getResultList();
    }

    public Course findCourse(long code) {
        return em.find(Course.class, code);
    }
}