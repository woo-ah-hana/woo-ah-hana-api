package org.hana.wooahhanaapi.domain.activeplan.adaptor;

import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateRespDto;

public interface NaverSearchPort {

    String getSearchResult(String query);
}
