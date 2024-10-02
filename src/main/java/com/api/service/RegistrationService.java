package com.api.service;

import com.api.entity.Registration;
import com.api.exception.ResourseNotFoundException;
import com.api.payload.RegistrationDto;
import com.api.repository.RegistrationRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter

@Service
public class RegistrationService {

    private  RegistrationRepository registrationRepository;
    private ModelMapper modelMapper;

    //Constructor Injection ...
    // Passing RegistrationRepository dependency into the RegistrationService as a argument
    public RegistrationService(RegistrationRepository registrationRepository,ModelMapper modelMapper) {
        this.registrationRepository = registrationRepository;
        this.modelMapper =  modelMapper;
    }

    //This method returns a list of all Registration entities from the database.
    //This line calls the findAll() method from the RegistrationRepository, which retrieves all records from
    // the Registration table. The result is stored in a List of Registration objects.
    // This returns the list of registrations to the caller.
    public List<RegistrationDto> getRegistrations(){
        List<Registration> registrations =  registrationRepository.findAll();
        List<RegistrationDto> dtos = registrations.stream().map(r -> mapToDto(r)).collect(Collectors.toList());
        return dtos;
    }

    //This method takes a Registration object as a parameter and returns the saved Registration object.
    //Registration savedEntity = registrationRepository.save(registration);: This line calls the save() method
    // on the registrationRepository to persist the registration object to the database.
    public RegistrationDto createRegistration(RegistrationDto registrationDto) {
        //copy dto to entity
        Registration registration = mapToEntity(registrationDto);

        Registration savedEntity = registrationRepository.save(registration); //saving to database "savedEntity"

        //copy entity to dto
        RegistrationDto dto = mapToDto(savedEntity);
        return dto;
    }

    public void deleteRegistration(long id) {

        registrationRepository.deleteById(id);

    }

    public Registration updateRegistration(long id, Registration registration) {

        Registration r = registrationRepository.findById(id).get();
       /* r.setName(registration.getName());
        r.setEmail(registration.getEmail());
        r.setMobile(registration.getMobile());*/
        Registration savedEntity = registrationRepository.save(r);
        return savedEntity;
    }

    Registration mapToEntity(RegistrationDto registrationDto){
        Registration registration = modelMapper.map(registrationDto, Registration.class);
//        Registration registration = new Registration();
//        registration.setName(registrationDto.getName());
//        registration.setEmail(registrationDto.getEmail());
//        registration.setMobile(registrationDto.getMobile());
        return registration;
    }

    RegistrationDto mapToDto(Registration registration){
        RegistrationDto dto = modelMapper.map(registration, RegistrationDto.class);
//        RegistrationDto dto = new RegistrationDto();  //converting to dto from RegistrationDto
//        dto.setName(registration.getName());
//        dto.setEmail(registration.getEmail());
//        dto.setMobile(registration.getMobile());
        return dto;
    }


    public RegistrationDto getRegistrationById(long id) {
        Registration registration = registrationRepository.findById(id).orElseThrow(
                ()->new ResourseNotFoundException("Record not found") //supplier functional interface to give only output
        );
        return mapToDto(registration);
    }
}
