package com.example.ncerttracker.data.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DayProgressItem(
    val dayKey: String, // "yyyy-MM-dd"
    val displayDate: String, // "28 Sep"
    val dayOfWeek: String, // "Mon"
    val fullDateLabel: String, // "Monday, 28 September 2026"
    val timestamp: Long,
    val isToday: Boolean,
    val totalCount: Int,
    val physicsCount: Int,
    val chemistryCount: Int,
    val mathsCount: Int,
    val chapters: List<ChapterEntity> = emptyList()
)

data class DailyStatistics(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalActiveDays: Int = 0,
    val todayCount: Int = 0,
    val dailyGoal: Int = 2,
    val dailyGoalAchieved: Boolean = false,
    val bestDayLabel: String = "None yet",
    val averagePerActiveDay: Float = 0f,
    val weeklyCompletionCount: Int = 0,
    val monthlyCompletionCount: Int = 0,
    val chartDays: List<DayProgressItem> = emptyList(),
    val selectedDay: DayProgressItem? = null,
    val completedHistoryByDate: List<DayProgressItem> = emptyList()
)

object DailyStatsCalculator {

    private val keyFormat = ThreadLocal.withInitial { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    private val displayFormat = ThreadLocal.withInitial { SimpleDateFormat("d MMM", Locale.getDefault()) }
    private val dayOfWeekFormat = ThreadLocal.withInitial { SimpleDateFormat("EEE", Locale.getDefault()) }
    private val fullDateFormat = ThreadLocal.withInitial { SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()) }

