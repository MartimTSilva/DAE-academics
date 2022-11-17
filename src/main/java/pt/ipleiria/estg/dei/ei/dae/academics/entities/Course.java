package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(
        name = "courses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllCourses",
                query = "SELECT s FROM Course s ORDER BY s.name" // JPQL
        )
})

@Entity
public class Course extends Versionable implements Serializable {
    @Id
    private long code;

    private String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Student> students;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Subject> subjects;

    public Course() {
        this.students = new ArrayList<Student>();
        this.subjects = new ArrayList<Subject>();
    }

    public Course(long code, String name) {
        this.code = code;
        this.name = name;
        this.students = new ArrayList<Student>();
        this.subjects = new ArrayList<Subject>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public List<Subject> getSubjects() {
        return new ArrayList<>(subjects);
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public void removeSubjects(Subject subject) {
        this.subjects.remove(subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return code == course.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
