package pl.mobilevulcanization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mobilevulcanization.model.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c WHERE c.clientType = 'company'")
    List<Client> findAllCompanyClients();

    @Query("SELECT c FROM Client c WHERE " +
            "(COALESCE(:clientType, c.clientType) = c.clientType) AND " +
            "(c.clientType = 'company' OR (COALESCE(:localDate, FUNCTION('DATE', c.dateOfAppointment)) = FUNCTION('DATE', c.dateOfAppointment))) AND" +
            "(c.clientType = 'company' OR :isCurrent = false OR FUNCTION('DATE', c.dateOfAppointment) >= CURRENT_DATE)")
    List<Client> findFilteredClients(String clientType, LocalDate localDate, Boolean isCurrent);

    @Query("SELECT c FROM Client c WHERE " +
            "(COALESCE(:clientType, c.clientType) = c.clientType) AND " +
            "(c.clientType = 'company' OR :isCurrent = false OR FUNCTION('DATE', c.dateOfAppointment) >= CURRENT_DATE)")
    List<Client> findFilteredClients(String clientType, Boolean isCurrent);

}