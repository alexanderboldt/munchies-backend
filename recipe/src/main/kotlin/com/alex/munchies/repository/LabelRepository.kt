package com.alex.munchies.repository

import com.alex.munchies.entity.LabelEntity
import org.springframework.data.repository.CrudRepository

interface LabelRepository : CrudRepository<LabelEntity, Long>, BaseRepository<LabelEntity>
