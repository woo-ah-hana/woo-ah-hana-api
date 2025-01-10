package org.hana.wooahhanaapi.domain.account.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.dto.GetAccountInfoReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.GetAccountInfoRespDto;

public interface GetAccountInfoPort {
    GetAccountInfoRespDto getBalance(GetAccountInfoReqDto getAccountInfoReqDto);
}
