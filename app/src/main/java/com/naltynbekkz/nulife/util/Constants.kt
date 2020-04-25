package com.naltynbekkz.nulife.util

import android.app.AlarmManager

class Constants {

    companion object {
        const val MAX_NOTIFICATION_TIME = 2 * AlarmManager.INTERVAL_DAY

        const val USER_COURSE = "user_course"
        const val QUESTION = "question"
        const val ANSWER = "answer"

        const val TODAY = "today"
        const val MONTH = "month"
        const val TASK = "task"
        const val ROUTINE = "routine"
        const val ASSOCIATE = "associate"
        const val OCCURRENCE = "occurrence"

        const val CAFE_ID = "cafe_id"

        const val CLUB_ID = "club_id"
        const val EVENT_ID = "event_id"
        const val ALL = "all"


        const val COORDINATOR_LAYOUT_SCROLL_STATE = "coordinator_layout_scroll_state"
        const val NESTED_SCROLL_STATE = "nested_scroll_state"

        const val REQUEST_CODE_CHOOSE = 0
        const val PERMISSION_REQUEST_CODE = 1
    }

}
