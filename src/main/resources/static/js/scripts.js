window.addEventListener('DOMContentLoaded', event => {
    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

    // Simple-DataTables initialization
    const datatablesSimple = document.getElementById('datatablesSimple');
    if (datatablesSimple) {
        new simpleDatatables.DataTable(datatablesSimple);
    }
});

//make-appointment file

function toggleFormFields() {
    var clientType = document.querySelector('input[name="clientType"]:checked').value;

    document.getElementById("submitButton").removeAttribute('disabled');

    // Show/hide the appropriate fields
    var personFields = document.getElementById("personFields");
    var companyFields = document.getElementById("companyFields");

    if (clientType === "person") {
        personFields.style.display = "block";
        companyFields.style.display = "none";

        // Add 'required' to all individual fields
        personFields.querySelectorAll("input, textarea, select").forEach(field => {
            field.setAttribute("required", "required");
        });

        // Remove 'required' from all company fields
        companyFields.querySelectorAll("input, textarea, select").forEach(field => {
            field.removeAttribute("required");
        });

    } else if (clientType === "company") {
        personFields.style.display = "none";
        companyFields.style.display = "block";

        // Add 'required' to all company fields
        companyFields.querySelectorAll("input, textarea").forEach(field => {
            field.setAttribute("required", "required");
        });

        // Remove 'required' from all individual fields
        personFields.querySelectorAll("input, textarea, select").forEach(field => {
            field.removeAttribute("required");
        });
    }
}

async function fetchAvailableTimes() {
    const localDate = document.getElementById("appointmentDate").value;

    if (localDate) {
        const url = `http://localhost:8080/allDates/free/from/${localDate}`;

        try {
            const response = await fetch(url);

            if (response.ok) {
                const datesList = await response.json();
                populateAvailableTimes(datesList);
            } else {
                console.error("Błąd podczas pobierania dostępnych terminów:", response.statusText);
            }
        } catch (error) {
            console.error("Wystąpił błąd:", error);
        }
    }
}

function populateAvailableTimes(datesList) {
    const availableTimesSelect = document.getElementById("availableTimes");
    availableTimesSelect.innerHTML = '<option value="" selected disabled>Wybierz wolny termin</option>'; // Reset options

    if (datesList.length > 0) {
        datesList.forEach(item => {
            const option = document.createElement("option");
            const dateObj = new Date(item.date);

            //Formatting date to yyyy-MM-dd HH:mm in local time zone
            const formattedDate = dateObj.toLocaleString('pl-PL', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                hour12: false,
            }).replace(",", ""); // Remove comma

            option.value = dateObj.toISOString(); // Value as ISO for data transfer
            option.textContent = formattedDate;
            availableTimesSelect.appendChild(option);
        });
    } else {
        availableTimesSelect.innerHTML += '<option value="" disabled>Brak dostępnych terminów</option>';
    }
}

//dates file

function populateDateTable(datesList) {
    const tableBody = document.querySelector("#datatablesDate tbody");
    tableBody.innerHTML = ''; // Clearing the table content

    datesList.forEach(item => {
        // Create a new row
        const row = document.createElement('tr');

        // Date in the format yyyy-MM-dd HH:mm:ss
        const date = new Date(item.date).toLocaleString('pl-PL', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });

        const isFree = item.free ? 'tak' : 'nie';

        // Create table cells
        const dateCell = document.createElement('td');
        dateCell.textContent = date;

        const isFreeCell = document.createElement('td');
        isFreeCell.textContent = isFree;

        const actionCell = document.createElement('td');
        const editButton = document.createElement('button');
        editButton.textContent = 'Edytuj';
        editButton.classList.add('btn-edit', 'btn', 'btn-primary');
        editButton.addEventListener('click', () => editDate(item.date, item.free)); // Dodajemy event handler do przycisku

        actionCell.appendChild(editButton);

        // Add cells to a row
        row.appendChild(dateCell);
        row.appendChild(isFreeCell);
        row.appendChild(actionCell);

        // Add a row to the table
        tableBody.appendChild(row);
    });
}

