package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dto.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static pt.ipleiria.estg.dei.ei.dae.academics.Codes.*;

@Path("students") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class StudentService {
    @EJB
    private StudentBean studentBean;

    @EJB
    private SubjectBean subjectBean;

    @GET
    public List<StudentDTO> getAllStudentsWS() {
        return studentsToDTOs(studentBean.getAllStudents());
    }

    // Converts an entity Student to a DTO Student class
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

    private SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
                subject.getCode(),
                subject.getName(),
                subject.getCourse().getCode(),
                subject.getCourse().getName(),
                subject.getScholarYear(),
                subject.getScholarYear()
        );
    }

    @POST
    @Path("/")
    public Response createNewStudent(StudentDTO studentDTO) {
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );

        Student newStudent = studentBean.findStudent(studentDTO.getUsername());
        if (newStudent == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(toDTO(newStudent)).build();
    }

    @GET
    @Path("{username}")
    public Response getStudentDetails(@PathParam("username") String username) {
        Student student = studentBean.findStudent(username);
        if (student != null) {
            return Response.ok(toDTO(student)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @GET
    @Path("{username}/subjects")
    public Response getStudentSubjects(@PathParam("username") String username) {
        Student student = studentBean.findStudent(username);
        if (student != null) {
            List<SubjectDTO> dtos = subjectsToDTOs(student.getSubjects());
            return Response.ok(dtos).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @POST
    @Path("{username}/enroll-subject/{code}")
    public Response addStudentSubject(@PathParam("username") String username, @PathParam("code") long code) {
        int res = studentBean.enrollStudentInSubject(username, code);

        switch (res) {
            case OK:
                return Response.ok().build();
            case USER_NOT_FOUND:
                return Response.status(Response.Status.CONFLICT).entity("User not found.").build();
            case SUBJECT_NOT_FOUND:
                return Response.status(Response.Status.CONFLICT).entity("Subject not found.").build();
            case SUBJECT_COURSE_NOT_MATCH:
                return Response.status(Response.Status.CONFLICT).entity("Subject does not belong to user course.").build();
            case USER_ALREADY_ENROLLED:
                return Response.status(Response.Status.CONFLICT).entity("User already enrolled.").build();
            default:
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("{username}/unroll-subject/{code}")
    public Response removeStudentSubject(@PathParam("username") String username, @PathParam("code") long code) {
        int res = studentBean.unrollStudentInSubject(username, code);

        switch (res) {
            case OK:
                return Response.ok().build();
            case USER_NOT_FOUND:
                return Response.status(Response.Status.CONFLICT).entity("User not found.").build();
            case SUBJECT_NOT_FOUND:
                return Response.status(Response.Status.CONFLICT).entity("Subject not found.").build();
            case SUBJECT_COURSE_NOT_MATCH:
                return Response.status(Response.Status.CONFLICT).entity("Subject does not belong to user course.").build();
            case USER_NOT_ENROLLED:
                return Response.status(Response.Status.CONFLICT).entity("User not enrolled.").build();
            default:
                return Response.serverError().build();
        }
    }

    // converts an entire list of entities into a list of DTOs
    private List<StudentDTO> studentsToDTOs(List<Student> students) {
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }
}