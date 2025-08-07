package com.natanxds.patientservice.service.Impl;

import com.natanxds.patientservice.dto.PatientRequestDTO;
import com.natanxds.patientservice.dto.PatientResponseDTO;
import com.natanxds.patientservice.exception.EmailAlreadyExistsException;
import com.natanxds.patientservice.exception.PatientNotFoundException;
import com.natanxds.patientservice.grpc.BillingServiceGrpcClient;
import com.natanxds.patientservice.kafka.KafkaProducer;
import com.natanxds.patientservice.mapper.PatientMapper;
import com.natanxds.patientservice.model.Patient;
import com.natanxds.patientservice.repository.PatientRepository;
import com.natanxds.patientservice.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    private final KafkaProducer kafkaProducer;

    public PatientServiceImpl(PatientRepository patientRepository,
                          BillingServiceGrpcClient billingServiceGrpcClient,
                          KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email address already exists: "
                    + patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),
                patient.getName(), patient.getEmail());

        kafkaProducer.sendEvent(patient);
        log.info("AAAAAAAAAAAAAAAPatient created: {}", patient);

        return PatientMapper.toPatientResponseDTO(patient);
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with id: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email address already exists: "
                    + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toPatientResponseDTO(updatedPatient);
    }

    @Override
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
