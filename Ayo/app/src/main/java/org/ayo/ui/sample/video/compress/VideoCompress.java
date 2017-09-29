package org.ayo.ui.sample.video.compress;

import android.os.Environment;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.googlecode.mp4parser.util.Matrix;
import com.googlecode.mp4parser.util.Path;

import org.ayo.log.Trace;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by wanglong on 17/6/28.
 * 视频压缩
 */

public class VideoCompress {


    private float mOriginalSize;

    private float mVideoDuration;

    private long mTotalMicroseconds;

    private int mOriginalBitrate;

    private int mBitrate;

    private long mVideoFramesSize;

    private long mAudioFramesSize;

    private int mRotationValue;

    private int mOriginalWidth;

    private int mOriginalHeight;

    private int mResultWidth;

    private int mResultHeight;

    private CompressListener mListener;

    private String mVideoPath;

    private MediaController mMediaController;

    private File mFile;

    public VideoCompress startCompress(String videoPath, CompressListener listener, long duration) {
        this.mVideoPath = videoPath;
        this.mListener = listener;
        this.mTotalMicroseconds = duration * 1000 * 1000;
        return this;
    }

    public void compress() {
        mFile = new File(Environment.getExternalStorageDirectory(), UUID.randomUUID() + ".dqd");
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean success = processOpenVideo();
        if (success) {
            mListener.compressStart();
            compressVideo(mVideoPath, mFile.getAbsolutePath(),
                    mResultWidth, mResultHeight, mRotationValue, mBitrate);
        } else {
            mListener.compressError("init fail");
        }
    }

    /**
     * 压缩视频
     */
    private void compressVideo(final String videoPath, final String resultPath, final int resultWidth
            , final int resultHeight, final int rotationValue, final int bitrate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Trace.e("压缩", "视频压缩开始");
                long start = System.currentTimeMillis();
                mMediaController = new MediaController();
                final boolean isSuccess = mMediaController.convertVideo(mListener, videoPath, -1, -1,
                        resultPath, resultWidth, resultHeight, rotationValue, bitrate, mTotalMicroseconds);
                long end = System.currentTimeMillis() - start;
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
                final String time = sdf.format(new Date(end));
                Trace.e("压缩", isSuccess ? "视频压缩成功,耗时 " + time : "视频压缩失败");
                if (isSuccess) {
                    mListener.compressSuccess(resultPath);
                } else {
                    mListener.compressError("compress fail");
                }
            }
        }).start();
    }

    private boolean processOpenVideo() {
        try {
            File file = new File(mVideoPath);
            mOriginalSize = file.length();

            IsoFile isoFile = new IsoFile(mVideoPath);
            List<Box> boxes = Path.getPaths(isoFile, "/moov/trak/");
            TrackHeaderBox trackHeaderBox = null;
            boolean isAvc = true;
            boolean isMp4A = true;

            Box boxTest = Path.getPath(isoFile, "/moov/trak/mdia/minf/stbl/stsd/mp4a/");
            if (boxTest == null) {
                isMp4A = false;
            }

            if (!isMp4A) {
                return false;
            }

            boxTest = Path.getPath(isoFile, "/moov/trak/mdia/minf/stbl/stsd/avc1/");
            if (boxTest == null) {
                isAvc = false;
            }

            for (Box box : boxes) {
                TrackBox trackBox = (TrackBox) box;
                long sampleSizes = 0;
                long trackBitrate = 0;
                try {
                    MediaBox mediaBox = trackBox.getMediaBox();
                    MediaHeaderBox mediaHeaderBox = mediaBox.getMediaHeaderBox();
                    SampleSizeBox sampleSizeBox = mediaBox.getMediaInformationBox().getSampleTableBox().getSampleSizeBox();
                    for (long size : sampleSizeBox.getSampleSizes()) {
                        sampleSizes += size;
                    }
                    mVideoDuration = (float) mediaHeaderBox.getDuration() / (float) mediaHeaderBox.getTimescale();
                    trackBitrate = (int) (sampleSizes * 8 / mVideoDuration);
                } catch (Exception e) {
                    Trace.e("tmessages", e);
                }
                TrackHeaderBox headerBox = trackBox.getTrackHeaderBox();
                if (headerBox.getWidth() != 0 && headerBox.getHeight() != 0) {
                    trackHeaderBox = headerBox;
                    mOriginalBitrate = mBitrate = (int) (trackBitrate / 100000 * 100000);
                    if (mBitrate > 1100000) {
                        mBitrate = 1100000;
                    }
                    mVideoFramesSize += sampleSizes;
                } else {
                    mAudioFramesSize += sampleSizes;
                }
            }
            if (trackHeaderBox == null) {
                return false;
            }

            Matrix matrix = trackHeaderBox.getMatrix();
            if (matrix.equals(Matrix.ROTATE_90)) {
                mRotationValue = 90;
            } else if (matrix.equals(Matrix.ROTATE_180)) {
                mRotationValue = 180;
            } else if (matrix.equals(Matrix.ROTATE_270)) {
                mRotationValue = 270;
            }
            mResultWidth = mOriginalWidth = (int) trackHeaderBox.getWidth();
            mResultHeight = mOriginalHeight = (int) trackHeaderBox.getHeight();

            if (mResultWidth > 640 || mResultHeight > 640) {
                float scale = mResultWidth > mResultHeight ? 640.0f / mResultWidth : 640.0f / mResultHeight;
                mResultWidth *= scale;
                mResultHeight *= scale;
                if (mBitrate != 0) {
                    mBitrate *= Math.max(0.5f, scale);
                    mVideoFramesSize = (long) (mBitrate / 8 * mVideoDuration);
                }
            }

            if (!isAvc && (mResultWidth == mOriginalWidth || mResultHeight == mOriginalHeight)) {
                return false;
            }
        } catch (Exception e) {
            Trace.e("tmessages", e);
            return false;
        }

        mVideoDuration *= 1000;

        return true;
    }

    public void release(boolean isCompress) {
        mMediaController.release();
        if (mFile != null && mFile.exists() && isCompress)
            mFile.delete();
    }

}
