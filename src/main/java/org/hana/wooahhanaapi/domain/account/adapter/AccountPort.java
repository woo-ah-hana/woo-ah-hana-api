package org.hana.wooahhanaapi.domain.account.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateRespDto;

public interface AccountPort {

    AccountCreateRespDto createNewAccount(AccountCreateReqDto accountCreateReqDto);

}
