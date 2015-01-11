/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.mt4j.audio;

import java.nio.FloatBuffer;

import org.mt4j.util.gdx.Array;
import org.mt4j.util.gdx.FileHandle;
import org.mt4j.util.gdx.IntArray;
import org.mt4j.util.gdx.IntMap;
import org.mt4j.util.gdx.LongMap;
import org.mt4j.util.gdx.ObjectMap;
import org.mt4j.util.math.ToolsBuffers;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.ALFactory;



/** @author Nathan Sweet */
public class OpenALAudio implements Audio {
	private final int deviceBufferSize;
	private final int deviceBufferCount;
	private IntArray idleSources, allSources;
	private LongMap<Integer> soundIdToSource;
	private IntMap<Long> sourceToSoundId;
	private long nextSoundId = 0;
	private ObjectMap<String, Class<? extends OpenALSound>> extensionToSoundClass = new ObjectMap();
	private ObjectMap<String, Class<? extends OpenALMusic>> extensionToMusicClass = new ObjectMap();

	Array<OpenALMusic> music = new Array(false, 1, OpenALMusic.class);
	boolean noDevice = false;
	
	private AL al;
	private ALC alc;
	private ALCdevice device;
	private ALCcontext context;

	public OpenALAudio () {
		this(16, 9, 512);
	}

