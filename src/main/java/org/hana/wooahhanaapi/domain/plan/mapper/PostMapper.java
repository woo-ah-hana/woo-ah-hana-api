package org.hana.wooahhanaapi.domain.plan.mapper;

import org.hana.wooahhanaapi.domain.plan.domain.Post;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;

public class PostMapper {
    public static PostEntity mapDomainToEntity(Post post) {
        return PostEntity.builder()
                .id(post.getId())
                .plan(post.getPlan())
                .member(post.getMember())
                .imageUrl(post.getImageUrl())
                .description(post.getDescription())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static Post mapEntityToDomain(PostEntity entity) {
        return Post.create(
                entity.getId(),
                entity.getPlan(),
                entity.getMember(),
                entity.getImageUrl(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }
}
