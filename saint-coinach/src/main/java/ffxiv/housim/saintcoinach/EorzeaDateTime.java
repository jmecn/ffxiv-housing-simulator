package ffxiv.housim.saintcoinach;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Calendar;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class EorzeaDateTime {
    /**
     * Factor used to convert from real to Eorzean time.<p/>
     * 60 * 24 Eorzean minutes (one day) per 70 real-world minutes.
     */
    public static final double RealToEorzeanFactor = (60.0 * 24.0) / 70.0;
    /**
     * Interval in which the value of Now is updated.
     */
    public static final long NowUpdateInterval = 1000;

    /// <summary>
    ///     <see cref="DateTime" /> of the Unix epoch.
    /// </summary>
    private static final Instant Zero = Instant.ofEpochMilli(0);
    /// <summary>
    ///     Last update of the value of <see cref="EorzeaDateTime.Now" /> in ticks.
    /// </summary>
    private static long LastNowUpdate = Instant.now().toEpochMilli();
    /// <summary>
    ///     Current value of <see cref="Now" />.
    /// </summary>
    private static EorzeaDateTime Now = new EorzeaDateTime();

    /// <summary>
    ///     Current bell (hour) in Eorzean time.
    /// </summary>
    @Getter
    private int bell;

    /// <summary>
    ///     Current minute in Eorzean time.
    /// </summary>
    @Getter
    private int minute;

    /// <summary>
    ///     Current moon (month) in Eorzean time.
    /// </summary>
    @Getter
    private int moon;

    /// <summary>
    ///     Current sun (day) in Eorzean time.
    /// </summary>
    @Getter
    private int sun;

    /// <summary>
    ///     Gets or sets the current year in the Eorzean calendar.
    /// </summary>
    /// <remarks>
    ///     This value is not actually used anywhere in the game, it is only preserved here to make conversion to real-time
    ///     possible.
    /// </remarks>
    /// <value>The current year in the Eorzean calendar.</value>
    @Getter
    @Setter
    private int year;

    /// <summary>
    ///     Initializes a new instance of the <see cref="EorzeaDateTime" /> class that represents the same time and date as
    ///     another <see cref="EorzeaDateTime" />.
    /// </summary>
    /// <param name="source"><see cref="EorzeaDateTime" /> whose values to copy.</param>
    public EorzeaDateTime(EorzeaDateTime source) {
        this.minute = source.getMinute();
        this.bell = source.getBell();
        this.sun = source.getSun();
        this.moon = source.getMoon();
        this.year = source.getYear();
    }

    /// <summary>
    ///     Initializes a new instance of the <see cref="EorzeaDateTime" /> class with the current time and date.
    /// </summary>
    public EorzeaDateTime() {
        this(Instant.now());
    }

    /// <summary>
    ///     Initializes a new instance of the <see cref="EorzeaDateTime" /> class for the specified Unix timestamp.
    /// </summary>
    /// <param name="unixTime">Unix timestamp of the time and date.</param>
    public EorzeaDateTime(int unixTime) {
        this((long)unixTime);
    }

    /// <summary>
    ///     Initializes a new instance of the <see cref="EorzeaDateTime" /> class for the specified Unix timestamp.
    /// </summary>
    /// <param name="unixTime">Unix timestamp of the time and date.</param>
    public EorzeaDateTime(long unixTime) {
        setUnixTime(unixTime);
    }

    /// <summary>
    ///     Initializes a new instance of the <see cref="EorzeaDateTime" /> class with the Eorzean equivalent for
    ///     <see cref="DateTime" />.
    /// </summary>
    /// <param name="time"><see cref="DateTime" /> to convert to Eorzean time.</param>
    public EorzeaDateTime(Instant time) {
        setRealTime(time);
    }

    /// <summary>
    ///     Convert the current date and time to a <see cref="TimeSpan" />.
    /// </summary>
    /// <returns>Returns the <see cref="TimeSpan" /> of elapsed time since zero in the Eorzean calendar.</returns>
    public Long getTimeSpan() {
        long years = year;
        long moons = (years * 12) + moon - 1;
        long suns = (moons * 32) + sun - 1;
        long bells = (suns * 24) + bell;
        long minutes = (bells * 60) + minute;
        long seconds = minutes * 60;
        return seconds;
    }

    /// <summary>
    ///     Get the Unix timestamp.
    /// </summary>
    /// <returns>Returns the Unix timestamp.</returns>
    public long getUnixTime() {
        long years = year;
        long moons = (years * 12) + moon - 1;
        long suns = (moons * 32) + sun - 1;
        long bells = (suns * 24) + bell;
        long minutes = (bells * 60) + minute;
        long seconds = minutes * 60;

        return (long)(seconds / RealToEorzeanFactor);
    }

    /// <summary>
    ///     Set the Unix timestamp.
    /// </summary>
    /// <param name="time">Unix timestamp to use.</param>
    /// <returns>Returns self.</returns>
    public EorzeaDateTime setUnixTime(long time) {
        double eorzeaSeconds = time * RealToEorzeanFactor;

        setEorzeaTime(eorzeaSeconds);

        return this;
    }

    /// <summary>
    ///     Set the Eorzean time using the total elapsed seconds since zero.
    /// </summary>
    /// <param name="eorzeaSeconds">Elapsed seconds since zero.</param>
    private void setEorzeaTime(double eorzeaSeconds) {
        double minutes = eorzeaSeconds / 60;
        double bells = minutes / 60;
        double suns = bells / 24;
        double moons = suns / 32;
        double years = moons / 12;

        minute = (int)(minutes % 60);
        bell = (int)(bells % 24);
        sun = (int)(suns % 32) + 1;
        moon = (int)(moons % 12) + 1;
        year = (int)years;
    }

    /// <summary>
    ///     Get the real-world <see cref="DateTime" /> of this <see cref="EorzeaDateTime" />.
    /// </summary>
    /// <returns>Returns the <see cref="DateTime" /> of this <see cref="EorzeaDateTime" />.</returns>
    public Instant getRealTime() {
        return Instant.ofEpochSecond(getUnixTime());
    }

    /// <summary>
    ///     Set the value of this <see cref="EorzeaDateTime" /> from a real-world <see cref="DateTime" />.
    /// </summary>
    /// <param name="time"><see cref="DateTime" /> to convert.</param>
    /// <returns>Returns self.</returns>
    public EorzeaDateTime setRealTime(Instant time) {
        return setUnixTime(time.getEpochSecond());
    }

    /// <summary>
    ///     Gets the current time in the Eorzean calendar.
    /// </summary>
    /// <value>The current time in the Eorzean calendar.</value>
    public static EorzeaDateTime now() {
        long nt = Instant.now().toEpochMilli();
        long d = nt - LastNowUpdate;
        if (d <= NowUpdateInterval) return Now;

        Now = new EorzeaDateTime();
        LastNowUpdate = nt;
        return Now;
    }


    /// <summary>
    ///     Gets or sets the current minute in the Eorzean calendar.
    /// </summary>
    /// <remarks>
    ///     Setting values outside the 0..59 range will be adjusted accordingly and carry over to <see cref="Bell" />.
    /// </remarks>
    /// <value>The current minute in the Eorzean calendar.</value>
    public void setMinute(int value) {
        minute = value;
        while (minute < 0) {
            minute += 60;
            bell--;
        }
        while (minute >= 60) {
            minute -= 60;
            bell++;
        }
    }

    /// <summary>
    ///     Gets or sets the current bell (hour) in the Eorzean calendar.
    /// </summary>
    /// <remarks>
    ///     Setting values outside the 0..23 range will be adjusted accordingly and carry over to <see cref="Sun" />.
    /// </remarks>
    /// <value>The current bell (hour) in the Eorzean calendar.</value>
    public void setBell(int value) {
        bell = value;
        while (bell < 0) {
            bell += 24;
            sun--;
        }
        while (bell >= 24) {
            bell -= 24;
            sun++;
        }
    }

    /// <summary>
    ///     Gets or sets the current sun (day) in the Eorzean calendar.
    /// </summary>
    /// <remarks>
    ///     Setting values outside the 1..32 range will be adjusted accordingly and carry over to <see cref="Moon" />.
    /// </remarks>
    /// <value>The current sun (day) in the Eorzean calendar.</value>
    public void setSun(int value) {
        sun = value;
        while (sun < 1) {
            sun += 32;
            moon--;
        }
        while (sun > 32) {
            sun -= 32;
            moon++;
        }
    }

    /// <summary>
    ///     Gets or sets the current moon (month) in the Eorzean calendar.
    /// </summary>
    /// <remarks>
    ///     Setting values outside the 1..12 range will be adjusted accordingly and carry over to <see cref="Year" />.
    /// </remarks>
    /// <value>The current moon (month) in the Eorzean calendar.</value>
    public void setMoon(int value) {
        moon = value;
        while (moon < 1) {
            moon += 12;
            year--;
        }
        while (moon > 12) {
            moon -= 12;
            year++;
        }
    }

    /// <summary>
    ///     Gets the total amount of minutes in the Eorzean calendar since zero.
    /// </summary>
    /// <value>The total amount of minutes in the Eorzean calendar since zero.</value>
    public double getTotalMinutes() {
        return minute + (60.0 * (bell + (24.0 * (sun + (32.0 * (moon + (12.0 * year)))))));
    }

    /// <summary>
    ///     Gets the total amount of bells (hours) in the Eorzean calendar since zero.
    /// </summary>
    /// <value>The total amount of bells (hours) in the Eorzean calendar since zero.</value>
    public double getTotalBells() {
        return bell + (minute / 60.0) + (24 * (sun + (32.0 * (moon + (12.0 * year)))));
    }

    /// <summary>
    ///     Gets the total amount of suns (days) in the Eorzean calendar since zero.
    /// </summary>
    /// <value>The total amount of suns (days) in the Eorzean calendar since zero.</value>
    public double getTotalSuns() {
        return sun + ((bell + (minute / 60.0)) / 24.0) + (32 * (moon + (12.0 * year)));
    }

    /// <summary>
    ///     Gets the total amount of moons (months) in the Eorzean calendar since zero.
    /// </summary>
    /// <value>The total amount of moons (months) in the Eorzean calendar since zero.</value>
    public double getTotalMoons() {
        return moon + ((sun + ((bell + (minute / 60.0)) / 24.0)) / 32.0) + (12.0 * year);
    }

    /// <summary>
    ///     Gets the total amount of years in the Eorzean calendar since zero.
    /// </summary>
    /// <value>The total amount of years in the Eorzean calendar since zero.</value>
    public double getTotalYears() {
        return year + ((moon + ((sun + ((bell + (minute / 60.0)) / 24.0)) / 32.0)) / 12.0);
    }

    /// <summary>
    ///     Clones the value of this <see cref="EorzeaDateTime" /> into a new <see cref="EorzeaDateTime" />.
    /// </summary>
    /// <returns>Returns a new <seealso cref="EorzeaDateTime" /> object with the same value as the current.</returns>
    @Override
    public EorzeaDateTime clone() {
        return new EorzeaDateTime(this);
    }
}
