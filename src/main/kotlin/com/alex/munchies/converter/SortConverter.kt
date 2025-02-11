package com.alex.munchies.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.Sort

class SortConverter : Converter<String, Sort> {

    override fun convert(source: String): Sort {
        return source
            .split(",")
            .map { split ->
                when (split.startsWith("-")) {
                    true -> Sort.Order(Sort.Direction.DESC, split.drop(1))
                    false -> Sort.Order(Sort.Direction.ASC, split)
                }
            }.let { Sort.by(it) }
    }
}