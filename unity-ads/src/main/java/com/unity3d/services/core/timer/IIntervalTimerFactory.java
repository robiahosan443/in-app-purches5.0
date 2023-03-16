package com.unity3d.services.core.timer;

public interface IIntervalTimerFactory {
	IIntervalTimer createTimer(final String timedActivityName, final Integer totalDurationMs, final Integer totalIntervals, final IIntervalTimerListener timerListener);
}
