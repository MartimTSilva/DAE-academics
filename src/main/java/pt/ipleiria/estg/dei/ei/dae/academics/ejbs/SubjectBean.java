package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.dto.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SubjectBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private CourseBean courseBean;

    public void create(long code, String name, long courseYear, long scholarYear, long courseCode) {
        Course course = courseBean.findCourse(courseCode);
        if (course == null)
            return;

        Subject subject = new Subject(code, name, course, courseYear, scholarYear);
        em.persist(subject);
        course.addSubject(subject);
    }

    public List<Subject> getAllSubjects() {
        return (List<Subject>) em.createNamedQuery("getAllSubjects").getResultList();
    }

    public Subject findSubject(long code) {
        return em.find(Subject.class, code);
    }
}