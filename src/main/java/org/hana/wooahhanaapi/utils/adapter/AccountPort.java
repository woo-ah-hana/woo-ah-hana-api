package org.hana.wooahhanaapi.utils.adapter;

import org.hana.wooahhanaapi.utils.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.utils.adapter.dto.AccountCreateRespDto;

public interface AccountPort {

    AccountCreateRespDto createNewAccount(AccountCreateReqDto accountCreateReqDto);



}
