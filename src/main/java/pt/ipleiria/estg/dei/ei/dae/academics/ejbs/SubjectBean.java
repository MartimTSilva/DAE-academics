package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SubjectBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long code, String name, Course course, long courseYear, long scholarYear) {
        Subject subject = new Subject(code, name, course, courseYear, scholarYear);
        em.persist(subject); //saves in database
    }

    public List<Course> getAllSubjects() {
        return (List<Course>) em.createNamedQuery("getAllSubjects").getResultList();
    }

    public Subject findSubject(long code) {
        return em.find(Subject.class, code);
    }
}