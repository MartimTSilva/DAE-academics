package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "students")
@NamedQueries({
        @NamedQuery(
                name = "getAllStudents",
                query = "SELECT s FROM Student s ORDER BY s.name" // JPQL
        )
})

@Entity
public class Student extends User implements Serializable {
    @ManyToOne
    @JoinColumn(name = "course_code")
    @NotNull
    private Course course;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    private List<Subject> subjects;

    @OneToMany(mappedBy = "student")
    private List<Document> documents;

    public Student() {
        super(null,null,null,null);
        this.subjects = new ArrayList<Subject>();
    }

    public Student(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
        this.course = course;
        this.subjects = new ArrayList<Subject>();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Subject> getSubjects() {
        return new ArrayList<>(subjects);
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Document> getDocuments() {
        return new ArrayList<>(documents);
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public void removeSubjects(Subject subject) {
        this.subjects.remove(subject);
    }
}