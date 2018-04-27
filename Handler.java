//import javax.inject.Inject;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Handler extends Thread {
    Dispatcher dispatcher;
    List<Item> processedItems = new ArrayList<>();

 // @Inject
    public Handler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        System.out.println(LocalTime.now() + " [" + getName() + "] запущен.");
        List<Item> items;
        do {
            items = dispatcher.getNextItems();
            try {
                for (Item item : items) {
                    processItem(item);
                }
            } finally {
                if (!items.isEmpty()) {
                    dispatcher.unlockGroup();
                }
            }
        } while (!items.isEmpty() || dispatcher.hasItems());
        System.out.println(LocalTime.now() + " [" + getName() + "] завершил работу.");
    }

    private void processItem(Item item) {
        item.setProcessingInfo(this);
        System.out.println(LocalTime.now() + " [" + getName() + "] processed " + item.toString() + ".");
        processedItems.add(item);
    }

    public List<Item> getProcessedItems() {
        return processedItems;
    }
}