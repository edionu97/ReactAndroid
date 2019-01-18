package controllers.fetch;

import controllers.messages.AddUnfetchedMessage;
import controllers.messages.UnfetchedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.providers.TokenExtraInfo;
import security.providers.TokenProvider;
import services.interfaces.FetchService;

@RestController
@RequestMapping("/fetching")
@ComponentScan(basePackages = "configuration")
public class FetchController {

    @Autowired
    public FetchController(FetchService fetchService, TokenProvider tokenProvider) {
        this.fetchService = fetchService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/unfetched")
    public ResponseEntity<UnfetchedMessage> getUnfetched(@RequestHeader("Authentication") String authenticationHeader) {

        if (authenticationHeader == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String username = tokenProvider.getSubject(authenticationHeader);

        return new ResponseEntity<>(new UnfetchedMessage(fetchService.unfetched(username)), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUnfetched(@RequestHeader("Authentication") String authenticationHeader, @RequestBody AddUnfetchedMessage message){

        if (authenticationHeader == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length() || message == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String username = tokenProvider.getSubject(authenticationHeader);

        try {
            fetchService.add(message.getTableName(), message.getType(), message.getData(), username);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/fetch")
    public ResponseEntity<?> fetchData(@RequestHeader("Authentication") String authenticationHeader, @RequestBody AddUnfetchedMessage message){
        if (authenticationHeader == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length() || message == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String username = tokenProvider.getSubject(authenticationHeader);

        try {
            fetchService.fetch(message.getTableName(), message.getType(), message.getData(), username);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private FetchService fetchService;
    private TokenProvider tokenProvider;
}
