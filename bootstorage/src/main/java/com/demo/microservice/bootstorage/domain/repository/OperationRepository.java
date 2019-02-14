package com.demo.microservice.bootstorage.domain.repository;

import com.demo.microservice.bootstorage.domain.model.Operation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Arpit Khandelwal.
 */
public interface OperationRepository extends CrudRepository<Operation, Long> {
}
