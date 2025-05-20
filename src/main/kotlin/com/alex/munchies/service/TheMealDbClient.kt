package com.alex.munchies.service

import com.alex.munchies.domain.api.ApiModelMeals
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "TheMealDbClient", url = "\${values.themealdb.url}")
interface TheMealDbClient {

    @RequestMapping(method = [RequestMethod.GET], value = ["/v1/1/lookup.php"])
    fun getMeal(@RequestParam("i") id: String): ApiModelMeals
}