package com.effective.mobile.tskmngmntsystm.repository;

import com.effective.mobile.tskmngmntsystm.enums.Status;
import com.effective.mobile.tskmngmntsystm.models.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    @Modifying
    @Query("UPDATE TaskEntity t SET t.status = :newStatus WHERE t.id = :id")
    int updateTaskStatus(@Param("newStatus") Status newStatus, @Param("id") Long id);
}
