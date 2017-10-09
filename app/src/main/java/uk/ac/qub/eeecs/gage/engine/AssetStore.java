package uk.ac.qub.eeecs.gage.engine;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;

/**
 * Asset store for holding loaded assets.
 *
 * @version 1.0
 */
public class AssetStore {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Bitmap asset store
     */
    private HashMap<String, Bitmap> mBitmaps;

    /**
     * Music asset store
     */
    private HashMap<String, Music> mMusic;

    /**
     * Sound asset store
     */
    private HashMap<String, Sound> mSounds;
    private SoundPool mSoundPool;

    /**
     * File IO
     */
    private FileIO mFileIO;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new asset store
     *
     * @param fileIO FileIO instance for loading assets
     */
    public AssetStore(FileIO fileIO) {
        mFileIO = fileIO;
        mBitmaps = new HashMap<String, Bitmap>();
        mMusic = new HashMap<String, Music>();
        mSounds = new HashMap<String, Sound>();
        mSoundPool = new SoundPool(Sound.MAX_CONCURRENT_SOUNDS,
                AudioManager.STREAM_MUSIC, 0);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Store //
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Add the specified bitmap asset to the store
     *
     * @param assetName Name given to the asset
     * @param asset     Bitmap asset to add
     * @return boolean true if the asset could be added, false it not (e.g. an
     * asset with the specified name already exists).
     */
    public boolean add(String assetName, Bitmap asset) {
        if (mBitmaps.containsKey(assetName))
            return false;

        mBitmaps.put(assetName, asset);
        return true;
    }

    /**
     * Add the specified music asset to the store
     *
     * @param assetName Name given to the asset
     * @param asset     Music asset to add
     * @return boolean true if the asset could be added, false it not (e.g. an
     * asset with the specified name already exists).
     */
    public boolean add(String assetName, Music asset) {
        if (mBitmaps.containsKey(assetName))
            return false;

        mMusic.put(assetName, asset);
        return true;
    }

    /**
     * Add the specified sound asset to the store
     *
     * @param assetName Name given to the asset
     * @param asset     Sound asset to add
     * @return boolean true if the asset could be added, false it not (e.g. an
     * asset with the specified name already exists).
     */
    public boolean add(String assetName, Sound asset) {
        if (mSounds.containsKey(assetName))
            return false;

        mSounds.put(assetName, asset);
        return true;
    }

    /**
     * Load and add the specified bitmap asset to the store
     *
     * @param assetName  Name given to the asset
     * @param bitmapFile Location of the bitmap asset
     * @return boolean true if the asset could be loaded and added, false if not
     */
    public boolean loadAndAddBitmap(String assetName, String bitmapFile) {

        boolean success = true;
        try {
            Bitmap bitmap = mFileIO.loadBitmap(bitmapFile, null);
            success = add(assetName, bitmap);
        } catch (IOException e) {
            Log.e("Gage", "AssetStore.loadAndAddBitmap: Cannot load ["
                    + bitmapFile + "]");
            success = false;
        }

        return success;
    }

    /**
     * Load and add the specified music asset to the store
     *
     * @param assetName Name given to the asset
     * @param musicFile Location of the music asset
     * @return boolean true if the asset could be loaded and added, false if not
     */
    public boolean loadAndAddMusic(String assetName, String musicFile) {
        boolean success = true;
        try {
            Music music = mFileIO.loadMusic(musicFile);
            success = add(assetName, music);

        } catch (IOException e) {
            Log.e("Gage", "AssetStore.loadAndAddMusic: Cannot load ["
                    + musicFile + "]");
            success = false;
        }

        return success;
    }

    /**
     * Load and add the specified sound asset to the store
     *
     * @param assetName Name given to the asset
     * @param soundFile Location of the sound asset
     * @return boolean true if the asset could be loaded and added, false if not
     */
    public boolean loadAndAddSound(String assetName, String soundFile) {
        boolean success = true;
        try {
            Sound sound = mFileIO.loadSound(soundFile, mSoundPool);
            success = add(assetName, sound);
        } catch (IOException e) {
            Log.e("Gage", "AssetStore.loadAndAddSound: Cannot load ["
                    + soundFile + "]");
            success = false;
        }

        return success;
    }

    /**
     * Retrieve the specified bitmap asset from the store
     *
     * @param assetName Name of the asset to retrieve
     * @return Bitmap asset, null if the named asset could not be found
     */
    public Bitmap getBitmap(String assetName) {
        return mBitmaps.get(assetName);
    }

    /**
     * Retrieve the specified music asset from the store
     *
     * @param assetName Name of the asset to retrieve
     * @return Music asset, null if the named asset could not be found
     */
    public Music getMusic(String assetName) {
        return mMusic.get(assetName);
    }

    /**
     * Retrieve the specified sound asset from the store
     *
     * @param assetName Name of the asset to retrieve
     * @return Sound asset, null if the named asset could not be found
     */
    public Sound getSound(String assetName) {
        return mSounds.get(assetName);
    }
}