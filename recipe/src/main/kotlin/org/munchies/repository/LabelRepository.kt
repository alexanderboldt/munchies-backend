package org.munchies.repository

import org.munchies.entity.LabelEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface LabelRepository : CoroutineCrudRepository<LabelEntity, Long>, BaseRepository<LabelEntity>