    fun calculate(
        chapters: List<ChapterEntity>,
        rangeDays: Int = 7,
        dailyGoal: Int = 2,
        selectedDayKey: String? = null
    ): DailyStatistics {
        val kFmt = keyFormat.get()!!
        val dFmt = displayFormat.get()!!
        val wFmt = dayOfWeekFormat.get()!!
        val fFmt = fullDateFormat.get()!!

        // Group completed chapters by dayKey ("yyyy-MM-dd")
        val completedChapters = chapters.filter { it.isCompleted && it.completedAt != null }

        val chaptersByDay = mutableMapOf<String, MutableList<ChapterEntity>>()
        for (ch in completedChapters) {
            val ts = ch.completedAt ?: continue
            val key = kFmt.format(Date(ts))
            chaptersByDay.getOrPut(key) { mutableListOf() }.add(ch)
        }

        // Today's calendar
        val nowCal = Calendar.getInstance()
        val todayKey = kFmt.format(nowCal.time)

        // Generate chart days for the range (ending today)
        val chartDays = mutableListOf<DayProgressItem>()
        for (i in (rangeDays - 1) downTo 0) {
            val cal = Calendar.getInstance().apply {
                time = nowCal.time
                add(Calendar.DAY_OF_YEAR, -i)
            }
            val dKey = kFmt.format(cal.time)
            val dChapters = chaptersByDay[dKey] ?: emptyList()
            val isToday = (dKey == todayKey)

            val pCount = dChapters.count { it.subject.equals("PHYSICS", ignoreCase = true) }
            val cCount = dChapters.count { it.subject.equals("CHEMISTRY", ignoreCase = true) }
            val mCount = dChapters.count { it.subject.equals("MATHEMATICS", ignoreCase = true) }

            chartDays.add(
                DayProgressItem(
                    dayKey = dKey,
                    displayDate = dFmt.format(cal.time),
                    dayOfWeek = wFmt.format(cal.time),
                    fullDateLabel = fFmt.format(cal.time),
                    timestamp = cal.timeInMillis,
                    isToday = isToday,
                    totalCount = dChapters.size,
                    physicsCount = pCount,
                    chemistryCount = cCount,
                    mathsCount = mCount,
                    chapters = dChapters
                )
            )
        }

        // Today count
        val todayChapters = chaptersByDay[todayKey] ?: emptyList()
        val todayCount = todayChapters.size
        val goalAchieved = todayCount >= dailyGoal

        // Streaks calculation using unique sorted active dates
        val activeKeysSorted = chaptersByDay.keys
            .filter { (chaptersByDay[it]?.size ?: 0) > 0 }
            .sorted()

        var currentStreak = 0
        var longestStreak = 0

        if (activeKeysSorted.isNotEmpty()) {
            // Check current streak: consecutive active days going backwards from today or yesterday
            var checkCal = Calendar.getInstance().apply {
                time = nowCal.time
            }
            var checkKey = kFmt.format(checkCal.time)

            if (!chaptersByDay.containsKey(checkKey) || (chaptersByDay[checkKey]?.isEmpty() == true)) {
                // If today has no completions, check if yesterday was active
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
                checkKey = kFmt.format(checkCal.time)
            }

            while (chaptersByDay.containsKey(checkKey) && (chaptersByDay[checkKey]?.isNotEmpty() == true)) {
                currentStreak++
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
                checkKey = kFmt.format(checkCal.time)
            }

            // Longest streak
            var tempStreak = 1
            longestStreak = 1
            val parsedDates = activeKeysSorted.mapNotNull {
                try {
                    kFmt.parse(it)
                } catch (e: Exception) {
                    null
                }
            }

            for (idx in 1 until parsedDates.size) {
                val prev = parsedDates[idx - 1]
                val curr = parsedDates[idx]
                val diffDays = ((curr.time - prev.time) / (1000 * 60 * 60 * 24)).toInt()

                if (diffDays == 1) {
                    tempStreak++
                    if (tempStreak > longestStreak) {
                        longestStreak = tempStreak
                    }
                } else if (diffDays > 1) {
                    tempStreak = 1
                }
            }
            if (currentStreak > longestStreak) {
                longestStreak = currentStreak
            }
        }

        val totalActiveDays = chaptersByDay.keys.count { (chaptersByDay[it]?.size ?: 0) > 0 }
        val totalCompleted = completedChapters.size
        val avgPerActiveDay = if (totalActiveDays > 0) {
            totalCompleted.toFloat() / totalActiveDays
        } else {
            0f
        }

        // Best Day
        var bestCount = 0
        var bestDayStr = "None yet"
        for ((k, list) in chaptersByDay) {
            if (list.size > bestCount) {
                bestCount = list.size
                try {
                    val d = kFmt.parse(k)
                    if (d != null) {
                        bestDayStr = "${dFmt.format(d)} ($bestCount chapters)"
                    }
                } catch (_: Exception) {
                    bestDayStr = "$k ($bestCount ch)"
                }
            }
        }

        // Weekly (last 7 days) and Monthly (last 30 days) completions
        val sevenDaysAgoTime = nowCal.timeInMillis - (7L * 24 * 60 * 60 * 1000)
        val thirtyDaysAgoTime = nowCal.timeInMillis - (30L * 24 * 60 * 60 * 1000)
        val weeklyCount = completedChapters.count { (it.completedAt ?: 0L) >= sevenDaysAgoTime }
        val monthlyCount = completedChapters.count { (it.completedAt ?: 0L) >= thirtyDaysAgoTime }

        // Selected Day (defaults to selectedDayKey, or today, or last day in chart)
        val activeSelectedKey = selectedDayKey ?: todayKey
        val selectedDay = chartDays.find { it.dayKey == activeSelectedKey }
            ?: chartDays.lastOrNull()

        // Complete history grouped by date, descending
        val historyList = chaptersByDay.entries
            .filter { it.value.isNotEmpty() }
            .sortedByDescending { it.key }
            .map { (key, chList) ->
                val date = try { kFmt.parse(key) } catch (e: Exception) { null }
                val isToday = (key == todayKey)
                DayProgressItem(
                    dayKey = key,
                    displayDate = if (date != null) dFmt.format(date) else key,
                    dayOfWeek = if (date != null) wFmt.format(date) else "",
                    fullDateLabel = if (date != null) fFmt.format(date) else key,
                    timestamp = date?.time ?: 0L,
                    isToday = isToday,
                    totalCount = chList.size,
                    physicsCount = chList.count { it.subject.equals("PHYSICS", ignoreCase = true) },
                    chemistryCount = chList.count { it.subject.equals("CHEMISTRY", ignoreCase = true) },
                    mathsCount = chList.count { it.subject.equals("MATHEMATICS", ignoreCase = true) },
                    chapters = chList.sortedByDescending { it.completedAt ?: 0L }
                )
            }

        return DailyStatistics(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            totalActiveDays = totalActiveDays,
            todayCount = todayCount,
            dailyGoal = dailyGoal,
            dailyGoalAchieved = goalAchieved,
            bestDayLabel = bestDayStr,
            averagePerActiveDay = avgPerActiveDay,
            weeklyCompletionCount = weeklyCount,
            monthlyCompletionCount = monthlyCount,
            chartDays = chartDays,
            selectedDay = selectedDay,
            completedHistoryByDate = historyList
        )
    }
}
