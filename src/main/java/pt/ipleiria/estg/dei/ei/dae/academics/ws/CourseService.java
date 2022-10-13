package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dto.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.CourseBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("courses") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class CourseService {
    @EJB
    private CourseBean courseBean;

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    public List<CourseDTO> getAllCoursesWS() {
        return toDTOs(courseBean.getAllCourses());
    }

    private CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getCode(),
                course.getName()
        );
    }

    @POST
    @Path("/")
    public Response createNewCourse(CourseDTO courseDTO) {
        courseBean.create(
                courseDTO.getCode(),
                courseDTO.getName()
        );

        Course newCourse = courseBean.findCourse(courseDTO.getCode());
        if (newCourse == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(toDTO(newCourse)).build();
    }


    // converts an entire list of entities into a list of DTOs
    private List<CourseDTO> toDTOs(List<Course> courses) {
        return courses.stream().map(this::toDTO).collect(Collectors.toList());
    }
}