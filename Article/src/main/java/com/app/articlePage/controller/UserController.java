package com.app.articlePage.controller;

import com.app.articlePage.Model.ERoles;
import com.app.articlePage.Model.URoles;
import com.app.articlePage.Model.User;
import com.app.articlePage.payload.response.MessageResponse;
import com.app.articlePage.repository.RoleRepository;
import com.app.articlePage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/add")
    public ResponseEntity<?>addUser(@RequestBody User user){
        userRepository.save(user);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<User>getUserById(@PathVariable long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return  new ResponseEntity<>(user.get(),HttpStatus.OK);
        }
        else
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @GetMapping("/users")
    public ResponseEntity<List<User>>getUsers(){
        return new ResponseEntity(userRepository.findAll(),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteUser(@PathVariable long id){

        if(!userRepository.existsById(id)){
            return  ResponseEntity.badRequest().body(new MessageResponse("Error de suppresion ce user n'exist pas"));
        }
        try {
            userRepository.deleteById(id);
            return new ResponseEntity(new MessageResponse("Bien Supprimer ce user"),HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PatchMapping("/updateUser/{id}")
    public ResponseEntity<?>updateUser(@PathVariable long id,@RequestBody User user){

        if(!userRepository.existsById(id)){
            return  ResponseEntity.badRequest().body(new MessageResponse("Error de modifier ce user, n'existe pas !"));
        }
        try {
            User user1 = userRepository.findById(id).get();
            user1.setUsername(user.getUsername());
            user1.setEmail(user.getEmail());
            user1.setPassword(user.getPassword());
            String dateStamp = new SimpleDateFormat("dd-MM-yyyy hh:mm").format(Calendar.getInstance().getTime());
            user1.setUpdatedAt(dateStamp);

            userRepository.save(user1);
            return new ResponseEntity<>(new MessageResponse("User Bien Modifier"),HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("/addRoleToUser")
    public ResponseEntity<?>updateByRole(
            @RequestParam(required = true) long id,
            @RequestParam(required = true) String role){

            if(!userRepository.existsById(id)){
                return ResponseEntity.badRequest().body(new MessageResponse("User n'exist pas"));
            }
            User user = userRepository.getOne(id);
            user.setUpdatedAt(new SimpleDateFormat("dd-MM-yyy hh:mm").format(Calendar.getInstance().getTime()));
            URoles uRoles = new URoles();

            if(role == "user" || role.equals("user")){
               uRoles = roleRepository.findByName(ERoles.USER)
                       .orElseThrow(()->new RuntimeException("Error :Role Not Found"));
            }
            else if(role == "admin" || role.equals("admin")){
                uRoles = roleRepository.findByName(ERoles.ADMIN)
                        .orElseThrow(()->new RuntimeException("Error :Role Not Found"));
            }
            user.getRoles().add(uRoles);
            userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("Role "+uRoles.getName()+" Bien Ajouter to User"),HttpStatus.OK);
    }
    @PutMapping("deleteRoleFromUser")
    public ResponseEntity<?>deleteRoleUser(
            @RequestParam(required = true) long id,
            @RequestParam(required = true) String role) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("User n'exist pas"));
        }
        User user = userRepository.getOne(id);
        user.setUpdatedAt(new SimpleDateFormat("dd-MM-yyy hh:mm").format(Calendar.getInstance().getTime()));

        URoles uRoles = new URoles();

        if (role == "user" || role.equals("user")) {
            uRoles = roleRepository.findByName(ERoles.USER)
                    .orElseThrow(() -> new RuntimeException("Error :Role Not Found"));
        } else if (role == "admin" || role.equals("admin")) {
            uRoles = roleRepository.findByName(ERoles.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error :Role Not Found"));
        }
        user.getRoles().remove(uRoles);
        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("Role " + uRoles.getName() + " Bien Supprimer pour User"), HttpStatus.OK);
    }}