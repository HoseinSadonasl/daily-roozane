package com.abc.daily.domain.use_case

data class PrefsDataStoreDomain(
    val orderNotesPrefsDataStore: OrderNotesPrefsDataStore,
    val themePrefsDataStore: ThemePrefsDataStore,
    val defaultCityPrefsDataStore: DefaultCityPrefsDataStore,
    val firstLunchPrefsDataStore: FirstLunchPrefsDataStore
)
