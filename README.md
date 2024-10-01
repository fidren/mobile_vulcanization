# Mobilna Wulkanizacja

## Opis projektu
Mobilna Wulkanizacja to aplikacja umożliwiająca zarządzanie usługami wulkanizacyjnymi, takimi jak umawianie terminów serwisowych, aktualizacja danych klientów oraz oferowanie usług dla klientów indywidualnych i firmowych. Projekt ma na celu usprawnienie procesu zarządzania terminami oraz obsługę klientów poprzez interfejs RESTful API.

## Funkcjonalności z poziomu Admin panelu
- **Zarządzanie klientami** – możliwość dodawanie, aktualizowania, usuwania i fltrowania klientów indywidualnych i firmowych.
- **Zarządzanie terminami** – dodawanie, aktualizowanie i usuwanie terminów serwisowych.
- **Przeglądanie dostępnych terminów** – filtrowanie i przeglądanie dostępnych terminów serwisowych

## Funkcjonalności z poziomu Klienta
- **Umawianie Wizyt** – możliwość umuwienia wizyt zarówno przez klientów indywidualnych, jak i firmowych.
- **Przeglądanie dostępnych terminów** – filtrowanie i przeglądanie dostępnych terminów serwisowych przez klientów indywidualnych podczas umawiania wizyty.
- **Wybór kategorii oraz Opis problemu** – wybór kategorii usług oraz szczegółowe opisywanie problemów.

## Technologie
- **Backend**: Java, Spring Boot
- **Frontend**: Html, CSS, Bootstrap, JavaScript
- **Baza danych**: PostgreSQL
- **Testy**: JUnit 5, Mockito, Spring Boot Test
- **Build tool**: Maven
- **Kontrola wersji**: Git

## Wymagania
- Java 17 lub nowsza
- Maven 3.6+ 
- PostgreSQL

## Instalacja i uruchomienie projektu
1. Sklonuj repozytorium:
    ```
    git clone https://github.com/twoje-uzytkownik/github-projekt.git

    ```
2. Przejdź do katalogu projektu:
    ```
    cd mobilna-wulkanizacja

    ```
3. Uruchom projekt przy użyciu Mavena:
    ```
    mvn spring-boot:run

    ```
4. Aplikacja powinna być dostępna pod adresem http://localhost:8080.

## Konfiguracja bazy danych
Projekt korzysta z bazy danych PostgreSQL.

**Zaktualizuj plik application.properties lub application.yml z odpowiednimi ustawieniami bazy danych:**
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/vulcanization
  spring.datasource.username=twoj_uzytkownik
  spring.datasource.password=twoje_haslo
  spring.jpa.hibernate.ddl-auto=update
  ```

## Uruchomienie testów
**Aby uruchomić testy, użyj polecenia:**
 ```
    mvn test

 ```

## API Endpoints
| Metoda HTTP | Endpoint | Opis |
| --- | --- | --- |
| GET |	/allClients | Pobierz wszystkich klientów |
| GET |	/filteredClients | Pobierz wyfiltrowanych klientów |
| POST | /addClient |	Dodaj nowego klienta |
| PUT	| /client/{clientId}/update |	Aktualizuj informacje o kliencie |
| DELETE | /client/{clientId}/delete |	Usuń klienta |
| GET |	/allDates | Pobierz wszystkie terminy |
| GET |	/filteredDates | Pobierz wyfiltrowane terminy |
| GET |	/allDates/free/current/{localDate} | Pobierz wolne terminy według daty |
| POST | /addDate | Dodaj nowy termin |
| PUT | /date/{localDate}/update | Aktualizuj status terminu |
| DELETE | /date/{localDate}/delete | Usuń termin |

## Autor
**Wojciech Mikula** - główny twórca projektu
