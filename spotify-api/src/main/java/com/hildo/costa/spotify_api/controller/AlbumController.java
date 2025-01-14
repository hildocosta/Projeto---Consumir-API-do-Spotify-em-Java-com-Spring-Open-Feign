package com.hildo.costa.spotify_api.controller;

import com.hildo.costa.spotify_api.client.Album;
import com.hildo.costa.spotify_api.client.AlbumSpotifyClient;
import com.hildo.costa.spotify_api.client.AuthSpotifyClient;
import com.hildo.costa.spotify_api.client.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/spotify/api")
public class AlbumController {

    private final AuthSpotifyClient authSpotifyClient;
    private final AlbumSpotifyClient albumSpotifyClient;

    public AlbumController(AuthSpotifyClient authSpotifyClient,
                           AlbumSpotifyClient albumSpotifyClient) {
        this.authSpotifyClient = authSpotifyClient;
        this.albumSpotifyClient = albumSpotifyClient;
    }

    @GetMapping("/albums")
    public ResponseEntity<List<Album>> helloWorld() {

        var request = new LoginRequest(
                "client_credentials",
                "1be0acc716a2429388d0db76f548f933",
                "0ea7b00085ac4e43908365c8ad6860df"
        );
        var token = authSpotifyClient.login(request).getAccessToken();

        var response = albumSpotifyClient.getReleases("Bearer " + token);


        return ResponseEntity.ok(response.getAlbums().getItems());
    }
}