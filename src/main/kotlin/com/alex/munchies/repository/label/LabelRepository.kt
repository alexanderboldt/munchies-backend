package com.alex.munchies.repository.label

import com.alex.munchies.repository.Repository
import org.springframework.data.repository.CrudRepository

interface LabelRepository : CrudRepository<LabelEntity, Long>, Repository<LabelEntity>