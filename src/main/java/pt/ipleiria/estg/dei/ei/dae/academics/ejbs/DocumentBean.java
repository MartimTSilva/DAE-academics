package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private StudentBean studentBean;

    public Document create(String filepath, String filename, String username) {
        Student student = studentBean.findStudent(username);
        Document document = new Document(filepath, filename, student);
        em.persist(document);
        return document;
    }

    public Document find(Long id) {
        return em.find(Document.class, id);
    }

    public List<Document> getStudentDocuments(String username) {
        Student student = studentBean.findStudent(username);
        return student.getDocuments();
    }
}