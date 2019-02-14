package com.demo.microservice.bootstorage.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * Created by Arpit Khandelwal.
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOperationRequest {
    //@NotNull(message = "{constraints.NotEmpty.message}")
    //private  String id;

    @NotNull(message = "{constraints.NotEmpty.message}")
    private  String num1;

    @NotNull(message = "{constraints.NotEmpty.message}")
    private  String num2;

    @NotNull(message = "{constraints.NotEmpty.message}")
    private  String op;

    @NotNull(message = "{constraints.NotEmpty.message}")
    private  String result;
}
