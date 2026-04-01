package com.example.MiniProject.Repository;

import com.example.MiniProject.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;


@Repository
public interface CourseRepo extends JpaRepository<Course,Long>, JpaSpecificationExecutor<Course> {
    boolean existsByCourseNameIgnoreCase(String courseName);

    boolean existsByCourseNameIgnoreCaseAndTenantId(String courseName, String tenantId);

    Optional<Course> findByCourseIdAndTenantId(Long courseId, String tenantId);

    List<Course> findAllByTenantId(String tenantId);

    @Query("select distinct c.tenantId from Course c where c.tenantId is not null and c.tenantId <> ''")
    List<String> findDistinctTenantIds();
}
