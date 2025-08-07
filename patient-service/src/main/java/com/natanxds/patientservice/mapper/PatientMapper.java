package com.natanxds.patientservice.mapper;

import com.natanxds.patientservice.dto.PatientRequestDTO;
import com.natanxds.patientservice.dto.PatientResponseDTO;
import com.natanxds.patientservice.model.Patient;

import java.time.LocalDate;
import java.util.UUID;

public class PatientMapper {

    public static PatientResponseDTO toPatientResponseDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId().toString(),
                patient.getName(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getDateOfBirth().toString()
        );
    }

    public static Patient toEntity(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return patient;
    }

}
