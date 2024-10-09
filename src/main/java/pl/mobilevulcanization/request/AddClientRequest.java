package pl.mobilevulcanization.request;

import lombok.Builder;

@Builder
public record AddClientRequest(
    String clientType, // "person" or "company"
    String appointmentDate,
    String name,
    String email,
    String phone,
    String address,
    String serviceCategory,
    String problemDescription,
    String nip
) {
}
