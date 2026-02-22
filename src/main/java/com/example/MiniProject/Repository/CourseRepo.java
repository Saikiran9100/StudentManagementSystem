package com.example.MiniProject.Repository;

import com.example.MiniProject.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;


@Repository
public interface CourseRepo extends JpaRepository<Course,Long> {
}
