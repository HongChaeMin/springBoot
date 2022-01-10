package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Long mno;

    private String title;

    // @Builder.Default
    // 안하면 초기값 설정 X
    // 원하는 값을 대입하고 어노테이션 붙어주면 초기값 설정됨
    private List<MovieImageDTO> imageDTOList; // imageDTOList = [];
    // list 생성시 기본 인덱스 10개 생성

}
