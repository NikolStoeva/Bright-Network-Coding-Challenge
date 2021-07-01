package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private String videoPlaying = null;
  private String pausedVideo = null;
  private final Map<String, String> flagged = new HashMap<>();
  private final List<VideoPlaylist> playlists = new ArrayList<>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");

    List <Video> allVideos = videoLibrary.getVideos();
    allVideos.sort(Comparator.comparing(Video::getTitle));

    // making the tags into a single string that is not comma separated
    for(Video video : allVideos){
      if(flagged.containsKey(video.getVideoId())){
        System.out.println(createAReadableString(video) + " - FLAGGED (reason: " + flagged.get(video.getVideoId()) + ")");
      }
      else System.out.println(createAReadableString(video));
    }
  }

  public void playVideo(String videoId){

    if(flagged.containsKey(videoId)){
      System.out.println("Cannot play video: Video is currently flagged (reason: " + flagged.get(videoId) + ")");
      return;
    }

    String videoToPlay = null;

    // getting the name of the video
      for (Video video : videoLibrary.getVideos()) {
        if (video.getVideoId().equals(videoId)) {
          videoToPlay = video.getTitle();
          break;
        }
      }
      // checking if a video was found and playing or stopping and playing
      if(videoToPlay == null) System.out.println("Cannot play video: Video does not exist");
      else if(videoPlaying == null) {
        videoPlaying = videoToPlay;
        System.out.println("Playing video: " + videoPlaying);
      }
      else {
        System.out.println("Stopping video: " + videoPlaying);
        videoPlaying = videoToPlay;
        pausedVideo = null;
        System.out.println("Playing video: " + videoPlaying);
      }

  }

  public void stopVideo() {
    if(videoPlaying == null) {
      System.out.println("Cannot stop video: No video is currently playing");
    }
    else {
      System.out.println("Stopping video: " + videoPlaying);
      videoPlaying = null;
      pausedVideo = null;
    }
  }

  public void playRandomVideo() {
    if(flagged.size() == videoLibrary.getVideos().size()){
      System.out.println("No videos available");
      return;
    }
    Random rand = new Random();

    List<Video> videos = videoLibrary.getVideos();

    int randomVideo = rand.nextInt(videos.size());

    while(flagged.containsKey(videos.get(randomVideo).getVideoId())){
      randomVideo = rand.nextInt(videos.size());
    }
    playVideo(videos.get(randomVideo).getVideoId());
  }

  public void pauseVideo() {
    if(videoPlaying == null) {
      System.out.println("Cannot pause video: No video is currently playing");
      pausedVideo = null;
    }
    else if(videoPlaying.equals(pausedVideo)) System.out.println("Video already paused: " + pausedVideo);
    else {
      pausedVideo = videoPlaying;
      System.out.println("Pausing video: " + pausedVideo);
    }
  }

  public void continueVideo() {
    if(videoPlaying == null) System.out.println("Cannot continue video: No video is currently playing");
    else if(pausedVideo == null) System.out.println("Cannot continue video: Video is not paused");
    else {
      System.out.println("Continuing video: " + pausedVideo);
      pausedVideo = null;
    }
  }

  public void showPlaying() {
    if(videoPlaying == null){
      System.out.println("No video is currently playing");
      return;
    }
    // finding the video and creating a string with all of its attributes
    String videoToDisplay = null;
    for(Video video : videoLibrary.getVideos()){
      if(video.getTitle().equals(videoPlaying)) {
        videoToDisplay = createAReadableString(video);
      }
    }
    if(pausedVideo == null) System.out.println("Currently playing: " + videoToDisplay);
    else System.out.println("Currently playing: " + videoToDisplay + " - PAUSED");
  }

  public void createPlaylist(String playlistName) {
    for (VideoPlaylist pl : playlists) {
      if (playlistName.equalsIgnoreCase(pl.getPlaylistName())) {
        System.out.println("Cannot create playlist: A playlist with the same name already exists");
        return;
      }
    }
    playlists.add(new VideoPlaylist(playlistName));
    System.out.println("Successfully created new playlist: " + playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {

    if(flagged.containsKey(videoId)){
      System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + flagged.get(videoId) + ")");
      return;
    }

    VideoPlaylist playlist = doesPlaylistExist(playlistName);
    if (playlist == null) System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");

    else {
      Video video = doesVideoExistInLibrary(videoId);
      if (video == null) System.out.println("Cannot add video to " + playlistName + ": Video does not exist");

      else {
        // checking if the video exists in the playlist
        for (Video v : playlist.getPlaylistVideos()) {
          if (v == video) {
            System.out.println("Cannot add video to " + playlistName + ": Video already added");
            return;
          }
        }
        playlist.addVideo(video);
        System.out.println("Added video to " + playlistName + ": " + video.getTitle());
      }
    }
  }

  public void showAllPlaylists() {
    if(playlists.isEmpty())System.out.println("No playlists exist yet");
    else{
      playlists.sort(Comparator.comparing(VideoPlaylist::getPlaylistName, String.CASE_INSENSITIVE_ORDER));
      System.out.println("Showing all playlists:");
      for(VideoPlaylist vpl : playlists){
        System.out.println(vpl.getPlaylistName());
      }
    }
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist playlist = doesPlaylistExist(playlistName);

    if(playlist == null) System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    // if the playlist exists
    else {
      if(playlist.getPlaylistVideos().size() != 1) System.out.println("Showing playlist: " + playlistName + " (" + playlist.getPlaylistVideos().size() + " videos)");
      else System.out.println("Showing playlist: " + playlistName + " (1 video)");

      if(playlist.getPlaylistVideos().isEmpty()){
        System.out.println("No videos here yet");
        return;
      }
      for(Video v : playlist.getPlaylistVideos()){

        if(flagged.containsKey(v.getVideoId())){
          System.out.println(createAReadableString(v) + " - FLAGGED (reason: " + flagged.get(v.getVideoId()) + ")");
        }
        else System.out.println(createAReadableString(v));
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = doesPlaylistExist(playlistName);

    if(playlist == null) System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    else {
      Video video = doesVideoExistInLibrary(videoId);
      if (video == null) System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      else {
        // checking if the video is in the playlist
        for (Video v : playlist.getPlaylistVideos()) {
          // removing the video if it is in the playlist
          if (v == video) {
            playlist.removeVideo(v);
            System.out.println("Removed video from " + playlistName + ": " + v.getTitle());
            return;
          }
        }

        System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
      }
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlist = doesPlaylistExist(playlistName);
    if(playlist == null) System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    else {
      playlist.getPlaylistVideos().clear();
      System.out.println("Successfully removed all videos from " + playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlist = doesPlaylistExist(playlistName);

    if(playlist == null) System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    else {
      playlists.remove(playlist);
      System.out.println("Deleted playlist: " + playlistName);
    }
  }

  public void searchVideos(String searchTerm) {
    generalSearch(searchTerm, "Term");
  }

  public void searchVideosWithTag(String videoTag) {
    generalSearch(videoTag, "Tag");
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason) {

    // checking if the video has been flagged
    if(flagged.containsKey(videoId)) System.out.println("Cannot flag video: Video is already flagged");
    else {
      for(Video v : videoLibrary.getVideos()){
        if(v.getVideoId().equals(videoId)){
          flagged.put(videoId,reason);
          if(v.getTitle().equals(videoPlaying) || v.getTitle().equals(pausedVideo)) stopVideo();
          System.out.println("Successfully flagged video: " + v.getTitle() + " (reason: " + reason + ")");
          return;
        }
      }
      System.out.println("Cannot flag video: Video does not exist");
    }

  }

  public void allowVideo(String videoId) {
    Video video = null;
    // checking if the video exists in the video list of videoLibrary
    for (Video v : videoLibrary.getVideos()) {
      if (v.getVideoId().equals(videoId)) {
        video = v;
        break;
      }
    }
    if (video == null) System.out.println("Cannot remove flag from video: Video does not exist");

    else if (flagged.containsKey(videoId)){
      flagged.remove(videoId);
      System.out.println("Successfully removed flag from video: " + video.getTitle());
    }
    else System.out.println("Cannot remove flag from video: Video is not flagged");
  }

  public String createAReadableString(Video video){
    StringBuilder string = new StringBuilder();
    //System.out.println(  +  + tags + "]");
    string.append(video.getTitle());
    string.append(" (");
    string.append(video.getVideoId());
    string.append(") [");

    String divider = "";
    for(String tag : video.getTags()){
      string.append(divider);
      divider = " ";
      string.append(tag);
    }
    string.append("]");

    return string.toString();
  }

  public VideoPlaylist doesPlaylistExist (String playlistName){
    VideoPlaylist playlist = null;
    for(VideoPlaylist vpl : playlists){
      if(vpl.getPlaylistName().equalsIgnoreCase(playlistName)){
        playlist = vpl;
        break;
      }
    }
    return playlist;
  }

  public Video doesVideoExistInLibrary (String videoId){
    Video video = null;
    // checking if the video exists in the video list of videoLibrary
    for (Video v : videoLibrary.getVideos()) {
      if (v.getVideoId().equals(videoId)) {
        video = v;
        break;
      }
    }
    return video;
  }

  public void generalSearch (String parm, String type){
    // sorting videos by title
    List<Video> allVideos = videoLibrary.getVideos();
    allVideos.sort(Comparator.comparing(Video::getTitle));

    int number = 1;
    Map<Integer, Video> results = new HashMap<>();
    boolean contains = false;

    for (Video video : allVideos) {
      if(flagged.containsKey(video.getVideoId())){
        continue;
      }
      if(type.equals("Term")) {
        if (video.getTitle().toLowerCase().contains(parm.toLowerCase())) {
          if (number == 1) System.out.println("Here are the results for " + parm + ":");
          contains = true;

          results.put(number, video);
          System.out.println(number + ") " + createAReadableString(video));
          number++;
        }
      }
      else{
        if (video.getTags().contains(parm.toLowerCase())) {
          if(number == 1) System.out.println("Here are the results for " + parm + ":");
          contains = true;

          results.put(number, video);
          System.out.println(number + ") " + createAReadableString(video));
          number++;
        }
      }

    }

    if(!contains){
      System.out.println("No search results for " + parm);
      return;
    }

    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");

    Scanner scanner = new Scanner(System.in);
    try {
      int answer = scanner.nextInt();
      if (results.containsKey(answer)) {
        playVideo(results.get(answer).getVideoId());
      }
    } catch (Exception ignored) {
    }
  }
}