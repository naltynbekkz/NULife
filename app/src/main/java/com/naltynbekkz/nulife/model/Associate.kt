package com.naltynbekkz.nulife.model

import java.io.Serializable

class Associate(
    var id: String,
    var title: String,
    var type: String = Occurrence.CUSTOM,
    var color: String?
) : Serializable {

    constructor(userCourse: UserCourse) : this(
        id = userCourse.id,
        title = userCourse.id,
        type = Occurrence.COURSE,
        color = userCourse.color
    )

    constructor(club: UserClub) : this(
        id = club.id,
        title = club.title,
        type = Occurrence.CLUB,
        color = null
    )

    constructor(occurrence: Occurrence) : this(
        id = if (occurrence.routineType != null) occurrence.id else occurrence.parentId!!,
        title = if (occurrence.routineType != null) occurrence.title else occurrence.parentTitle!!,
        type = if (occurrence.routineType != null) occurrence.routineType!! else occurrence.parentType!!,
        color = occurrence.color
    )

    companion object {

        fun getDataFromUserCourses(userCourses: ArrayList<UserCourse>): ArrayList<Associate> {
            return ArrayList<Associate>().apply {
                userCourses.forEach {
                    add(Associate(it))
                }
            }
        }

        fun getDataFromUserClubs(userClubs: ArrayList<UserClub>): ArrayList<Associate> {
            return ArrayList<Associate>().apply {
                userClubs.forEach {
                    add(Associate(it))
                }
            }
        }

        fun getData(occurrences: List<Occurrence>?): ArrayList<Associate> {
            return ArrayList<Associate>().apply {
                occurrences?.forEach {
                    add(Associate(it))
                }
            }
        }
    }
}