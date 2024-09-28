package pl.mobilevulcanization.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateClientRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String problemDescription;
    private String nip;
}
