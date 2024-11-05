package com.wintux._3.Repositories;

import org.springframework.stereotype.Repository;
import com.wintux._3.Models.Est;
import org.springframework.data.repository.JpaRepository;
@Repository
public class EstudianteRepository extends JpaRepository<Est,Integer>{

}
