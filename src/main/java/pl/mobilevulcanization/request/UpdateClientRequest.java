package pl.mobilevulcanization.request;

import lombok.Builder;

@Builder
public record UpdateClientRequest(
        String name,
        String email,
        String phone,
        String address,
        String problemDescription,
        String nip
) {
}
