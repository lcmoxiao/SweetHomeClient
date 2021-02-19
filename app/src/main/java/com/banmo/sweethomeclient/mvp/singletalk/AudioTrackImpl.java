package com.banmo.sweethomeclient.mvp.singletalk;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.ByteArrayInputStream;

abstract class AudioTrackImpl {
    private static final String TAG = "AudioTrackImpl";
    AudioTrack audioTrack;
    //配置播放器
    int sampleRate = 44100;//录音时采用的采样频率，采用相同的
    int channelConfig = AudioFormat.CHANNEL_OUT_MONO;//mono表示单声道，播放采用输出单声道
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    final int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, audioFormat);
    int mode = AudioTrack.MODE_STREAM;//流模式

    private boolean isPause;

    private Thread playVoiceThread = null;
    private byte[] data;

    public void setData(byte[] data) {
        this.data = data;
    }

    // ************ 流播放 ************
    public void firstPlay() {

        //首次点击播放才会进来，如果多次点击播放，则不会做任何事情
        if (playVoiceThread == null) {
            playVoiceThread = new Thread(() -> {
                //设置线程优先级，android.os.Process.THREAD_PRIORITY_AUDIO-标准音乐播放使用的线程优先级
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

                // 创建 AudioTrack 对象
                audioTrack = new AudioTrack(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build(),
                        new AudioFormat.Builder().setSampleRate(sampleRate)
                                .setEncoding(audioFormat)
                                .setChannelMask(channelConfig)
                                .build(),
                        minBufferSize,
                        mode,
                        AudioManager.AUDIO_SESSION_ID_GENERATE
                );
                Log.e(TAG, "firstPlay: source" + data.length);
                int markInFrames = data.length / minBufferSize;
                Log.e(TAG, "play: " + markInFrames + "source.length" + data.length + " + minBufferSize :" + minBufferSize);
                // 检查初始化是否成功
                if (audioTrack.getState() == AudioTrack.STATE_UNINITIALIZED) {
                    Log.e(TAG, "doPlay: false");
                    return;
                }
                // 播放
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                    byte[] buffer = new byte[minBufferSize];
                    int readCount;
                    while (inputStream.available() > 0) {
                        if (isPause) {
                            continue;
                        }
                        readCount = inputStream.read(buffer, 0, buffer.length);
                        if (readCount > 0) {
                            audioTrack.write(buffer, 0, readCount);
                            audioTrack.play();
                        }
                    }
                    //播放完自动停止
                    Log.e(TAG, "onMarkerReached: 播放完毕");
                    onFinished();
                    stopPlay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            playVoiceThread.start();
        } else { //暂停之后再点播放，则直接继续之前的进度播放
            isPause = false;
        }
    }

    abstract void onFinished();

    public boolean isPlaying() {
        return audioTrack != null;
    }

    //暂停播放
    public void pausePlay() {
        Log.e(TAG, "pausePlay: ");
        //播放线程启动了才允许暂停
        if (playVoiceThread != null) {
            isPause = true;
            audioTrack.pause();
        }
    }

    //停止播放
    public void stopPlay() {
        Log.e(TAG, "stopPlay: ");
        if (playVoiceThread != null) {
            playVoiceThread.interrupt();
            playVoiceThread = null;
        }
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }

}
