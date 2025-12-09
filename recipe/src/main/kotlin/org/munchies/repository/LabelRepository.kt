package org.munchies.repository

import org.munchies.entity.LabelEntity
import org.springframework.data.repository.CrudRepository

interface LabelRepository : CrudRepository<LabelEntity, Long>, BaseRepository<LabelEntity>
