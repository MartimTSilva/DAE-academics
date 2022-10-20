package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dto.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.CourseBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static pt.ipleiria.estg.dei.ei.dae.academics.Codes.*;

@Path("subjects") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SubjectService {
    @EJB
    private SubjectBean subjectBean;

    @EJB
    private CourseBean courseBean;

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    public List<SubjectDTO> getAllSubjectsWS() {
        return toDTOs(subjectBean.getAllSubjects());
    }

    private SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
                subject.getCode(),
                subject.getName(),
                subject.getCourse().getCode(),
                subject.getCourse().getName(),
                subject.getCourseYear(),
                subject.getScholarYear()
        );
    }

    private StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getUsername(),
                student.getPassword(),
                student.getName(),
                student.getEmail(),
                student.getCourse().getCode(),
                student.getCourse().getName()
        );
    }

    @POST
    @Path("/")
    public Response createNewSubject(SubjectDTO subjectDTO) {
        subjectBean.create(
                subjectDTO.getCode(),
                subjectDTO.getName(),
                subjectDTO.getCourseYear(),
                subjectDTO.getScholarYear(),
                subjectDTO.getCourseCode()
        );

        Subject newSubject = subjectBean.findSubject(subjectDTO.getCode());
        if (newSubject == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED).entity(toDTO(newSubject)).build();
    }

    @GET
    @Path("/{subjectCode}/students")
    public Response getSubjectStudents(@PathParam("subjectCode") long subjectCode) {
        Subject subject = subjectBean.findSubject(subjectCode);
        if (subject != null) {
            List<StudentDTO> dtos =  studentsToDTOs(subject.getStudents());
            return Response.ok().entity(dtos).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("SUBJECT_NOT_FOUND")
                .build();
    }

    // converts an entire list of entities into a list of DTOs
    private List<SubjectDTO> toDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<StudentDTO> studentsToDTOs(List<Student> students) {
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }
}