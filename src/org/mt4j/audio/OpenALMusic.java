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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.mt4j.util.gdx.FileHandle;
import org.mt4j.util.math.ToolsBuffers;

import com.jogamp.openal.AL;


/** @author Nathan Sweet */
public abstract class OpenALMusic implements Music {
	static private final int bufferSize = 4096 * 10;
//	static private final int bufferCount = 3;
	static private final int bufferCount = 6;
	static private final int bytesPerSample = 2;
	static private final byte[] tempBytes = new byte[bufferSize];
	static private final ByteBuffer tempBuffer = ToolsBuffers.createByteBuffer(bufferSize);

	private final OpenALAudio audio;
	private IntBuffer buffers;
	private int sourceID = -1;
	private int format, sampleRate;
	private boolean isLooping, isPlaying;
	private float volume = 1;
	private float renderedSeconds, secondsPerBuffer;

	protected final FileHandle file;
	private AL al;

	public OpenALMusic (OpenALAudio audio, FileHandle file) {
		this.audio = audio;
		this.al = audio.getAL();
		this.file = file;
		
		if (audio != null) {
			if (!audio.noDevice) 
				audio.music.add(this);
		}
	}

	protected void setup (int channels, int sampleRate) {
		this.format = channels > 1 ? AL.AL_FORMAT_STEREO16 : AL.AL_FORMAT_MONO16;
		this.sampleRate = sampleRate;
		secondsPerBuffer = (float)bufferSize / bytesPerSample / channels / sampleRate;
	}

	public void play () {
		if (audio.noDevice) return;
		if (sourceID == -1) {
			sourceID = audio.obtainSource(true);
			
			if (sourceID == -1) 
				return;
			
			if (buffers == null) {
				buffers = ToolsBuffers.createIntBuffer(bufferCount);
				
//				al.alGenBuffers(buffers); //FIXME
				al.alGenBuffers(bufferCount, buffers);
				
				if (al.alGetError() != AL.AL_NO_ERROR) 
					throw new RuntimeException("Unabe to allocate audio buffers.");
			}
			
			al.alSourcei(sourceID, AL.AL_LOOPING, AL.AL_FALSE);
			al.alSourcef(sourceID, AL.AL_GAIN, volume);
			
			for (int i = 0; i < bufferCount; i++) {
				int bufferID = buffers.get(i);
				if (!fill(bufferID)) 
					break;
				
//				al.alSourceQueueBuffers(sourceID, bufferID); //FIXME
				int[] bufferIDArr = new int[]{bufferID};
				al.alSourceQueueBuffers(sourceID, 1, bufferIDArr, 0);
			}
			
			if (al.alGetError() != AL.AL_NO_ERROR) {
				stop();
				return;
			}
		}
		al.alSourcePlay(sourceID);
		isPlaying = true;
	}

	public void stop () {
		if (audio.noDevice) return;
		if (sourceID == -1) return;
		reset();
		audio.freeSource(sourceID);
		sourceID = -1;
		renderedSeconds = 0;
		isPlaying = false;
	}

	public void pause () {
		if (audio.noDevice) 
			return;
		if (sourceID != -1) 
			al.alSourcePause(sourceID);
		isPlaying = false;
	}

	public boolean isPlaying () {
		if (audio.noDevice) 
			return false;
		
		if (sourceID == -1) 
			return false;
		return isPlaying;
	}

	public void setLooping (boolean isLooping) {
		this.isLooping = isLooping;
	}

	public boolean isLooping () {
		return isLooping;
	}

	public void setVolume (float volume) {
		this.volume = volume;
		if (audio.noDevice) 
			return;
		if (sourceID != -1) 
			al.alSourcef(sourceID, AL.AL_GAIN, volume);
	}

