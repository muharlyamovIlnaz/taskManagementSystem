package com.effective.mobile.tskmngmntsystm.models;

import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    private TaskSpecification() {
    }

    public static Specification<TaskEntity> authorIdIs(Long id) {
        return ((root, query, builder) -> builder.equal(root.get("authorId"), id));
    }

    public static Specification<TaskEntity> performerIdIs(Long id) {
        return ((root, query, builder) -> builder.equal(root.get("performerId"), id));
    }
}
