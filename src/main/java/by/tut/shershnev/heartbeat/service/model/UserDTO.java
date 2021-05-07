package by.tut.shershnev.heartbeat.service.model;

public class UserDTO {

    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmingPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmingPassword() {
        return confirmingPassword;
    }

    public void setConfirmingPassword(String confirmingPassword) {
        this.confirmingPassword = confirmingPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmingPassword='" + confirmingPassword + '\'' +
                '}';
    }
}
