package com.alex.munchies.converter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SortConvertTest {

    private val sortConverter = SortConverter()

    @Test
    fun testIdAsc() {
        val sortList = sortConverter.convert("id").toList()
        val sortId = sortList[0]

        assertThat(sortList.size).isEqualTo(1)
        assertThat(sortId.property).isEqualTo("id")
        assertThat(sortId.isAscending).isTrue
    }

    @Test
    fun testIdDesc() {
        val sortList = sortConverter.convert("-id").toList()
        val sortId = sortList[0]

        assertThat(sortList.size).isEqualTo(1)
        assertThat(sortId.property).isEqualTo("id")
        assertThat(sortId.isDescending).isTrue
    }

    @Test
    fun testIdAscAndTitleAsc() {
        val sortList = sortConverter.convert("id,title").toList()
        val sortId = sortList[0]
        val sortTitle = sortList[1]

        assertThat(sortList.size).isEqualTo(2)

        assertThat(sortId.property).isEqualTo("id")
        assertThat(sortId.isAscending).isTrue

        assertThat(sortTitle.property).isEqualTo("title")
        assertThat(sortTitle.isAscending).isTrue
    }

    @Test
    fun testIdDescAndTitleAsc() {
        val sortList = sortConverter.convert("-id,title").toList()
        val sortId = sortList[0]
        val sortTitle = sortList[1]

        assertThat(sortList.size).isEqualTo(2)

        assertThat(sortId.property).isEqualTo("id")
        assertThat(sortId.isDescending).isTrue

        assertThat(sortTitle.property).isEqualTo("title")
        assertThat(sortTitle.isAscending).isTrue
    }

    @Test
    fun testIdAscAndTitleDesc() {
        val sortList = sortConverter.convert("id,-title").toList()
        val sortId = sortList[0]
        val sortTitle = sortList[1]

        assertThat(sortList.size).isEqualTo(2)

        assertThat(sortId.property).isEqualTo("id")
        assertThat(sortId.isAscending).isTrue

        assertThat(sortTitle.property).isEqualTo("title")
        assertThat(sortTitle.isDescending).isTrue
    }

    @Test
    fun testIdDescAndTitleDesc() {
        val sortList = sortConverter.convert("-id,-title").toList()
        val sortId = sortList[0]
        val sortTitle = sortList[1]

        assertThat(sortList.size).isEqualTo(2)

        assertThat(sortId.property).isEqualTo("id")
        assertThat(sortId.isDescending).isTrue

        assertThat(sortTitle.property).isEqualTo("title")
        assertThat(sortTitle.isDescending).isTrue
    }
}
