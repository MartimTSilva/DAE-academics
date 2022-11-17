package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dto.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.TeacherBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static pt.ipleiria.estg.dei.ei.dae.academics.Codes.*;

@Path("teachers") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class TeacherService {
    @EJB
    private TeacherBean teacherBean;

    @EJB
    private SubjectBean subjectBean;

    @GET
    public List<TeacherDTO> getAllTeachersWS() {
        return teachersToDTOs(teacherBean.getAllTeachers());
    }

    // Converts an entity Student to a DTO Student class
    private TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getUsername(),
                teacher.getPassword(),
                teacher.getName(),
                teacher.getEmail(),
                teacher.getOffice()
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
    public Response createNewTeacher(TeacherDTO teacherDTO) {
        teacherBean.create(
                teacherDTO.getUsername(),
                teacherDTO.getPassword(),
                teacherDTO.getName(),
                teacherDTO.getEmail(),
                teacherDTO.getOffice()
        );

        Teacher newTeacher = teacherBean.findTeacher(teacherDTO.getUsername());
        if (newTeacher == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(toDTO(newTeacher)).build();
    }

    @GET
    @Path("{username}")
    public Response getTeacherDetails(@PathParam("username") String username) {
        Teacher teacher = teacherBean.findTeacher(username);
        if (teacher != null) {
            return Response.ok(toDTO(teacher)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @GET
    @Path("{username}/subjects")
    public Response getTeacherSubjects(@PathParam("username") String username) {
        Teacher teacher = teacherBean.findTeacher(username);
        if (teacher != null) {
            List<SubjectDTO> dtos = subjectsToDTOs(teacherBean.getTeacherSubjects(teacher));
            return Response.ok(dtos).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @POST
    @Path("{username}/enroll-subject/{code}")
    public Response addTeacherSubject(@PathParam("username") String username, @PathParam("code") long code) {
        int res = teacherBean.associateTeacherInSubject(username, code);

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
    public Response removeTeacherSubject(@PathParam("username") String username, @PathParam("code") long code) {
        int res = teacherBean.dissociateTeacherInSubject(username, code);

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
    private List<TeacherDTO> teachersToDTOs(List<Teacher> teachers) {
        return teachers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }
}