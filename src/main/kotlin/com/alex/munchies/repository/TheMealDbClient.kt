package com.alex.munchies.repository

import com.alex.munchies.repository.api.ApiModelMealsGet
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "TheMealDbClient", url = "\${values.themealdb.url}")
interface TheMealDbClient {

    @RequestMapping(method = [RequestMethod.GET], value = ["/v1/1/lookup.php"])
    fun getMeal(@RequestParam("i") id: String): ApiModelMealsGet
}