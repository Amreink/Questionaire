package tkapps.questionaire.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import tkapps.questionaire.interfaces.OnDirectorySelectedListener;
import tkapps.questionaire.interfaces.OnFileSelectedListener;

/**
 * Created by pascal.euhus on 02.02.2016.
 */
public class FileDialog {

    private static final String PARENT_DIR = "..";
    private final String TAG = getClass().getName();
    private final Context context;
    private String[] fileList;
    private File currentPath;
    private FileDialogListenerCaretaker<OnFileSelectedListener> fileListenerList = new FileDialogListenerCaretaker<>();
    private FileDialogListenerCaretaker<OnDirectorySelectedListener> dirListenerList = new FileDialogListenerCaretaker<>();
    private boolean selectDirectoryOption;
    private String fileEndsWith;
    public FileDialog(Context context, File path) {
        this.context = context;
        if (!path.exists()) path = Environment.getExternalStorageDirectory();
        loadFileList(path);
    }

    /**
     * @return file dialog
     */
    public Dialog createFileDialog() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(currentPath.getPath());
        if (selectDirectoryOption) {
            builder.setPositiveButton("Select directory", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, currentPath.getPath());
                    fireDirectorySelectedEvent(currentPath);
                }
            });
        }

        builder.setItems(fileList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String fileChosen = fileList[which];
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory()) {
                    loadFileList(chosenFile);
                    dialog.cancel();
                    dialog.dismiss();
                    showDialog();
                } else fireFileSelectedEvent(chosenFile);
            }
        });
        dialog = builder.show();
        dialog.setCancelable(false);
        return dialog;
    }

    public void addFileListener(OnFileSelectedListener listener) {
        fileListenerList.add(listener);
    }

    public void removeFileListener(OnFileSelectedListener listener) {
        fileListenerList.remove(listener);
    }

    public void setSelectDirectoryOption(boolean selectDirectoryOption) {
        this.selectDirectoryOption = selectDirectoryOption;
    }

    public void addDirectoryListener(OnDirectorySelectedListener listener) {
        dirListenerList.add(listener);
    }

    public void removeDirectoryListener(OnDirectorySelectedListener listener) {
        dirListenerList.remove(listener);
    }

    /**
     * Show file dialog
     */
    public void showDialog() {
        createFileDialog().show();
    }

    private void fireFileSelectedEvent(final File file) {
        fileListenerList.fireEvent(new FileDialogListenerCaretaker.FireHandler<OnFileSelectedListener>() {
            public void fireEvent(OnFileSelectedListener listener) {
                listener.onFileSelected(file);
            }
        });
    }

    private void fireDirectorySelectedEvent(final File directory) {
        dirListenerList.fireEvent(new FileDialogListenerCaretaker.FireHandler<OnDirectorySelectedListener>() {
            public void fireEvent(OnDirectorySelectedListener listener) {
                listener.onDirectorySelected(directory);
            }
        });
    }

    private void loadFileList(File path) {
        this.currentPath = path;
        List<String> foundFiles = new ArrayList<String>();
        if (path.exists()) {
            if (path.getParentFile() != null) foundFiles.add(PARENT_DIR);
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    if (!sel.canRead()) return false;
                    if (selectDirectoryOption) return sel.isDirectory();
                    else {
                        boolean endsWith = fileEndsWith != null ? filename.toLowerCase().endsWith(fileEndsWith) : true;
                        return endsWith || sel.isDirectory();
                    }
                }
            };
            String[] fileList1 = path.list(filter);
            for (String file : fileList1) {
                foundFiles.add(file);
            }
        }
        fileList =  foundFiles.toArray(new String[]{});
    }

    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(PARENT_DIR)) return currentPath.getParentFile();
        else return new File(currentPath, fileChosen);
    }

    public void setFileEndsWith(String fileEndsWith) {
        this.fileEndsWith = fileEndsWith != null ? fileEndsWith.toLowerCase() : fileEndsWith;
    }

}
