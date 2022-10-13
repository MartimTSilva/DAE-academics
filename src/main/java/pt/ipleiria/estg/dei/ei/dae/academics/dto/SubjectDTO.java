package pt.ipleiria.estg.dei.ei.dae.academics.dto;

import java.io.Serializable;

public class SubjectDTO implements Serializable {
    private long code;
    private String name;
    private long courseCode;
    private String courseName;
    private long courseYear;
    private long scholarYear;

    public SubjectDTO() {
    }

    public SubjectDTO(long code, String name, long courseCode, String courseName, long courseYear, long scholarYear) {
        this.code = code;
        this.name = name;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseYear = courseYear;
        this.scholarYear = scholarYear;
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

    public long getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(long courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
}