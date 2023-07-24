package org.judeukkim.springbootKDJ.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.judeukkim.springbootKDJ.entity.Board;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileDto {

    public Long id;  //파일 번호

    private String oldFileName;// 원래이름

    private String newFileName;// DB 저장이름

    private Board boardEntity;

    private LocalDateTime createTime; // 파일 생성 시간


}
