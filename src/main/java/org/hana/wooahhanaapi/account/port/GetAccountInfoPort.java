package org.hana.wooahhanaapi.account.port;

import org.hana.wooahhanaapi.account.dto.GetAccountInfoReqDto;
import org.hana.wooahhanaapi.account.dto.GetAccountInfoRespDto;

public interface GetAccountInfoPort {
    GetAccountInfoRespDto getAccountInfo(GetAccountInfoReqDto getAccountInfoReqDto);
}
