package com.demo.microservice.bootstorage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Arpit Khandelwal.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationDto {
    private  String id;
    private  String num1;
    private  String num2;
    private  String op;
    private  String result;

    @Override
    public String toString() {
        return num1.concat(" ")
                .concat(op)
                .concat(" ")
                .concat(num2)
                .concat(" = ")
                .concat(result);
    }
}
