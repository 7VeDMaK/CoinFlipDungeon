package com.coinflip.dungeon.Controller;

import com.coinflip.dungeon.Domain.Campaign;
import com.coinflip.dungeon.Service.CampaignService;
import com.coinflip.dungeon.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign")
public class CampaignController {

    @Autowired
    CampaignService campaignService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<Campaign>> findAll(){
        return new ResponseEntity<>(campaignService.sayHelloAndPrintCampaigns(), HttpStatus.OK);
    }

//    @PostMapping("/addCampaign")
//    public ResponseEntity<String> addCampaignToDatabase(@RequestHeader("Authorization") String token, @Valid @RequestBody CharacterRequest characterRequest) {
//        String username = jwtUtils.getUserNameFromJwtToken(token.split(" ")[1].trim());
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));
//        Character character = new Character(characterRequest.getName(), characterRequest.getDescription(), user); // Create User object
//        characterRepository.save(character);
//        return ResponseEntity.ok("Character " + characterRequest.getName() + " was saved in database.");
//    }
//
//    // it should be noted that "correct" input is assumed (i.e. all values are correct).
//    // If some values are empty and the user uses this endpoint,
//    // the request should give already existing information about the character.
//    // For example:
//    // name: Averon ==> Avellion
//    // description: A middle-aged single magician ==> A middle-aged single magician
//    // Maybe it should have one more attribute 'current name' to simplify modify logic
//
//    @PatchMapping("/modifyCampaign")
//    public ResponseEntity<String> modifyCampaignToDatabase(@RequestHeader("Authorization") String token, @Valid @RequestBody CharacterRequest characterRequest) {
//        String username = jwtUtils.getUserNameFromJwtToken(token.split(" ")[1].trim());
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
//        Character character = characterRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Character not found"));
//        character.setName(characterRequest.getName());
//        character.setDescription(characterRequest.getDescription());
//        character.setPublic(characterRequest.getIsPublic());
//        characterRepository.save(character);
//
//        return ResponseEntity.ok("Character " + characterRequest.getName() + " was updated in database.");
//    }
//
//    // Important to note there are no action when user not registered, or it is another
//    @GetMapping("/getCampaign")
//    public ResponseEntity<String> getCampaignToDatabase(@RequestHeader("Authorization") String token, @RequestParam String name) {
//        if (token.isEmpty()) return ResponseEntity.badRequest().body("User not signed error");
//        //Can't be reached
//        String username = jwtUtils.getUserNameFromJwtToken(token.split(" ")[1].trim());
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
//        Integer userId = user.getId();
//        Character characterFound = characterRepository.findByNameAndUserId(name, userId).orElseThrow(() -> new NoSuchElementException("Character not found"));
//
//        if (characterFound.isPublic()) {
//            String message = String.format("Id: %d \nName: %s \nDescription: %s", characterFound.getId(), characterFound.getName(), characterFound.getDescription());
//            return ResponseEntity.ok(message);
//        }
//        return ResponseEntity.badRequest().body("Private character error / getCharacter endpoint error");
//        //3 case of the criteria task to check performance
//    }
//
//    @GetMapping("/deleteCampaign")
//    public ResponseEntity<String> addCampaignToDatabase(@RequestHeader("Authorization") String token, @RequestParam String name) {
//        String username = jwtUtils.getUserNameFromJwtToken(token.split(" ")[1].trim());
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));
//        Integer userId = user.getId();
//        characterRepository.deleteAllByNameAndUserId(name, userId);
//        return ResponseEntity.ok("Character " + name + " was deleted from database.");
//    }
}
