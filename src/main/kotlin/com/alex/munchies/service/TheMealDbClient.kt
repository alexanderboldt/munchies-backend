package com.alex.munchies.service

import com.alex.munchies.domain.Meals
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * Interface manages the rest-calls for `The Meal DB`.
 */
@FeignClient(name = "TheMealDbClient", url = $$"${values.themealdb.url}")
interface TheMealDbClient {

    /**
     * Makes a GET-call to retrieve a meal.
     *
     * @param id The id of a meal as a `String`.
     * @return The meals as [Meals].
     */
    @RequestMapping(method = [RequestMethod.GET], value = ["/v1/1/lookup.php"])
    fun getMeal(@RequestParam("i") id: String): Meals
}
