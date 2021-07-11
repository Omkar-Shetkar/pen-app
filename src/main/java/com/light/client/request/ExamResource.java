package com.light.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResource {
    private String name;
    private Date start;
    private Date end;
    private String language;
//    private String address;
}
