package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
                query = "SELECT s FROM Subject s ORDER BY s.course.name, s.scholarYear DESC, s.courseYear, s.name"
        )
})

@Entity
public class Subject extends Versionable implements Serializable {
    @Id
    private long code;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_code")
    private Course course;

    @JoinColumn(name = "course_year")
    private String courseYear;

    @JoinColumn(name = "scholar_year")
    private String scholarYear;

    @ManyToMany
    @JoinTable(name = "subjects_students",
            joinColumns = @JoinColumn(name = "subject_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "student_username", referencedColumnName = "username"))
    private List<Student> students;

    @ManyToMany
    @JoinTable(name = "subjects_teachers",
            joinColumns = @JoinColumn(name = "subject_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "teacher_username", referencedColumnName = "username"))
    private List<Teacher> teachers;

    public Subject() {
        this.students = new ArrayList<Student>();
        this.teachers = new ArrayList<Teacher>();
    }

    public Subject(long code, String name, Course course, String courseYear, String scholarYear) {
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

    public String getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public String getScholarYear() {
        return scholarYear;
    }

    public void setScholarYear(String scholarYear) {
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
