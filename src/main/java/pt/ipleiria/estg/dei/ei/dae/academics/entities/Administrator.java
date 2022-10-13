package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Administrator extends User implements Serializable {
    public Administrator() {
        super(null, null, null, null);
    }

    public Administrator(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}
