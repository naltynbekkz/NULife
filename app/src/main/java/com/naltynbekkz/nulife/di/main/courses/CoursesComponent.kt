package com.naltynbekkz.nulife.di.main.courses

import com.naltynbekkz.nulife.ui.courses.answers.front.AnswersFragment
import com.naltynbekkz.nulife.ui.courses.answers.front.CommentsFragment
import com.naltynbekkz.nulife.ui.courses.answers.front.EditAnswerFragment
import com.naltynbekkz.nulife.ui.courses.answers.front.NewAnswerFragment
import com.naltynbekkz.nulife.ui.courses.courses.front.CoursesFragment
import com.naltynbekkz.nulife.ui.courses.courses.front.EditCourseFragment
import com.naltynbekkz.nulife.ui.courses.courses.front.EnrollCourseFragment
import com.naltynbekkz.nulife.ui.courses.courses.front.EnrollFragment
import com.naltynbekkz.nulife.ui.courses.deadlines.front.DeadlinesFragment
import com.naltynbekkz.nulife.ui.courses.deadlines.front.NewDeadlineFragment
import com.naltynbekkz.nulife.ui.courses.questions.front.EditQuestionFragment
import com.naltynbekkz.nulife.ui.courses.questions.front.NewQuestionFragment
import com.naltynbekkz.nulife.ui.courses.questions.front.QuestionsFragment
import com.naltynbekkz.nulife.ui.courses.resources.front.NewImagesFragment
import com.naltynbekkz.nulife.ui.courses.resources.front.NewResourceFragment
import com.naltynbekkz.nulife.ui.courses.resources.front.ResourcesFragment
import dagger.Subcomponent
import javax.inject.Scope

@CoursesScope
@Subcomponent(
    modules = [
        CoursesModule::class,
        CoursesViewModelsModule::class
    ]
)
interface CoursesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CoursesComponent
    }

    fun inject(fragment: CoursesFragment)
    fun inject(fragment: EnrollCourseFragment)
    fun inject(fragment: EnrollFragment)
    fun inject(fragment: EditCourseFragment)

    fun inject(fragment: DeadlinesFragment)
    fun inject(fragment: NewDeadlineFragment)

    fun inject(fragment: QuestionsFragment)
    fun inject(fragment: NewQuestionFragment)
    fun inject(fragment: EditQuestionFragment)

    fun inject(fragment: ResourcesFragment)
    fun inject(fragment: NewImagesFragment)
    fun inject(fragment: NewResourceFragment)

    fun inject(fragment: AnswersFragment)
    fun inject(fragment: CommentsFragment)
    fun inject(fragment: EditAnswerFragment)
    fun inject(fragment: NewAnswerFragment)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class CoursesScope