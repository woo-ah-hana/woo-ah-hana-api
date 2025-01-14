package org.hana.wooahhanaapi.domain.activeplan.adaptor;

import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateRespDto;
import org.hana.wooahhanaapi.domain.activeplan.adaptor.dto.SearchResponseDto;

public interface NaverSearchPort {

    SearchResponseDto getSearchResult(String query);
}
