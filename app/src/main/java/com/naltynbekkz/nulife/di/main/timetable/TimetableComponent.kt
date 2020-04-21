package com.naltynbekkz.nulife.di.main.timetable

import com.naltynbekkz.nulife.ui.timetable.front.*
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
    fun inject(activity: EditRoutineActivity)
    fun inject(activity: EditTaskActivity)
    fun inject(activity: NewRoutineActivity)
    fun inject(activity: NewTaskActivity)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class TimetableScope