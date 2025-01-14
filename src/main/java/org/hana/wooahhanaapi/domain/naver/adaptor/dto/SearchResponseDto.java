package org.hana.wooahhanaapi.domain.naver.adaptor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponseDto {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<SearchResultItemDto> items;
}
