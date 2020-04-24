package com.naltynbekkz.nulife.di.main.timetable

import com.naltynbekkz.nulife.ui.timetable.front.MonthFragment
import com.naltynbekkz.nulife.ui.timetable.front.NewRoutineFragment
import com.naltynbekkz.nulife.ui.timetable.front.NewTaskFragment
import com.naltynbekkz.nulife.ui.timetable.front.WeekdayFragment
import dagger.Subcomponent
import javax.inject.Scope

@TimetableScope
@Subcomponent(
    modules = [
        TimetableViewModelsModule::class
    ]
)
interface TimetableComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TimetableComponent
    }

    fun inject(fragment: MonthFragment)
    fun inject(fragment: WeekdayFragment)
    fun inject(fragment: NewRoutineFragment)
    fun inject(fragment: NewTaskFragment)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class TimetableScope