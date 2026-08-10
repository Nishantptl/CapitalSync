package com.nishant.capitalsync.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private fun getDateFormat() = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun formatTimestamp(timestamp: Long): String {
        return getDateFormat().format(Date(timestamp))
    }

    fun getDayLabel(timestamp: Long): String {
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        return dayFormat.format(Date(timestamp))
    }

    /**
     * Returns the 7 days of a specific week based on weekOffset (0 = current week).
     * The week starts on Monday and ends on Sunday.
     */
    fun getWeekDays(weekOffset: Int = 0): List<Long> {
        val calendar = Calendar.getInstance(Locale.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Calculate offset to Monday
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysFromMonday = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - Calendar.MONDAY
        calendar.add(Calendar.DAY_OF_YEAR, -daysFromMonday)

        // Apply week offset
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset)

        return List(7) { index ->
            if (index > 0) calendar.add(Calendar.DAY_OF_YEAR, 1)
            calendar.timeInMillis
        }
    }

    fun formatWeekRange(dates: List<Long>): String {
        if (dates.isEmpty()) return ""
        val start = dates.first()
        val end = dates.last()

        val calStart = Calendar.getInstance().apply { timeInMillis = start }
        val calEnd = Calendar.getInstance().apply { timeInMillis = end }

        val monthYearFormat = SimpleDateFormat("MMM yy", Locale.getDefault())
        val monthYear = monthYearFormat.format(calEnd.time)

        val startDaySuffix = getDayWithSuffix(calStart.get(Calendar.DAY_OF_MONTH))
        val endDaySuffix = getDayWithSuffix(calEnd.get(Calendar.DAY_OF_MONTH))

        return "$startDaySuffix - $endDaySuffix $monthYear"
    }

    private fun getDayWithSuffix(day: Int): String {
        if (day in 11..13) return "${day}th"
        return when (day % 10) {
            1 -> "${day}st"
            2 -> "${day}nd"
            3 -> "${day}rd"
            else -> "${day}th"
        }
    }

    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun parseDate(dateString: String): Long {
        return try {
            getDateFormat().parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}