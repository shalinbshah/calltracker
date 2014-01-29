package com.call.tracker.voicenotes;

import java.io.File;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.customview.ButtonRoboto;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.VoiceNotesModel;
import com.devspark.appmsg.AppMsg;

public class VoiceDetailsActivity extends BaseActivity {
	String voiceTime = "", path = "";

	TextView textViewTime, textViewUr;

	MediaPlayer mPlayer = new MediaPlayer();
	Button butUrgent, butAssigncontact;
	ButtonRoboto buttonPlaySound;
	private VoiceNotesModel modelNotes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_voice_details);
		initControl();
	}

	private void initControl() {
		Bundle bundle = getIntent().getExtras();

		butAssigncontact = (Button) findViewById(R.id.butAssigncontact);
		buttonPlaySound = (ButtonRoboto) findViewById(R.id.buttonPlaySound);
		if (bundle != null) {
			if (bundle.containsKey("data")) {
				modelNotes = (VoiceNotesModel) bundle.getSerializable("data");
				buttonPlaySound.setText(modelNotes.getDateTime());
				voiceTime = modelNotes.getVoice_time();
				path = modelNotes.getVoice_path();
			} else {
				voiceTime = bundle.getString("time");
				path = bundle.getString("filepath");
			}
		}

		textViewTime = (TextView) findViewById(R.id.textViewTime);
		textViewTime.setText(voiceTime);

		textViewUr = (TextView) findViewById(R.id.textViewUr);

		butUrgent = (Button) findViewById(R.id.butUrgent);
		butUrgent.setTag("1");
		butUrgent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getTag().toString().equals("1")) {
					butUrgent.setBackgroundResource(R.drawable.icon_alert_red);
					butUrgent.setTag("2");
					textViewUr
							.setText(R.string.this_voice_note_is_marked_as_urgent);
					AppMsg appMsg = AppMsg
							.makeText(VoiceDetailsActivity.this,
									"This Voice Note is Now Urgent",
									AppMsg.STYLE_ALERT);
					appMsg.setLayoutGravity(Gravity.BOTTOM);
					appMsg.show();
				} else {
					butUrgent.setTag("1");
					butUrgent.setBackgroundResource(R.drawable.icon_alert_grey);
					AppMsg appMsg = AppMsg.makeText(VoiceDetailsActivity.this,
							"This Voice Note is Now Not Urgent",
							AppMsg.STYLE_ALERT);
					appMsg.setLayoutGravity(Gravity.BOTTOM);
					appMsg.show();
					textViewUr
							.setText(R.string.this_voice_note_has_not_been_marked_as_urgent);
				}
			}
		});

		if (getIntent().getExtras().containsKey("data"))
			updateView();
	}

	private void updateView() {
		butAssigncontact.setVisibility(View.INVISIBLE);
		String tag = modelNotes.getUrgent();
		if (tag.equals("2")) {
			butUrgent.setBackgroundResource(R.drawable.icon_alert_red);
			butUrgent.setTag("2");
			textViewUr.setText(R.string.this_voice_note_is_marked_as_urgent);
		} else {
			butUrgent.setTag("1");
			butUrgent.setBackgroundResource(R.drawable.icon_alert_grey);
			textViewUr
					.setText(R.string.this_voice_note_has_not_been_marked_as_urgent);
		}

	}

	public void callShare(View v) {
		String fileName = path;
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("audio/mp3");
		shareIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.fromFile(new File(fileName)));
		startActivity(Intent.createChooser(shareIntent, "Share File"));
	}

	public void callAssigncontact(View v) {
		Intent intent = new Intent(getApplicationContext(),
				AssignContactToVoiceNoteActivity.class);
		intent.putExtra("type", "voice");
		updatePref(FILEPATH, path);
		updatePref(VOICE_TIME, voiceTime);
		updatePref(URGENT, butUrgent.getTag().toString());
		startActivity(intent);
	}

	public void callDelete(View v) {
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.openDB(getApplicationContext());
		adapter.openDataBase();
		ContentValues values = new ContentValues();
		values.put("urgent", butUrgent.getTag().toString());
		Bundle bundle = getIntent().getExtras();
		VoiceNotesModel note = (VoiceNotesModel) bundle.get("data");
		adapter.getMyDatabase().delete("tbl_voice_note", "id=?",
				new String[] { Integer.toString(note.getId()) });
		adapter.close();
		finish();
	}

	public void callSave(View v) {
		// Update voice note
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.openDB(getApplicationContext());
		adapter.openDataBase();
		ContentValues values = new ContentValues();
		values.put("urgent", butUrgent.getTag().toString());
		Bundle bundle = getIntent().getExtras();
		VoiceNotesModel note = (VoiceNotesModel) bundle.get("data");
		adapter.getMyDatabase().update("tbl_voice_note", values,
				"id='" + note.getId() + "'", null);
		adapter.close();
		finish();
	}

	public void callPlaySound(View v) {
		if (mPlayer.isPlaying()) {
			mPlayer.stop(); // stop recording
			mPlayer.reset(); // set state to idle
		}
		try {
			mPlayer.reset();
			mPlayer.setDataSource(path);
			mPlayer.prepare();
			mPlayer.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
