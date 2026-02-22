package com.example.MiniProject.Repository;


import com.example.MiniProject.Entity.IdCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdCardRepo extends JpaRepository<IdCard,Long> {
}
