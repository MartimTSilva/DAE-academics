package pt.ipleiria.estg.dei.ei.dae.academics.dto;

import java.io.Serializable;

public class TeacherDTO implements Serializable {
    private String username;
    private String password, name, email;

    private long office;


    public TeacherDTO() {
    }

    public TeacherDTO(String username, String password, String name, String email, long office) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.office = office;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getOffice() {
        return office;
    }

    public void setOffice(long office) {
        this.office = office;
    }
}