package org.hana.wooahhanaapi.domain.account.port;

import org.hana.wooahhanaapi.domain.account.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.dto.AccountCreateRespDto;

public interface AccountCreatePort {

    AccountCreateRespDto createNewAccount(AccountCreateReqDto accountCreateReqDto);

}
