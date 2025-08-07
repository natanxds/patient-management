package com.natanxds.patientservice.dto;

import java.time.LocalDate;

public record PatientResponseDTO(
        String id,
        String name,
        String email,
        String address,
        String dateOfBirth
) {

}
