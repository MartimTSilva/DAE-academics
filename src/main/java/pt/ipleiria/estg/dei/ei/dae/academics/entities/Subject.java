package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(
        name = "subjects",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllSubjects",
                query = "SELECT s FROM Subject s ORDER BY s.name" // JPQL
        )
        //TODO: EX 7
})

@Entity
public class Subject implements Serializable {
    @Id
    private long code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "course_code")
    private Course course;

    @JoinColumn(name = "course_year")
    private long courseYear;
    @JoinColumn(name = "scholar_year")
    private long scholarYear;

    @ManyToMany
    @JoinTable(name = "subjects_students",
            joinColumns = @JoinColumn(name = "subject_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "student_username", referencedColumnName =
                    "username"))
    private List<Student> students;

    @ManyToMany
    @JoinTable(name = "subjects_teachers",
            joinColumns = @JoinColumn(name = "subject_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "teacher_username", referencedColumnName =
                    "username"))
    private List<Teacher> teachers;

    public Subject() {
        this.students = new ArrayList<Student>();
        this.teachers = new ArrayList<Teacher>();
    }

    public Subject(long code, String name, Course course, long courseYear, long scholarYear) {
        this.code = code;
        this.name = name;
        this.course = course;
        this.courseYear = courseYear;
        this.scholarYear = scholarYear;
        this.students = new ArrayList<Student>();
        this.teachers = new ArrayList<Teacher>();
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public long getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(long courseYear) {
        this.courseYear = courseYear;
    }

    public long getScholarYear() {
        return scholarYear;
    }

    public void setScholarYear(long scholarYear) {
        this.scholarYear = scholarYear;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public List<Teacher> getTeachers() {
        return new ArrayList<>(teachers);
    }

    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
    }
}
