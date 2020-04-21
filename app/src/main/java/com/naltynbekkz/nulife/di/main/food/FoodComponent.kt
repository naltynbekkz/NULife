package com.naltynbekkz.nulife.di.main.food

import com.naltynbekkz.nulife.ui.food.front.*
import dagger.Subcomponent
import javax.inject.Scope

@FoodScope
@Subcomponent(
    modules = [
        FoodViewModelsModule::class
    ]
)
interface FoodComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FoodComponent
    }

    fun inject(fragment: FoodFragment)
    fun inject(fragment: CafeDetailsFragment)
    fun inject(fragment: CafeMenuFragment)
    fun inject(fragment: CafeFragment)
    fun inject(fragment: NewReviewFragment)
    fun inject(fragment: ReviewsFragment)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class FoodScope