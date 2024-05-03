package com.Spring.application.audit;

import com.Spring.application.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {
    @CreatedBy
    @Column(name = "created_by")
    @JsonView(Views.InternalView.class)
    private U createdBy;

    @CreatedDate
    @Column(name = "created_date")
    @JsonView(Views.InternalView.class)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    @JsonView(Views.InternalView.class)
    private U lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonView(Views.InternalView.class)
    private LocalDateTime lastModifiedDate;
}
