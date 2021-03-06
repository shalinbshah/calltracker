package com.call.tracker.voicenotes;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
	Button butUrgent, butAssigncontact, butSave;
	ImageView buttonPlaySound;
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
		buttonPlaySound = (ImageView) findViewById(R.id.buttonPlaySound);
		butSave = (ButtonRoboto) findViewById(R.id.btnSave);
		if (bundle != null) {
			if (bundle.containsKey("data")) {
				modelNotes = (VoiceNotesModel) bundle.getSerializable("data");
				voiceTime = modelNotes.getDateTime() + " / "
						+ modelNotes.getVoice_time();
				path = modelNotes.getVoice_path();
			} else {
				String currentDateTimeString = DateFormat.getDateTimeInstance()
						.format(new Date());

				voiceTime = currentDateTimeString + " / "
						+ bundle.getString("time");
				path = bundle.getString("filepath");
			}
		}

		textViewTime = (TextView) findViewById(R.id.textViewTime);
		textViewTime.setText(voiceTime);

		textViewUr = (TextView) findViewById(R.id.textViewUr);

		butUrgent = (Button) findViewById(R.id.butUrgent);
		butUrgent.setTag("0");
		butUrgent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getTag().toString().equals("0")) {
					butUrgent.setBackgroundResource(R.drawable.icon_alert_red);
					butUrgent.setTag(1);
					textViewUr
							.setText(R.string.this_voice_note_is_marked_as_urgent);
					AppMsg appMsg = AppMsg.makeText(VoiceDetailsActivity.this,
							"This Voice Note is Urgent", AppMsg.STYLE_ALERT);
					appMsg.setLayoutGravity(Gravity.BOTTOM);
					appMsg.setDuration(AppMsg.LENGTH_SHORT);
					appMsg.show();
				} else {
					butUrgent.setTag(0);
					butUrgent.setBackgroundResource(R.drawable.icon_alert_grey);
					AppMsg appMsg = AppMsg.makeText(VoiceDetailsActivity.this,
							"This Voice Note is Not Urgent", AppMsg.STYLE_INFO);
					appMsg.setLayoutGravity(Gravity.BOTTOM);
					appMsg.setDuration(AppMsg.LENGTH_SHORT);
					appMsg.show();
					textViewUr
							.setText(R.string.this_voice_note_has_not_been_marked_as_urgent);
				}
			}
		});

		if (getIntent().getExtras().containsKey("data")) {
			updateView();
		} else {
			butSave.setEnabled(false);
		}
	}

	private void updateView() {
		butAssigncontact.setVisibility(View.INVISIBLE);
		int tag = modelNotes.getUrgent();
		if (tag == 1) {
			butUrgent.setBackgroundResource(R.drawable.icon_alert_red);
			butUrgent.setTag(1);
			textViewUr.setText(R.string.this_voice_note_is_marked_as_urgent);
		} else {
			butUrgent.setTag(0);
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
				SelectGroupVoiceNoteActivity.class);
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
		Bundle bundle = getIntent().getExtras();
		VoiceNotesModel note = (VoiceNotesModel) bundle.get("data");
		if (null != note) {
			try {
				adapter.getMyDatabase().delete("tbl_voice_notes", "id=?",
						new String[] { Integer.toString(note.getId()) });
			} catch (Exception e) {
			}
		}
		adapter.close();
		finish();
	}

	public void callSave(View v) {
		// Update voice note
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.openDB(getApplicationContext());
		adapter.openDataBase();
		ContentValues values = new ContentValues();
		values.put("urgent", Integer.parseInt(butUrgent.getTag().toString()));
		Bundle bundle = getIntent().getExtras();
		VoiceNotesModel note = (VoiceNotesModel) bundle.get("data");
		adapter.getMyDatabase().update("tbl_voice_notes", values,
				"id='" + note.getId() + "'", null);
		adapter.close();
		finish();
	}

	public void callPlaySound(View v) {
		((ImageView) v).setImageResource(R.drawable.media_play);
		if (mPlayer.isPlaying()) {
			mPlayer.stop(); // stop recording
			mPlayer.reset(); // set state to idle
			((ImageView) v).setImageResource(R.drawable.media_play);
		}
		try {
			((ImageView) v).setImageResource(R.drawable.media_stop);
			mPlayer.reset();
			mPlayer.setDataSource(path);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					buttonPlaySound.setImageResource(R.drawable.media_play);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
