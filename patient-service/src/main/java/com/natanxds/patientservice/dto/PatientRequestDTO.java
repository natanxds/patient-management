package com.natanxds.patientservice.dto;

import com.natanxds.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientRequestDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be va             lid")
    private String email;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "Date of birth is mandatory")
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class,
            message = "Registered date is mandatory")
    private String registeredDate;
}
