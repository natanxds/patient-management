package com.natanxds.patientservice.service;

import com.natanxds.patientservice.dto.PatientRequestDTO;
import com.natanxds.patientservice.dto.PatientResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<PatientResponseDTO> getPatients();

    PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO);

    PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO);

    void deletePatient(UUID id);
}
