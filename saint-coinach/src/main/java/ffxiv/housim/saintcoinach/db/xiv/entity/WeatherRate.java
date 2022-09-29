package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.EorzeaDateTime;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.utils.Pair;
import sun.jvm.hotspot.utilities.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Class enabling the prediction of weather in Eorzea.
 */
public class WeatherRate extends XivRow {
    /**
     * Interval in which the {@link Weather} can change.
     */
    public static final long WeatherChangeInterval = 8 * 3600 * 1000;// 8 hours

    /**
     * {@link Weather}s possible for the current group.
     */
    private List<Weather> possibleWeathers;

    /**
     * Helper field for forecast. <c>Key</c> is the maximum predication value for <c>Val</c> to happen.
     */
    private List<Pair<Integer, Weather>> weatherRates;

    public WeatherRate(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
        int count = 8;
        List<Weather> w = new ArrayList<>();
        List<Pair<Integer, Weather>> wr = new ArrayList<>();
        int min = 0;
        for (int i = 0; i < count; i++) {
            String suffix = String.format("[%d]", i);
            Weather weather = (Weather) get("Weather" + suffix);
            int rate = (int) get("Rate" + suffix);
            w.add(weather);
            wr.add(new Pair<>(min + rate, weather));

            min += rate;
        }
        possibleWeathers = w;
        weatherRates = wr;
    }


    /// <summary>
    ///     Forecast the <see cref="Weather" /> for the current location at a specific <see cref="EorzeaDateTime" />.
    /// </summary>
    /// <param name="time"><see cref="EorzeaDateTime" /> for which to forecast the weather.</param>
    /// <returns>The <see cref="Weather" /> at the current location at <c>time</c>.</returns>
    public Weather forecast(EorzeaDateTime time) {
        int target = calculateTarget(time);

        return weatherRates.stream().filter(it -> target < it.getKey()).map(Pair::getVal).findFirst().orElse(null);
    }

    /// <summary>
    ///     Calculate the value used for the <see cref="Forecast" /> at a specific <see cref="EorzeaDateTime" />.
    /// </summary>
    /// <param name="time"><see cref="EorzeaDateTime" /> for which to calculate the value.</param>
    /// <returns>The value from 0..99 (inclusive) calculated based on <c>time</c>.</returns>
    private static int calculateTarget(EorzeaDateTime time) {
        long unix = time.getUnixTime();
        // Get Eorzea hour for weather start
        long bell = unix / 175;
        // Do the magic 'cause for calculations 16:00 is 0, 00:00 is 8 and 08:00 is 16
        long increment = (bell + 8 - (bell % 8)) % 24;

        // Take Eorzea days since unix epoch
        long totalDays = unix / 4200;

        long calcBase = (totalDays * 0x64) + increment;

        long step1 = (calcBase << 0xB) ^ calcBase;
        long step2 = (step1 >> 8) ^ step1;

        return (int)(step2 % 0x64);
    }

}
