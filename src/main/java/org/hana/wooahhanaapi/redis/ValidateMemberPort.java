package org.hana.wooahhanaapi.redis;

import org.hana.wooahhanaapi.redis.dto.MemberValidationConfirmDto;

public interface ValidateMemberPort {
    boolean validateMember(MemberValidationConfirmDto dto);
}
