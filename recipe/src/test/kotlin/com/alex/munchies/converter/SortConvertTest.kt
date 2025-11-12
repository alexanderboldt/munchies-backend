package com.alex.munchies.converter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

@Suppress("unused")
class SortConvertTest : StringSpec({

    val sortConverter = SortConverter()

    "should convert id asc" {
        val sortList = sortConverter.convert("id").toList()
        val sortId = sortList[0]

        sortList shouldHaveSize 1
        sortId.property shouldBe "id"
        sortId.isAscending shouldBe true
    }

    "should convert id desc" {
        val sortList = sortConverter.convert("-id").toList()
        val sortId = sortList[0]

        sortList shouldHaveSize 1
        sortId.property shouldBe "id"
        sortId.isDescending shouldBe true
    }

    "should convert id asc and title asc" {
        val sortList = sortConverter.convert("id,title").toList()
        val sortId = sortList[0]
        val sortTitle = sortList[1]

        sortList shouldHaveSize 2

        sortId.property shouldBe "id"
        sortId.isAscending shouldBe true

        sortTitle.property shouldBe "title"
        sortTitle.isAscending shouldBe true
    }

    "should convert id desc and title asc" {
        val sortList = sortConverter.convert("-id,title").toList()
        val sortId = sortList[0]
        val sortTitle = sortList[1]

        sortList shouldHaveSize 2

        sortId.property shouldBe "id"
        sortId.isDescending shouldBe true

        sortTitle.property shouldBe "title"
        sortTitle.isAscending shouldBe true
    }

    "should convert id asc and title desc" {
        val sortList = sortConverter.convert("id,-title").toList()
        val sortId = sortList[0]
        val sortTitle = sortList[1]

        sortList shouldHaveSize 2

        sortId.property shouldBe "id"
        sortId.isAscending shouldBe true

        sortTitle.property shouldBe "title"
        sortTitle.isDescending shouldBe true
    }

    "should convert id desc and title desc" {
        val sortList = sortConverter.convert("-id,-title").toList()
        val sortId = sortList[0]
        val sortTitle = sortList[1]

        sortList shouldHaveSize 2

        sortId.property shouldBe "id"
        sortId.isDescending shouldBe true

        sortTitle.property shouldBe "title"
        sortTitle.isDescending shouldBe true
    }
})
