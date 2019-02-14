package com.demo.microservice.bootstorage.service;

import com.demo.microservice.bootstorage.domain.model.Operation;
import com.demo.microservice.bootstorage.dto.OperationDto;

import java.util.List;

/**
 * Created by Arpit Khandelwal.
 */
public interface OperationService {
    Operation createOperation(OperationDto operationDto);

    List<Operation> getAllOperations();

    void deleteOperation(OperationDto operationDto);
}
