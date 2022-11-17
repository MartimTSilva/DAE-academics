package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dto.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;

import javax.ws.rs.core.SecurityContext;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static pt.ipleiria.estg.dei.ei.dae.academics.Codes.*;

@Path("students") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
@Authenticated
@RolesAllowed({"Teacher", "Administrator"})
public class StudentService {
    @EJB
    private StudentBean studentBean;

    @EJB
    private EmailBean emailBean;

    @Context
    private SecurityContext securityContext;

    @GET
    public List<StudentDTO> getAllStudentsWS() {
        return toDTOsNoSubjects(studentBean.getAllStudents());
    }

    private StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getUsername(),
                student.getPassword(),
                student.getName(),
                student.getEmail(),
                student.getCourse().getCode(),
                student.getCourse().getName(),
                subjectsToDTOs(student.getSubjects())
        );
    }

    // Converts an entity Student to a DTO Student class
    private StudentDTO toDTONoSubjects(Student student) {
        return new StudentDTO(
                student.getUsername(),
                student.getPassword(),
                student.getName(),
                student.getEmail(),
                student.getCourse().getCode(),
                student.getCourse().getName(),
                null
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
    public Response createNewStudent(StudentDTO studentDTO)
            throws MyEntityNotFoundException, MyEntityExistsException, MyEntityExistsException.MyConstraintViolationException {
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

    @PUT
    @Path("/")
    public Response editStudentDetails(StudentDTO studentDTO) {
        studentBean.update(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );

        Student student = studentBean.findStudent(studentDTO.getUsername());
        if (student == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(toDTO(student)).build();
    }

    @GET
    @Authenticated
    @RolesAllowed({"Student"})
    @Path("{username}")
    public Response getStudentDetails(@PathParam("username") String username) {
        Student student = studentBean.findStudent(username);
        if (student != null) {
            return Response.ok(toDTO(student)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @GET
    @Path("{username}/subjects")
    public Response getStudentSubjects(@PathParam("username") String username) {
        List<SubjectDTO> dtos = subjectsToDTOs(studentBean.getStudentSubjects(username));
        return Response.ok(dtos).build();
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

    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email)
            throws MyEntityNotFoundException, MessagingException {

        Student student = studentBean.findStudent(username);
        if (student == null) {
            throw new MyEntityNotFoundException("Student with username '" + username + "' not found in our records.");
        }

        emailBean.send(student.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }

    // converts an entire list of entities into a list of DTOs
    private List<StudentDTO> toDTOsNoSubjects(List<Student> students) {
        return students.stream().map(this::toDTONoSubjects).collect(Collectors.toList());
    }

    private List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }
}