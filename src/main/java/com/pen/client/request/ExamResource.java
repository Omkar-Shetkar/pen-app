package com.pen.client.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamResource {
    private String name;
    private Date start;
    private Date end;
    private String language;
//    private String address;
}
