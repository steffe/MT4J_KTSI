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

import org.mt4j.util.math.ToolsBuffers;
import org.mt4j.util.math.ToolsMath;

import com.jogamp.openal.AL;


/** @author Nathan Sweet */
public class OpenALAudioDevice implements AudioDevice {
	static private final int bytesPerSample = 2;

	private final OpenALAudio audio;
	private final int channels;
	private IntBuffer buffers;
	private int sourceID = -1;
	private int format, sampleRate;
	private boolean isPlaying;
	private float volume = 1;
	private float renderedSeconds, secondsPerBuffer;
	private byte[] bytes;
	private final int bufferSize;
	private final int bufferCount;
	private final ByteBuffer tempBuffer;

	private AL al;
	private int[] removedBufferID;
	private int[] queueBufferIDArr;
	private int[] sourcePlayStateArr;
	
	public OpenALAudioDevice (OpenALAudio audio, int sampleRate, boolean isMono, int bufferSize, int bufferCount) {
		this.audio = audio;
		this.al = audio.getAL();
		channels = isMono ? 1 : 2;
		this.bufferSize = bufferSize;
		this.bufferCount = bufferCount;
		this.format = channels > 1 ? AL.AL_FORMAT_STEREO16 : AL.AL_FORMAT_MONO16;
		this.sampleRate = sampleRate;
		secondsPerBuffer = (float)bufferSize / bytesPerSample / channels / sampleRate;
		tempBuffer = ToolsBuffers.createByteBuffer(bufferSize);
		
		removedBufferID = new int[1];
		queueBufferIDArr = new int[1];
		sourcePlayStateArr = new int[1];
	}

	public void writeSamples (short[] samples, int offset, int numSamples) {
		if (bytes == null || bytes.length < numSamples * 2) bytes = new byte[numSamples * 2];
		for (int i = offset, ii = 0; i < numSamples; i++) {
			short sample = samples[i];
			bytes[ii++] = (byte)(sample & 0xFF);
			bytes[ii++] = (byte)((sample >> 8) & 0xFF);
		}
		writeSamples(bytes, 0, numSamples * 2);
	}

	public void writeSamples (float[] samples, int offset, int numSamples) {
		if (bytes == null || bytes.length < numSamples * 2) bytes = new byte[numSamples * 2];
		for (int i = offset, ii = 0; i < numSamples; i++) {
			float floatSample = samples[i];
			floatSample = ToolsMath.clamp(floatSample, -1f, 1f);
			int intSample = (int)(floatSample * 32767);
			bytes[ii++] = (byte)(intSample & 0xFF);
			bytes[ii++] = (byte)((intSample >> 8) & 0xFF);
		}
		writeSamples(bytes, 0, numSamples * 2);
	}

	public void writeSamples (byte[] data, int offset, int length) {
		if (length < 0) throw new IllegalArgumentException("length cannot be < 0.");

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
			
			// Fill initial buffers.
			int queuedBuffers = 0;
			
			for (int i = 0; i < bufferCount; i++) {
				int bufferID = buffers.get(i);
				int written = Math.min(bufferSize, length);
				tempBuffer.clear();
				tempBuffer.put(data, offset, written).flip();
				
//				al.alBufferData(bufferID, format, tempBuffer, sampleRate); //FIXME
				al.alBufferData(bufferID, format, tempBuffer, tempBuffer.limit(), sampleRate);
				
//				al.alSourceQueueBuffers(sourceID, bufferID); //FIXME
				int[] bla = new int[]{bufferID};
				al.alSourceQueueBuffers(sourceID, 1, bla, 0);
				
				length -= written;
				offset += written;
				queuedBuffers++;
			}
			
			// Queue rest of buffers, empty.
			tempBuffer.clear().flip();
			
			for (int i = queuedBuffers; i < bufferCount; i++) {
				int bufferID = buffers.get(i);
				
//				al.alBufferData(bufferID, format, tempBuffer, sampleRate); //FIXME
//				al.alSourceQueueBuffers(sourceID, bufferID);	//FIXME
				
				al.alBufferData(bufferID, format, tempBuffer, tempBuffer.limit(), sampleRate);
				int[] bla = new int[]{bufferID};
				al.alSourceQueueBuffers(sourceID, 1, bla, 0);
			}
			
			al.alSourcePlay(sourceID);
			isPlaying = true;
		}

