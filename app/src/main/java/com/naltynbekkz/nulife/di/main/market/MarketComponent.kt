package com.naltynbekkz.nulife.di.main.market

import com.naltynbekkz.nulife.ui.market.front.MarketFragment
import dagger.Subcomponent
import javax.inject.Scope

@MarketScope
@Subcomponent(
    modules = [
        MarketModule::class,
        MarketViewModelsModule::class
    ]
)
interface MarketComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): MarketComponent
    }

    fun inject(fragment: MarketFragment)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class MarketScope