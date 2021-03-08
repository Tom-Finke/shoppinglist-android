package it.niedermann.owncloud.notes;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import it.niedermann.owncloud.notes.main.MainActivity;
import it.niedermann.owncloud.notes.shared.model.DBNote;
import it.niedermann.owncloud.notes.shared.util.ShareUtil;

public class AppendToNoteActivity extends MainActivity {

    private static final String TAG = AppendToNoteActivity.class.getSimpleName();

    String receivedText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivedText = ShareUtil.extractSharedText(getIntent());
        @Nullable final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(R.string.append_to_note);
        } else {
            Log.e(TAG, "SupportActionBar is null. Expected toolbar to be present to set a title.");
        }
        binding.activityNotesListView.toolbar.setSubtitle(receivedText);
    }

    @Override
    public void onNoteClick(int position, View v) {
        if (!TextUtils.isEmpty(receivedText)) {
            final DBNote note = db.getNote(localAccount.getId(), ((DBNote) adapter.getItem(position)).getId());
            final String oldContent = note.getContent();
            String newContent;
            if (oldContent != null && oldContent.length() > 0) {
                newContent = oldContent + "\n\n" + receivedText;
            } else {
                newContent = receivedText;
            }
            db.updateNoteAndSync(ssoAccount, localAccount, note, newContent, () -> Toast.makeText(this, getString(R.string.added_content, receivedText), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, R.string.shared_text_empty, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public boolean onNoteLongClick(int position, View v) {
        return false;
    }
}
