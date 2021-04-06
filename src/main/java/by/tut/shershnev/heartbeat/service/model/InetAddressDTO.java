package by.tut.shershnev.heartbeat.service.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class InetAddressDTO {

    @NotEmpty
    @Pattern(regexp = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b", message = "This ip address is invalid")
    private String ipAddress;
    private String status;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InetAddressDTO that = (InetAddressDTO) o;
        return Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, status);
    }
}
