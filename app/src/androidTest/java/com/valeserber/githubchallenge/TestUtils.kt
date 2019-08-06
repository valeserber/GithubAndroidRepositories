package com.valeserber.githubchallenge

import com.valeserber.githubchallenge.database.DBOwner
import com.valeserber.githubchallenge.database.DBRepository

class TestUtils {
    companion object {
        fun getTestOwner1() = DBOwner(
            1342004L,
            "google",
            "https://avatars1.githubusercontent.com/u/1342004?v=4"
        )

        fun getTestOwner2() = DBOwner(
            1833474L,
            "wasabeef",
            "https://avatars2.githubusercontent.com/u/1833474?v=4"
        )

        fun getTestRepository1() = DBRepository(
            24953448L,
            "material-design-icons",
            "Material Design icons by Google",
            "https://api.github.com/repos/google/material-design-icons",
            10L,
            20L,
            30L,
            null,
            1342004L
        )

        fun getTestRepository2() = DBRepository(
            28428729L,
            "awesome-android-ui",
            "A curated list of awesome Android UI/UX libraries",
            "https://api.github.com/repos/wasabeef/awesome-android-ui",
            15L,
            25L,
            35L,
            "Java",
            1833474L
        )

        fun getTestRepository3() = DBRepository(
            28428728L,
            "awesome-android-ui-2",
            "A curated list of awesome Android UI/UX libraries 2",
            "https://api.github.com/repos/wasabeef/awesome-android-ui",
            18L,
            28L,
            38L,
            "Kotlin",
            1833474L
        )
    }
}