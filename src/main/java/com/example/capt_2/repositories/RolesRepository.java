package com.example.capt_2.repositories;

import com.example.capt_2.models.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends CrudRepository<RoleEntity, Long>{
}
