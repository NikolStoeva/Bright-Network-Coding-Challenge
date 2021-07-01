package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {

    private final String playlistName;
    private final List<Video> playlistVideos;

    public VideoPlaylist(String playlistName){
        this.playlistName = playlistName;
        playlistVideos = new ArrayList<>();
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public List<Video> getPlaylistVideos() {
        return playlistVideos;
    }

    public void addVideo (Video video){
        playlistVideos.add(video);
    }

    public void removeVideo (Video video){
        playlistVideos.remove(video);
    }
}
