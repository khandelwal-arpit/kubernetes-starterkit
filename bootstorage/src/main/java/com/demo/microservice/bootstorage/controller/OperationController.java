package com.demo.microservice.bootstorage.controller;


import com.demo.microservice.bootstorage.domain.model.Operation;
import com.demo.microservice.bootstorage.dto.OperationDto;
import com.demo.microservice.bootstorage.request.CreateOperationRequest;
import com.demo.microservice.bootstorage.service.OperationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Arpit Khandelwal.
 */
@RestController
@RequestMapping("/api/bootstorage")
public class OperationController {

    @Autowired
    OperationService operationService;

    @Autowired
    ModelMapper mapper;

    @CrossOrigin
    @GetMapping(value = "/")
    public List<OperationDto> getOperations(){
        List<Operation> allOperations = operationService.getAllOperations();
        if (allOperations != null && !allOperations.isEmpty()) {
            return allOperations
                    .stream()
                    .map(account -> mapper.map(account, OperationDto.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @CrossOrigin
    @GetMapping(value = "/operations")
    public List<String> getOperationsInStringForm(){
        List<Operation> allOperations = operationService.getAllOperations();
        if (allOperations != null && !allOperations.isEmpty()) {
            return allOperations
                    .stream()
                    .sorted(Comparator.comparing(Operation::getTimestamp).reversed())
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @PostMapping(value = "/create")
    public OperationDto createOperation(@RequestBody @Valid CreateOperationRequest createOperationRequest){
        OperationDto operationDto = mapper.map(createOperationRequest, OperationDto.class);
        Operation operation = operationService.createOperation(operationDto);
        if (operation != null) {
            return mapper.map(operation, OperationDto.class);
        }
        return null;
    }

    @GetMapping(value = "/deletelru")
    public ResponseEntity deleteOperation(){
        List<Operation> allOperations = operationService.getAllOperations();
        if (allOperations != null && !allOperations.isEmpty()) {
            List<Operation> sortedOperations = allOperations
                        .stream()
                        .sorted(Comparator.comparing(Operation::getTimestamp))
                        .collect(Collectors.toList());
                operationService.deleteOperation(mapper.map(sortedOperations.get(0), OperationDto.class)); //deletes the first operation
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/healthz")
    public ResponseEntity healthz(){
        return new ResponseEntity(HttpStatus.OK);
    }
}
