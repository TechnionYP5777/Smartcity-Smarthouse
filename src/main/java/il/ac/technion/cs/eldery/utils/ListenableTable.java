package il.ac.technion.cs.eldery.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** @param <T> type of the column names of this table
 * @param <S> type of the data stored in this table
 * @author Sharon
 * @since 17.12.16 */
public class ListenableTable<T, S> extends Table<T, S> {
    @SuppressWarnings("unused") private List<Consumer<ListenableTable<T, S>>> listeners = new ArrayList<>();
}
