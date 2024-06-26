package com.team.testeffective.navigate

import androidx.fragment.app.Fragment

interface NavigateHelper {

    fun navigateTo(fragment: Fragment)

    fun replaceScreen(fragment: Fragment)
}