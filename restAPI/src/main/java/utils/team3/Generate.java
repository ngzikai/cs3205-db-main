package utils.team3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;

import entity.steps.*;

public class Generate {
		private static final long ZERO_LONG = 0L;
		private static final long ONE_LONG = 1L;
		private static final int SESSION_UPPER_BOUND = 21;
		private static final int SESSION_LOWER_BOUND = 20;
		private static final long DIFF_UPPER_BOUND = 100000;
		private static final long DIFF_LOWER_BOUND = 10;
		private static final int POS_0 = 0;
		private static final int POS_1 = 1;
		private static final int POS_2 = 2;
		private static int noOFDataSessions = 10;
		private static String filePath = "/tmp/CS3205/";

		public static void main(String[] args) {
			ArrayList<Steps> stepsDataList = createStepsData(noOFDataSessions);

			write(stepsDataList);

			for (int i = 0; i < stepsDataList.size(); i++) {
				Gson gson = new Gson();
				String json = gson.toJson(stepsDataList.get(i));

				System.out.println(json);
			}
		}

		public static ArrayList<Steps> createStepsData(int noOFDataSessions) {
			ArrayList<Steps> stepsDataList = new ArrayList<Steps>();

			for (int i = 0; i < noOFDataSessions; i++) {
				long timestamp = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(10000, 20000000);
				Steps data = new Steps(timestamp, "session_" + timestamp);

				int seed = ThreadLocalRandom.current().nextInt(SESSION_LOWER_BOUND, SESSION_UPPER_BOUND);
				ArrayList<ArrayList<Long>> values = generateValues(timestamp, seed);

				Steps_Time time = new Steps_Time();
				time.setValues(values.get(POS_0));
				data.setTime(time);

				Steps_Channels channels = new Steps_Channels();

				ArrayList<Steps_Channel> channelsData = new ArrayList<Steps_Channel>();

				Steps_Channel timeDifference = new Steps_Channel();

				timeDifference.setName(Steps.FIELD_CHANNELS_TYPES[POS_0]);

				timeDifference.setValues(values.get(POS_2));

				channelsData.add(timeDifference);

				channels.setData(channelsData);

				data.setChannels(channels);

				stepsDataList.add(data);
			}

			return stepsDataList;
		}

		private static ArrayList<ArrayList<Long>> generateValues(long timestamp, int seed) {
			long upperBound = DIFF_UPPER_BOUND;
			long lowerBound = DIFF_LOWER_BOUND;

			ArrayList<ArrayList<Long>> listValues = new ArrayList<ArrayList<Long>>();

			ArrayList<Long> timeValues = new ArrayList<Long>();
			ArrayList<Long> stepsValues = new ArrayList<Long>(Collections.nCopies(seed, ONE_LONG));
			ArrayList<Long> diffValues = new ArrayList<Long>();

			long pause = ZERO_LONG;

			long nextPauseTime;

			if (seed < 5000L) {
				nextPauseTime = 5000L;
			} else {
				nextPauseTime = ThreadLocalRandom.current().nextLong(seed / 5L, seed);
			}

			long current = timestamp;

			for (long i = 0; i < seed; i++) {
				if (timeValues.size() == 0) {
					timeValues.add(timestamp - current);
					diffValues.add(ZERO_LONG);
				} else {
					long diff = 0;

					if (timeValues.size() == 1) {
						upperBound = 1500L;
						lowerBound = 850L;
						diff = ThreadLocalRandom.current().nextLong(lowerBound, upperBound);
					} else {
						if ((i - pause) < nextPauseTime) {
							if (diffValues.get(diffValues.size() - 1) < 1000L) {
								upperBound = 1300L;
								lowerBound = 850L;
								if (diffValues.get(diffValues.size() - 1) < 950L) {
									upperBound = 750L;
									lowerBound = 350L;
								}
							}
							diff = ThreadLocalRandom.current().nextLong(lowerBound, upperBound);
						} else {
							diff = ThreadLocalRandom.current().nextLong(2000L, 5000L);
							upperBound = diffValues.get(diffValues.size() - 1) + 900L;
							lowerBound = diffValues.get(diffValues.size() - 1) - 900L;
							lowerBound = lowerBound < 0 ? 350 : lowerBound;
							pause = i;
							nextPauseTime = ThreadLocalRandom.current().nextLong(pause + (seed - pause) / 2L, seed);
						}
					}

					diffValues.add(diff);
					current += diff;
					timeValues.add(current - timestamp);
				}
			}

			listValues.add(timeValues);
			listValues.add(stepsValues);
			listValues.add(diffValues);
			return listValues;
		}

		private static void write(ArrayList<Steps> stepsData) {
			String fileName = "steps_";
			String ext = ".json";

			for (Steps data : stepsData) {
				try {
					FileWriter writer = new FileWriter(filePath + fileName + data.getTimestamp() + ext);

					// System.out.println(data.getTime().getValues());
					// System.out.println(data.getChannels().get(POS_1).getValues());
					// System.out.println();

					Gson gson = new Gson();
					gson.toJson(data, writer);

					writer.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
}
