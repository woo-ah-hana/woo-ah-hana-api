package org.hana.wooahhanaapi.utils.redis;

import org.hana.wooahhanaapi.utils.redis.dto.MemberValidationConfirmDto;

public interface ValidateMemberPort {
    boolean validateMember(MemberValidationConfirmDto dto);
}
