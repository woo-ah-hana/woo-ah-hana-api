package org.hana.wooahhanaapi.naver.port;

import org.hana.wooahhanaapi.naver.dto.SearchResponseDto;

import java.util.List;

public interface NaverSearchPort {

    SearchResponseDto getSearchResult(String query);
    List<SearchResponseDto> getSearchResultList(List<String> queries);
}
