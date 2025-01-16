package org.hana.wooahhanaapi.domain.account.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRespDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.SimplifiedTransferReqDto;

public interface AccountTransferPort {

    AccountTransferRespDto createAccountTransfer(SimplifiedTransferReqDto reqDto);
}
