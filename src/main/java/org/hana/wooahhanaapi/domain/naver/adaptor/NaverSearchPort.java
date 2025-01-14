package org.hana.wooahhanaapi.domain.naver.adaptor;

import org.hana.wooahhanaapi.domain.naver.adaptor.dto.SearchResponseDto;

import java.util.List;

public interface NaverSearchPort {

    SearchResponseDto getSearchResult(String query);
    List<SearchResponseDto> getSearchResultList(List<String> queries);
}
