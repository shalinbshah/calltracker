package com.call.tracker.voicenotes;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class AudioRecorder {

	MediaRecorder recorder = new MediaRecorder();
	private final String path;

	/**
	 * Creates a new audio recording at the given path (relative to root of SD
	 * card).
	 */
	public AudioRecorder(String path) {
		this.path = sanitizePath(path);
	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.contains(".")) {
			path += ".mp3";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/callTracker" + path;
	}

	/**
	 * Starts a new recording.
	 */
	public void start() {
		try {
			String state = android.os.Environment.getExternalStorageState();
			if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
				throw new IOException("SD Card is not mounted.  It is " + state
						+ ".");
			}

			// make sure the directory we plan to store the recording in exists
			File directory = new File(getPath()).getParentFile();
			if (!directory.exists() && !directory.mkdirs()) {
				throw new IOException("Path to file could not be created.");
			}

			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(getPath());
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops a recording that has been previously started.
	 */
	public void stop() {
		recorder.stop(); // stop recording
		recorder.reset(); // set state to idle
		recorder.release(); // release resources back to the system
		recorder = null;
	}

	public String getPath() {
		return path;
	}

}