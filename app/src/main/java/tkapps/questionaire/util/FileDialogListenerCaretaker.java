package tkapps.questionaire.util;

import java.util.ArrayList;
import java.util.List;


public class FileDialogListenerCaretaker<T> {
    private List<T> listenerList = new ArrayList<T>();

    public void add(T listener) {
        listenerList.add(listener);
    }

    public void fireEvent(FireHandler<T> fireHandler) {
        List<T> copy = new ArrayList<T>(listenerList);
        for (T t : copy) {
            fireHandler.fireEvent(t);
        }
    }

    public void remove(T listener) {
        listenerList.remove(listener);
    }

    public List<T> getListenerList() {
        return listenerList;
    }

    public interface FireHandler<L> {
        void fireEvent(L listener);
    }
}
