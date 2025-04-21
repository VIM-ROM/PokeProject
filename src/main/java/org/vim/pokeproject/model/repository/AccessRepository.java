package org.vim.pokeproject.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vim.pokeproject.model.entity.Access;

@Repository
public interface AccessRepository extends JpaRepository <Access, Integer>{
}
