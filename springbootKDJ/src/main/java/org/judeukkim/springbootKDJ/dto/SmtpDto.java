package org.judeukkim.springbootKDJ.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmtpDto<T> {
    private int status; //js status
    private T data;  //매개변수 선언
}
