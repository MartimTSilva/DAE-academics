package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "documents")
@NamedQuery(
        name = "getStudentDocuments",
        query = "SELECT doc FROM Document doc WHERE doc.student.username = :username"
)
@Entity
public class Document implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    private String filepath, filename;

    @ManyToOne
    private Student student;

    public Document() {
    }

    public Document(String filepath, String filename, Student student) {
        this.filepath = filepath;
        this.filename = filename;
        this.student = student;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
