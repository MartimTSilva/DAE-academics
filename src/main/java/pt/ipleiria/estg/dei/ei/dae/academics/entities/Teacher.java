package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher extends User implements Serializable {
    private long office;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "teachers")
    private List<Subject> subjects;

    public Teacher() {
        super(null, null, null, null);
        this.subjects = new ArrayList<Subject>();
    }

    public Teacher(String username, String password, String name, String email, long office) {
        super(username, password, name, email);
        this.office = office;
        this.subjects = new ArrayList<Subject>();
    }

    public long getOffice() {
        return office;
    }

    public List<Subject> getSubjects() {
        return new ArrayList<Subject>(subjects);
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public void removeSubjects(Subject subject) {
        this.subjects.remove(subject);
    }
}
