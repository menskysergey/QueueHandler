import sun.plugin.com.Dispatcher;

import java.util.*;
import java.util.logging.Handler;

public class Application {

    private static Dispatcher dispatcher;
    private static List<Handler> handlers = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Главный поток запущен.");

        Map<String, String> argMap = new HashMap<>();
        Arrays.asList(args).forEach(value -> argMap.put(value.split(":")[0], value.split(":")[1]));

        int groupsNum = Integer.decode(argMap.get("groups"));
        int handlersNum = Integer.decode(argMap.get("handlers"));
        long itemsNum = Integer.decode(argMap.get("items"));
        int packetSize = Integer.decode(argMap.get("packetSize"));

        dispatcher =  new Dispatcher(groupsNum, packetSize);
        (new Thread() {
            @Override
            public void run() {
                fillItems(groupsNum, itemsNum);
            }
        }).start();

        // Запустим обработчики.
        int i = 0;
        while (i++ < handlersNum) {
            handlers.add(new Handler(dispatcher));
        }
        handlers.parallelStream().forEach(Handler::start);

        // Ждем, когда завершат работу все обработчики.
        for (Handler handler : handlers) {
            try {
                handler.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Главный поток завершен");
    }

    private static void fillItems(int groupsNum, long itemsNum) {
        Random random = new Random();
        for (long i = 0; i < itemsNum; i++) {
            Item item = new Item(i, random.nextInt(groupsNum));
            dispatcher.addItem(item);
            System.out.println(LocalTime.now() + " [" + Thread.currentThread().getName() + "] " + item + " added to queue");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        dispatcher.finish();
    }

    public static SortedSet<Item> getProcessedItems() {
        Comparator<Item> comparator = (o1, o2) -> {
            if (o1.getWhenWasProcessed().isBefore(o2.getWhenWasProcessed())) {
                return -1;
            } else {
                return 1;
            }
        };
        SortedSet<Item> result = new TreeSet<>(comparator);

        for (Handler handler : handlers) {
            result.addAll(handler.getProcessedItems());
        }

        return result;
    }

}