	public OpenALAudio (int simultaneousSources, int deviceBufferCount, int deviceBufferSize) {
		this.deviceBufferSize = deviceBufferSize;
		this.deviceBufferCount = deviceBufferCount;

		registerSound("ogg", Ogg.Sound.class);
		registerMusic("ogg", Ogg.Music.class);
		registerSound("wav", Wav.Sound.class);
		registerMusic("wav", Wav.Music.class);
		registerSound("mp3", Mp3.Sound.class);
		registerMusic("mp3", Mp3.Music.class);
		
		try {
			//AL.create(); //FIXME
			 al = ALFactory.getAL();
			 alc = ALFactory.getALC();
			 device = alc.alcOpenDevice(null);
			 context = alc.alcCreateContext(device, null);
			 alc.alcMakeContextCurrent(context);
		} catch (Exception ex) {
			noDevice = true;
			ex.printStackTrace();
			return;
		}

		allSources = new IntArray(false, simultaneousSources);
		
		/*
		for (int i = 0; i < simultaneousSources; i++) {
			int sourceID = al.alGenSources();
			if (al.alGetError() != AL.AL_NO_ERROR) 
				break;
			allSources.add(sourceID);
		}
		*/
		
		for (int i = 0; i < simultaneousSources; i++) {
			// Sources are points emitting sound.
		    int[] source = new int[1];
		    al.alGenSources(1, source, 0);
			if (al.alGetError() != AL.AL_NO_ERROR) 
				break;
			allSources.add(source[0]);
		}
		
		
		
		idleSources = new IntArray(allSources);
		soundIdToSource = new LongMap<Integer>();
		sourceToSoundId = new IntMap<Long>();

		FloatBuffer orientation = (FloatBuffer)ToolsBuffers.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}).flip();
		al.alListenerfv(AL.AL_ORIENTATION, orientation);
		FloatBuffer velocity = (FloatBuffer)ToolsBuffers.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).flip();
		al.alListenerfv(AL.AL_VELOCITY, velocity);
		FloatBuffer position = (FloatBuffer)ToolsBuffers.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).flip();
		al.alListenerfv(AL.AL_POSITION, position);
	}

	public void registerSound (String extension, Class<? extends OpenALSound> soundClass) {
		if (extension == null) throw new IllegalArgumentException("extension cannot be null.");
		if (soundClass == null) throw new IllegalArgumentException("soundClass cannot be null.");
		extensionToSoundClass.put(extension, soundClass);
	}

	public void registerMusic (String extension, Class<? extends OpenALMusic> musicClass) {
		if (extension == null) throw new IllegalArgumentException("extension cannot be null.");
		if (musicClass == null) throw new IllegalArgumentException("musicClass cannot be null.");
		extensionToMusicClass.put(extension, musicClass);
	}

	public OpenALSound newSound (FileHandle file) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		Class<? extends OpenALSound> soundClass = extensionToSoundClass.get(file.extension());
		if (soundClass == null) throw new RuntimeException("Unknown file extension for sound: " + file);
		try {
			return soundClass.getConstructor(new Class[] {OpenALAudio.class, FileHandle.class}).newInstance(this, file);
		} catch (Exception ex) {
			throw new RuntimeException("Error creating sound " + soundClass.getName() + " for file: " + file, ex);
		}
	}

	public OpenALMusic newMusic (FileHandle file) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		Class<? extends OpenALMusic> musicClass = extensionToMusicClass.get(file.extension());
		if (musicClass == null) throw new RuntimeException("Unknown file extension for music: " + file);
		try {
			return musicClass.getConstructor(new Class[] {OpenALAudio.class, FileHandle.class}).newInstance(this, file);
		} catch (Exception ex) {
			throw new RuntimeException("Error creating music " + musicClass.getName() + " for file: " + file, ex);
		}
	}

	int obtainSource (boolean isMusic) {
		if (noDevice) 
			return 0;
		
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceId = idleSources.get(i);
			
//			int state = al.alGetSourcei(sourceId, AL.AL_SOURCE_STATE); //FIXME
			int[] bla = new int[1];
			al.alGetSourcei(sourceId, AL.AL_SOURCE_STATE, bla, 0);
			int state = bla[0]; 
			
			if (state != AL.AL_PLAYING && state != AL.AL_PAUSED) {
				if (isMusic) {
					idleSources.removeIndex(i);
				} else {
					if (sourceToSoundId.containsKey(sourceId)) {
						long soundId = sourceToSoundId.get(sourceId);
						sourceToSoundId.remove(sourceId);
						soundIdToSource.remove(soundId);
					}

					long soundId = nextSoundId++;
					sourceToSoundId.put(sourceId, soundId);
					soundIdToSource.put(soundId, sourceId);
				}
				al.alSourceStop(sourceId);
				al.alSourcei(sourceId, AL.AL_BUFFER, 0);
				al.alSourcef(sourceId, AL.AL_GAIN, 1);
				al.alSourcef(sourceId, AL.AL_PITCH, 1);
				al.alSource3f(sourceId, AL.AL_POSITION, 0, 0, 0);
				return sourceId;
			}
		}
		return -1;
	}

	void freeSource (int sourceID) {
		if (noDevice) return;
		
		al.alSourceStop(sourceID);
		al.alSourcei(sourceID, AL.AL_BUFFER, 0);
		
		if (sourceToSoundId.containsKey(sourceID)) {
			long soundId = sourceToSoundId.remove(sourceID);
			soundIdToSource.remove(soundId);
		}
		idleSources.add(sourceID);
	}

	void freeBuffer (int bufferID) {
		if (noDevice) return;
		
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceID = idleSources.get(i);
			
//			if (al.alGetSourcei(sourceID, AL.AL_BUFFER) == bufferID) { //FIXME
			int[] bla = new int[1];
			al.alGetSourcei(sourceID, AL.AL_BUFFER, bla, 0);
			int retrievedID = bla[0]; 
			if (retrievedID == bufferID) {
				
				if (sourceToSoundId.containsKey(sourceID)) {
					long soundId = sourceToSoundId.remove(sourceID);
					soundIdToSource.remove(soundId);
				}
				
				al.alSourceStop(sourceID);
				al.alSourcei(sourceID, AL.AL_BUFFER, 0);
			}
		}
		
	}

	void stopSourcesWithBuffer (int bufferID) {
		if (noDevice) return;
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceID = idleSources.get(i);

			int[] bla = new int[1];
			al.alGetSourcei(sourceID, AL.AL_BUFFER, bla, 0);
			int retrievedID = bla[0]; 
			if (retrievedID == bufferID) { //FIXME 
			//if (al.alGetSourcei(sourceID, AL.AL_BUFFER) == bufferID) { //FIXME 
				if (sourceToSoundId.containsKey(sourceID)) {
					long soundId = sourceToSoundId.remove(sourceID);
					soundIdToSource.remove(soundId);
				}
				al.alSourceStop(sourceID);
			}
		}
	}

	public void update () {
		if (noDevice) return;
		for (int i = 0; i < music.size; i++)
			music.items[i].update();
	}

	public long getSoundId (int sourceId) {
		if (!sourceToSoundId.containsKey(sourceId)) return -1;
		return sourceToSoundId.get(sourceId);
	}

	public void stopSound (long soundId) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);
		al.alSourceStop(sourceId);
	}

	public void setSoundGain (long soundId, float volume) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);
		al.alSourcef(sourceId, AL.AL_GAIN, volume);
	}

	public void setSoundLooping (long soundId, boolean looping) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);
		al.alSourcei(sourceId, AL.AL_LOOPING, looping ? AL.AL_TRUE : AL.AL_FALSE);
	}

	public void setSoundPitch (long soundId, float pitch) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);
		al.alSourcef(sourceId, AL.AL_PITCH, pitch);
	}

	public void setSoundPan (long soundId, float pan, float volume) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);

		al.alSource3f(sourceId, AL.AL_POSITION, pan, 0, 0);
		al.alSourcef(sourceId, AL.AL_GAIN, volume);
	}

	public void dispose () {
		if (noDevice) return;
		
		for (int i = 0, n = allSources.size; i < n; i++) {
			int sourceID = allSources.get(i);
			
			int[] bla = new int[1];
			al.alGetSourcei(sourceID, AL.AL_SOURCE_STATE, bla, 0);
			int state = bla[0]; 
//			int state = al.alGetSourcei(sourceID, AL.AL_SOURCE_STATE); //FIXME
			if (state != AL.AL_STOPPED) 
				al.alSourceStop(sourceID);
			
//			al.alDeleteSources(sourceID); //FIXME
			int[] sourceIDArr = new int[]{sourceID};
			al.alDeleteSources(1, sourceIDArr, 0);
		}

		sourceToSoundId.clear();
		soundIdToSource.clear();

//		AL.destroy();
//		while (AL.isCreated()) {
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//			}
//		}
		
		alc.alcMakeContextCurrent(null);
		alc.alcDestroyContext(context);
		alc.alcCloseDevice(device);
	}

	public AudioDevice newAudioDevice (int sampleRate, final boolean isMono) {
		if (noDevice) return new AudioDevice() {
			@Override
			public void writeSamples (float[] samples, int offset, int numSamples) {
			}

			@Override
			public void writeSamples (short[] samples, int offset, int numSamples) {
			}

			@Override
			public void setVolume (float volume) {
			}

			@Override
			public boolean isMono () {
				return isMono;
			}

			@Override
			public int getLatency () {
				return 0;
			}

			@Override
			public void dispose () {
			}
		};
		return new OpenALAudioDevice(this, sampleRate, isMono, deviceBufferSize, deviceBufferCount);
	}

	public AudioRecorder newAudioRecorder (int samplingRate, boolean isMono) {
		if (noDevice) return new AudioRecorder() {
			@Override
			public void read (short[] samples, int offset, int numSamples) {
			}

			@Override
			public void dispose () {
			}
		};
		return new JavaSoundAudioRecorder(samplingRate, isMono);
	}

	public AL getAL() {
		return this.al;
	}
	
}
