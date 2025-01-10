package org.hana.wooahhanaapi.domain.account.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRespDto;

public interface AccountTransferPort {

    AccountTransferRespDto createAccountTransfer(AccountTransferReqDto reqDto);
}
