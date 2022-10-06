package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    StudentBean studentBean;
    @EJB
    CourseBean courseBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");
        Course ei = new Course(1, "Engenharia Informática");
        courseBean.create(ei.getCode(), ei.getName());
        studentBean.create("martimtsilva", "123", "Martim", "martimtsilva@hotmail.com", ei.getCode());
    }
}