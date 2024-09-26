package pl.mobilevulcanization.request;

import lombok.Data;

@Data
public class UpdateClientRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String problemDescription;
    private String nip;
}
