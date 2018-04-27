import java.time.LocalTime;

/**
 * Элемент для обработки
 *
 * Комментарии к дизайну:
 * 1. Не использую интерфейс, так как не предполагается множественная реализация элементов. Это было бы излишне и
 *    усложнило код.
 * 2. Поля с метаинформацией (whenWasProcessed, handler) не вынес в отдельный класс-обертку (WorkUnit), чтобы
 *    не усложнять восприятие кода.
 */
public class Item {
    private long id;
    private int groupId;
    private LocalTime whenWasProcessed;
    private Handler handler;

    public Item(long id, int groupId) {
        this.id = id;
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setProcessingInfo(Handler handler) {
        this.whenWasProcessed = LocalTime.now();
        this.handler = handler;
    }

    public LocalTime getWhenWasProcessed() {
        return whenWasProcessed;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", whenWasProcessed=" + whenWasProcessed +
                ", handler=" + ((handler != null) ? "[" + handler.getName() + "]" : "none") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        return id == item.id;

    }

    @Override
    public int hashCode() {
        return (int) id;
    }
}