package pl.mobilevulcanization.request;

import lombok.Data;

@Data
public class AddClientRequest {
    private String clientType; // "person" or "company"
    private String appointmentDate;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String serviceCategory;
    private String problemDescription;
    private String nip;
}
