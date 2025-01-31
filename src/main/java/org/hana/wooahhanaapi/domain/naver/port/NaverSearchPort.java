package org.hana.wooahhanaapi.domain.naver.port;

import org.hana.wooahhanaapi.domain.naver.dto.SearchResponseDto;

import java.util.List;

public interface NaverSearchPort {

    SearchResponseDto getSearchResult(String query);
    List<SearchResponseDto> getSearchResultList(List<String> queries);
}
