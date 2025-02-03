package org.hana.wooahhanaapi.account.port;

import org.hana.wooahhanaapi.account.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.account.dto.AccountCreateRespDto;

public interface AccountCreatePort {

    AccountCreateRespDto createNewAccount(AccountCreateReqDto accountCreateReqDto);

}
