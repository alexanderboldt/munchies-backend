package org.munchies.configuration

import org.munchies.converter.SortConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ApplicationConfiguration : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(SortConverter())
    }
}
