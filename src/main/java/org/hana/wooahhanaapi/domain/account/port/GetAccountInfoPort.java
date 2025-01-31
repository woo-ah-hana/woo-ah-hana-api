package org.hana.wooahhanaapi.domain.account.port;

import org.hana.wooahhanaapi.domain.account.dto.GetAccountInfoReqDto;
import org.hana.wooahhanaapi.domain.account.dto.GetAccountInfoRespDto;

public interface GetAccountInfoPort {
    GetAccountInfoRespDto getAccountInfo(GetAccountInfoReqDto getAccountInfoReqDto);
}
