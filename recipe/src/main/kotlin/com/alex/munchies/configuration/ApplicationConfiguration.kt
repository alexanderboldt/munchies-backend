package com.alex.munchies.configuration

import com.alex.munchies.converter.SortConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Suppress("unused")
@Configuration
class ApplicationConfiguration : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(SortConverter())
    }
}