		while (length > 0) {
			int written = fillBuffer(data, offset, length);
			length -= written;
			offset += written;
		}
	}

	/** Blocks until some of the data could be buffered. */
	private int fillBuffer (byte[] data, int offset, int length) {
		int written = Math.min(bufferSize, length);

		outer:
		while (true) {
//			int buffers = al.alGetSourcei(sourceID, AL.AL_BUFFERS_PROCESSED); //FIXME
			int[] bla = new int[1];
			al.alGetSourcei(sourceID, AL.AL_BUFFERS_PROCESSED, bla, 0);
			int buffers = bla[0]; 
			
			while (buffers-- > 0) {
//				int bufferID = al.alSourceUnqueueBuffers(sourceID); //FIXME
//				int[] removedBufferID = new int[]{sourceID};
				removedBufferID[0] = sourceID;
				al.alSourceUnqueueBuffers(sourceID, 1, removedBufferID, 0);
				int bufferID = removedBufferID[0];
				
				if (bufferID == AL.AL_INVALID_VALUE) 
					break;
				
				renderedSeconds += secondsPerBuffer;

				tempBuffer.clear();
				tempBuffer.put(data, offset, written).flip();
				
//				al.alBufferData(bufferID, format, tempBuffer, sampleRate); //FIXME
//				al.alSourceQueueBuffers(sourceID, bufferID); //FIXME
				
				al.alBufferData(bufferID, format, tempBuffer, tempBuffer.limit(), sampleRate);
//				int[] queueBufferIDArr = new int[]{bufferID};
				queueBufferIDArr[0] = bufferID;
				al.alSourceQueueBuffers(sourceID, 1, queueBufferIDArr, 0);
				
				break outer;
			}
			
			// Wait for buffer to be free.
			try {
				Thread.sleep((long)(1000 * secondsPerBuffer / bufferCount));
			} catch (InterruptedException ignored) {
			}
			
		}

		// A buffer underflow will cause the source to stop.
//		if (!isPlaying || al.alGetSourcei(sourceID, AL.AL_SOURCE_STATE) != AL.AL_PLAYING) { //FIXME
//		int[] sourcePlayStateArr = new int[1];
		al.alGetSourcei(sourceID, AL.AL_SOURCE_STATE, sourcePlayStateArr, 0);
		if (!isPlaying || sourcePlayStateArr[0] != AL.AL_PLAYING) {
			al.alSourcePlay(sourceID);
			isPlaying = true;
		}

		return written;
	}

	public void stop () {
		if (sourceID == -1) return;
		audio.freeSource(sourceID);
		sourceID = -1;
		renderedSeconds = 0;
		isPlaying = false;
	}

	public boolean isPlaying () {
		if (sourceID == -1) return false;
		return isPlaying;
	}

	public void setVolume (float volume) {
		this.volume = volume;
		if (sourceID != -1) al.alSourcef(sourceID, AL.AL_GAIN, volume);
	}

	public float getPosition () {
		if (sourceID == -1) 
			return 0;
		
		int[] secOffset = new int[1];
		al.alGetSourcei(sourceID, AL.AL_SEC_OFFSET, secOffset, 0);
		
//		return renderedSeconds + al.alGetSourcef(sourceID, AL.AL_SEC_OFFSET);
		return renderedSeconds + secOffset[0];
	}

	public void setPosition (float position) {
		renderedSeconds = position;
	}

	public int getChannels () {
		return format == AL.AL_FORMAT_STEREO16 ? 2 : 1;
	}

	public int getRate () {
		return sampleRate;
	}

	public void dispose () {
		if (buffers == null) return;
		if (sourceID != -1) {
			audio.freeSource(sourceID);
			sourceID = -1;
		}
//		al.alDeleteBuffers(buffers); //FIXME
		al.alDeleteBuffers(buffers.limit(), buffers);
		
		buffers = null;
	}

	public boolean isMono () {
		return channels == 1;
	}

	public int getLatency () {
		return (int)(secondsPerBuffer * bufferCount * 1000);
	}
}