function editDate(date, isFree) {
    // Convert date to Date object
    const utcDate = new Date(date);

    // Manually convert to Poland's local time zone
    const offset = utcDate.getTimezoneOffset() * 60000; // Time zone offset in milliseconds
    const localDate = new Date(utcDate.getTime() - offset);

    // Format date to yyyy-MM-ddTHH:mm
    document.getElementById("originalDate").value = localDate.toISOString().slice(0, 16);
    document.getElementById("editIsFree").checked = isFree;

    // Show modal
    const editDateModal = new bootstrap.Modal(document.getElementById("editDateModal"), {});
    editDateModal.show();
}

function saveEditedDate() {
    const isFree = document.getElementById("editIsFree").checked;
    const originalDate = document.getElementById("originalDate").value;

    const _csrf = $("meta[name='_csrf']").attr("content");
    const _csrf_header = $("meta[name='_csrf_header']").attr("content");

    fetch(`/date/${encodeURIComponent(originalDate)}/update`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            [_csrf_header]: _csrf
        },
        body: JSON.stringify(isFree)
    })
        .then(response => {

            if (response.ok) {
                alert("Data została pomyślnie zaktualizowana!");
                location.reload(); // Reload the page to update the table view
            } else {
                return response.text().then(text => {
                    alert("Wystąpił błąd podczas aktualizacji daty.");
                });
            }
        })
        .catch(error => console.error('Błąd podczas fetch:', error)); // See errors if no response
}

function deleteDate() {
    const originalDate = document.getElementById("originalDate").value;

    const _csrf = $("meta[name='_csrf']").attr("content");
    const _csrf_header = $("meta[name='_csrf_header']").attr("content");

    fetch(`/date/${encodeURIComponent(originalDate)}/delete`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [_csrf_header]: _csrf
        }
    })
        .then(response => {
            if (response.ok) {
                alert("Data została pomyślnie usunięta!");
                location.reload(); // Przeładuj stronę, aby zaktualizować widok tabeli
            } else {
                alert("Wystąpił błąd podczas usuwania daty.");
            }
        })
        .catch(error => console.error('Błąd:', error));
}

//appointments file

