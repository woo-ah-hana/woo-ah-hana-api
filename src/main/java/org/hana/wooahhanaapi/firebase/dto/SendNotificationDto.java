package org.hana.wooahhanaapi.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendNotificationDto {
    String token;
    String title;
    String body;
}
