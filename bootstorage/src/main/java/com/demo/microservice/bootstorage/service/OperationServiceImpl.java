package com.demo.microservice.bootstorage.service;

import com.demo.microservice.bootstorage.domain.model.Operation;
import com.demo.microservice.bootstorage.domain.repository.OperationRepository;
import com.demo.microservice.bootstorage.dto.OperationDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.time.Instant;


/**
 * Created by Arpit Khandelwal.
 */
@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    OperationRepository operationRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public Operation createOperation(OperationDto operationDto) {
        Operation op = mapper.map(operationDto, Operation.class);
        op.setTimestamp(Instant.now().toEpochMilli());
        return operationRepository.save(op);
    }

    @Override
    public List<Operation> getAllOperations() {
        Iterator<Operation> iteratorToCollection = operationRepository.findAll().iterator();
        return StreamSupport.stream(
                Spliterators
                        .spliteratorUnknownSize(iteratorToCollection, Spliterator.ORDERED), false)
                .collect(Collectors.toList()
                );
    }

    @Override
    public void deleteOperation(OperationDto operationDto) {
        Operation op = mapper.map(operationDto, Operation.class);
        operationRepository.delete(op);
    }
}
