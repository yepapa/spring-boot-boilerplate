package com.yethree.springbootboilerplate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class Person {

    @NotEmpty
    @Size(max=64)
    private String name;

    @Min(10)
    private int age;
}
