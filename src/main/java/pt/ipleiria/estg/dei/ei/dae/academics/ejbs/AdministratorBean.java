package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Administrator;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email) {
        Administrator admin = new Administrator(username, password, name, email);
        em.persist(admin); //saves in database
    }
}