	public float getPosition () {
		if (audio.noDevice) 
			return 0;
		
		if (sourceID == -1) 
			return 0;
		
//		return renderedSeconds + al.alGetSourcef(sourceID, AL.AL_SEC_OFFSET); //FIXME
		int[] secOffset = new int[1];
		al.alGetSourcei(sourceID, AL.AL_SEC_OFFSET, secOffset, 0);
		return renderedSeconds + secOffset[0];
	}

	/** Fills as much of the buffer as possible and returns the number of bytes filled. Returns <= 0 to indicate the end of the
	 * stream. */
	abstract public int read (byte[] buffer);

	/** Resets the stream to the beginning. */
	abstract public void reset ();

	public int getChannels () {
		return format == AL.AL_FORMAT_STEREO16 ? 2 : 1;
	}

	public int getRate () {
		return sampleRate;
	}

	public void update () {
		if (audio.noDevice) 
			return;
		
		if (sourceID == -1) 
			return;

		boolean end = false;
//		int buffers = al.alGetSourcei(sourceID, AL.AL_BUFFERS_PROCESSED); //FIXME
		int[] processedBuffersArr = new int[1];
		al.alGetSourcei(sourceID, AL.AL_BUFFERS_PROCESSED, processedBuffersArr, 0);
		int buffers = processedBuffersArr[0]; 
//		/*
		while (buffers-- > 0) {
//			int bufferID = al.alSourceUnqueueBuffers(sourceID); //FIXME
			int[] removedBufferID = new int[1];
			al.alSourceUnqueueBuffers(sourceID, 1, removedBufferID, 0);
			int bufferID = removedBufferID[0];
			
			if (bufferID == AL.AL_INVALID_VALUE) 
				break;
			
			renderedSeconds += secondsPerBuffer;
			
			if (end) 
				continue;
			
//			if (fill(bufferID))
//				al.alSourceQueueBuffers(sourceID, bufferID); //FIXME
//			else
//				end = true;
			
			if (fill(bufferID)){
				int[] bufferIDArr = new int[]{bufferID};
				al.alSourceQueueBuffers(sourceID, 1, bufferIDArr, 0);
			}else{
				end = true;
			}
		}
//		*/
		
//		if (end && al.alGetSourcei(sourceID, AL.AL_BUFFERS_QUEUED) == 0)  //FIXME
		int[] bufferQueued = new int[1];
		al.alGetSourcei(sourceID, AL.AL_BUFFERS_QUEUED, bufferQueued, 0);
		if (end && bufferQueued[0] == 0)
			stop();

		// A buffer underflow will cause the source to stop.
//		if (isPlaying && al.alGetSourcei(sourceID, AL.AL_SOURCE_STATE) != AL.AL_PLAYING) //FIXME
		int[] sourceState = new int[1];
		al.alGetSourcei(sourceID, AL.AL_SOURCE_STATE, sourceState, 0);
		if (isPlaying && sourceState[0] != AL.AL_PLAYING)
			al.alSourcePlay(sourceID);
	}

	private boolean fill (int bufferID) {
		tempBuffer.clear();
		int length = read(tempBytes);
		if (length <= 0) {
			if (isLooping) {
				reset();
				renderedSeconds = 0;
				length = read(tempBytes);
				if (length <= 0) return false;
			} else
				return false;
		}
		tempBuffer.put(tempBytes, 0, length).flip();
		
//		al.alBufferData(bufferID, format, tempBuffer, sampleRate); //FIXME
		al.alBufferData(bufferID, format, tempBuffer, tempBuffer.limit(), sampleRate);
		
		return true;
	}

	public void dispose () {
		if (audio.noDevice) 
			return;
		
		if (buffers == null) 
			return;
		
		if (sourceID != -1) {
			reset();
			audio.music.removeValue(this, true);
			audio.freeSource(sourceID);
			sourceID = -1;
		}
		
//		al.alDeleteBuffers(buffers);
		al.alDeleteBuffers(buffers.limit(), buffers);
		
		buffers = null;
	}
}
