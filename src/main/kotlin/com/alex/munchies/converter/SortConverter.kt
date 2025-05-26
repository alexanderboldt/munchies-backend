package com.alex.munchies.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.Sort

/**
 * Converts a sort query from a `String` in a request to a [Sort] object.
 *
 * Sort values are separated with a comma and can have a minus for a descending order.
 * If a minus is not available, the ascending order will be used as default.
 *
 * Example:
 * Using this query parameter: "id,-title,description"
 * This will lead to the following group sort: id in ascending order, title in descending order and description in ascending order.
 */
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