package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}
