package com.alex.munchies.repository.database.label

import com.alex.munchies.repository.database.Repository
import org.springframework.data.repository.CrudRepository

interface LabelRepository : CrudRepository<DbModelLabel, Long>, Repository<DbModelLabel>