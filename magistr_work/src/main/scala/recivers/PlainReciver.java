package recivers;

import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import settings.Constants;

public class PlainReciver extends Receiver<String> {

    Integer secondDelay;

    public PlainReciver() {
        super(StorageLevel.MEMORY_AND_DISK());
    }


    public PlainReciver(int secondDelay) {
        super(StorageLevel.MEMORY_AND_DISK());
        this.secondDelay = secondDelay;
    }

    private void receive() {
        while (true) {
            GetSimplePlainsArray reciver = new GetSimplePlainsArray();
            String[] data = reciver.getPlainsArray();
            for (int i = 1; i < data.length - 1; i++) {
                store(data[i]);
            }
            try {
                if (secondDelay != null) {
                    Thread.sleep(secondDelay * 1000);

                } else {
                    Thread.sleep(Constants.SPARK_REALTIME_HTTP_REQUEST_FREEQUENSY_SECONDS() * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        new Thread(this::receive).start();
    }

    @Override
    public void onStop() {

    }
}
