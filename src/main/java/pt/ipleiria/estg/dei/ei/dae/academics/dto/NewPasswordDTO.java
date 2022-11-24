package pt.ipleiria.estg.dei.ei.dae.academics.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class NewPasswordDTO implements Serializable {
    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    public NewPasswordDTO() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}