function populateAppointmentTable(clientsList) {
    const tableBody = document.querySelector("#datatablesClient tbody");
    tableBody.innerHTML = ''; // Clearing the table content

    clientsList.forEach(item =>{
        // Create a new row
        const row = document.createElement('tr');

        // Create table cells
        const nameCell = document.createElement('td');
        nameCell.textContent = item.name || '-';

        const addressCell = document.createElement('td');
        addressCell.textContent = item.address || '-';

        const phoneCell = document.createElement('td');
        phoneCell.textContent = item.phone || '-';

        const emailCell = document.createElement('td');
        emailCell.textContent = item.email || '-';

        const categoryCell = document.createElement('td');
        categoryCell.textContent = item.category || '-';

        const descriptionCell = document.createElement('td');
        descriptionCell.textContent = item.description || '-';

        const dateCell = document.createElement('td');

        if (item.dateOfAppointment) {
            // Date in the format yyyy-MM-dd HH:mm:ss
            dateCell.textContent = new Date(item.dateOfAppointment).toLocaleString('pl-PL', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
        } else {
            dateCell.textContent = '-'; // Set value to '-' if no date is provided
        }

        const clientTypeCell = document.createElement('td');
        clientTypeCell.textContent = item.clientType === 'person' ? 'Osoba prywatna' : 'Firma';

        const nipCell = document.createElement('td');
        nipCell.textContent = item.nip || '-';

        const actionCell = document.createElement('td');

        const editButton = document.createElement('button');
        editButton.textContent = 'Edytuj';
        editButton.classList.add('btn-edit', 'btn', 'btn-primary');
        editButton.addEventListener('click', () => editClient(item)); // Dodajemy event handler do przycisku

        actionCell.appendChild(editButton);

        // Add cells to a row
        row.appendChild(nameCell);
        row.appendChild(addressCell);
        row.appendChild(phoneCell);
        row.appendChild(emailCell);
        row.appendChild(categoryCell);
        row.appendChild(descriptionCell);
        row.appendChild(dateCell);
        row.appendChild(clientTypeCell);
        row.appendChild(nipCell);
        row.appendChild(actionCell);

        // Add a row to the table
        tableBody.appendChild(row);
    })
}

function editClient(item) {
    const idField = document.getElementById("editId");
    const nameField = document.getElementById("editName");
    const addressField = document.getElementById("editAddress");
    const phoneField = document.getElementById("editPhone");
    const emailField = document.getElementById("editEmail");
    const categoryField = document.getElementById("editCategory");
    const descriptionField = document.getElementById("editDescription");
    const dateField = document.getElementById("editDate");
    const clientTypeField = document.getElementById("editClientType");
    const nipField = document.getElementById("editNIP");

    if(item.clientType === "person") {
        nipField.disabled = true;
        clientTypeField.value = "Klient fizyczny";
        // Convert date to Date object
        const utcDate = new Date(item.dateOfAppointment);

        // Manually convert to Poland's local time zone
        const offset = utcDate.getTimezoneOffset() * 60000; // Time zone offset in milliseconds
        const localDate = new Date(utcDate.getTime() - offset);

        // Format date to yyyy-MM-ddTHH:mm
        dateField.value = localDate.toISOString().slice(0, 16);
    } else if(item.clientType === "company"){
        nipField.disabled = false;
        dateField.value = "";
        clientTypeField.value = "Klient firmowy";
    }

    idField.value = item.id;
    nameField.value = item.name;
    addressField.value = item.address;
    phoneField.value = item.phone;
    emailField.value = item.email;
    categoryField.value = item.category;
    descriptionField.value = item.description;
    nipField.value = item.nip;

    const editClientModal = new bootstrap.Modal(document.getElementById("editClientModal"), {});
    editClientModal.show();
}

function saveEditedClient() {
    // Check the values taken from the form
    const id = document.getElementById("editId").value;
    const name = document.getElementById("editName").value;
    const address = document.getElementById("editAddress").value;
    const phone = document.getElementById("editPhone").value;
    const email = document.getElementById("editEmail").value;
    const description = document.getElementById("editDescription").value
    const nip = document.getElementById("editNIP").value;

    // Creating a client object to send
    const clientData = {
        name: name,
        address: address,
        phone: phone,
        email: email,
        problemDescription: description,
        nip: nip
    };

    const _csrf = $("meta[name='_csrf']").attr("content");
    const _csrf_header = $("meta[name='_csrf_header']").attr("content");

    fetch(`/client/${id}/update`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            [_csrf_header]: _csrf
        },
        body: JSON.stringify(clientData)
    })
        .then(response => {
            if (response.ok) {
                alert("Klient został pomyślnie zaktualizowany!");
                location.reload();// Reload the page to update the table view
            } else {
                return response.text().then(text => {
                    alert("Wystąpił błąd podczas aktualizacji klienta.");
                });
            }
        })
        .catch(error => console.error('Błąd podczas fetch:', error)); // See errors if no response
}

function deleteClient() {
    const id = document.getElementById("editId").value;

    const _csrf = $("meta[name='_csrf']").attr("content");
    const _csrf_header = $("meta[name='_csrf_header']").attr("content");

    fetch(`/client/${id}/delete`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [_csrf_header]: _csrf
        }
    })
        .then(response => {
            if (response.ok) {
                alert("Klient został pomyślnie usunięty!");
                location.reload(); // Reload the page to update the table view
            } else {
                alert("Wystąpił błąd podczas usuwania klienta.");
                console.error('Błąd:', response.statusText);
            }
        })
        .catch(error => console.error('Błąd:', error));
}

