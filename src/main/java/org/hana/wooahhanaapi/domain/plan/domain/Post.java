package org.hana.wooahhanaapi.domain.plan.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.exception.FileSizeExceededException;
import org.hana.wooahhanaapi.domain.plan.exception.InvalidFileTypeException;
import org.hana.wooahhanaapi.domain.plan.exception.InvalidPostDataException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private UUID id;

    private PlanEntity plan;

    private MemberEntity member;

    private String imageUrl;

    private String description;

    private LocalDateTime createdAt;

    public static Post create(UUID id, PlanEntity plan, MemberEntity member, String imageUrl, String description, LocalDateTime createdAt) {
        if (plan == null || member == null || description == null || imageUrl == null || createdAt == null) {
            throw new InvalidPostDataException("필수 데이터가 누락되었습니다.");
        }

        return new Post(id, plan, member, imageUrl, description, createdAt);
    }

}
