package com.alex.munchies.repository

import com.alex.munchies.entity.StepEntity
import org.springframework.data.repository.CrudRepository

interface StepRepository : CrudRepository<StepEntity, Long>, Repository<StepEntity>
