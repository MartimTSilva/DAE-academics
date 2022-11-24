package pt.ipleiria.estg.dei.ei.dae.academics.dto;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.User;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentDTO implements Serializable {
    @NotBlank
    private long id;

    @NotBlank
    private String filename;

    public DocumentDTO() {
    }

    public DocumentDTO(long id, String filename) {
        this.id = id;
        this.filename = filename;
    }

    public static DocumentDTO from(Document document) {
        return new DocumentDTO(
                document.getId(),
                document.getFilename()
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public static List<DocumentDTO> from(List<Document> documents) {
        return documents.stream().map(DocumentDTO::from).collect(Collectors.toList());
    }
}