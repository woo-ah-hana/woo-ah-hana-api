package org.hana.wooahhanaapi.domain.activeplan.adaptor;

import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateRespDto;
import org.hana.wooahhanaapi.domain.activeplan.adaptor.dto.SearchResponseDto;

import java.util.List;

public interface NaverSearchPort {

    SearchResponseDto getSearchResult(String query);
    List<SearchResponseDto> getSearchResultList(List<String> queries);
}
