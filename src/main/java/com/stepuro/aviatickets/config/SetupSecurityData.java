package com.stepuro.aviatickets.config;

import com.stepuro.aviatickets.models.Privilege;
import com.stepuro.aviatickets.models.Role;
import com.stepuro.aviatickets.models.User;
import com.stepuro.aviatickets.repositories.PrivilegeRepository;
import com.stepuro.aviatickets.repositories.RoleRepository;
import com.stepuro.aviatickets.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class SetupSecurityData implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readAircompanyPrivilege
                = createPrivilegeIfNotFound("READ_AIRCOMPANY_PRIVILEGE");
        Privilege writeAircompanyPrivilege
                = createPrivilegeIfNotFound("WRITE_AIRCOMPANY_PRIVILEGE");
        Privilege deleteAircompanyPrivilege
                = createPrivilegeIfNotFound("DELETE_AIRCOMPANY_PRIVILEGE");

        Privilege readAirplanePrivilege
                = createPrivilegeIfNotFound("READ_AIRPLANE_PRIVILEGE");
        Privilege writeAirplanePrivilege
                = createPrivilegeIfNotFound("WRITE_AIRPLANE_PRIVILEGE");
        Privilege deleteAirplanePrivilege
                = createPrivilegeIfNotFound("DELETE_AIRPLANE_PRIVILEGE");

        Privilege readAirplaneModelPrivilege
                = createPrivilegeIfNotFound("READ_AIRPLANE_MODEL_PRIVILEGE");
        Privilege writeAirplaneModelPrivilege
                = createPrivilegeIfNotFound("WRITE_AIRPLANE_MODEL_PRIVILEGE");
        Privilege deleteAirplaneModelPrivilege
                = createPrivilegeIfNotFound("DELETE_AIRPLANE_MODEL_PRIVILEGE");

        Privilege readAirportPrivilege
                = createPrivilegeIfNotFound("READ_AIRPORT_PRIVILEGE");
        Privilege writeAirportPrivilege
                = createPrivilegeIfNotFound("WRITE_AIRPORT_PRIVILEGE");
        Privilege deleteAirportPrivilege
                = createPrivilegeIfNotFound("DELETE_AIRPORT_PRIVILEGE");

        Privilege readFlightPrivilege
                = createPrivilegeIfNotFound("READ_FLIGHT_PRIVILEGE");
        Privilege writeFlightPrivilege
                = createPrivilegeIfNotFound("WRITE_FLIGHT_PRIVILEGE");
        Privilege deleteFlightPrivilege
                = createPrivilegeIfNotFound("DELETE_FLIGHT_PRIVILEGE");

        Privilege readPivilegePrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE_PRIVILEGE");
        Privilege writePrivilegePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE_PRIVILEGE");
        Privilege deletePrivilegePrivilege
                = createPrivilegeIfNotFound("DELETE_PRIVILEGE_PRIVILEGE");

        Privilege readRolePrivilege
                = createPrivilegeIfNotFound("READ_ROLE_PRIVILEGE");
        Privilege writeRolePrivilege
                = createPrivilegeIfNotFound("WRITE_ROLE_PRIVILEGE");
        Privilege deleteRolePrivilege
                = createPrivilegeIfNotFound("DELETE_ROLE_PRIVILEGE");

        Privilege readTicketPrivilege
                = createPrivilegeIfNotFound("READ_TICKET_PRIVILEGE");
        Privilege writeTicketPrivilege
                = createPrivilegeIfNotFound("WRITE_TICKET_PRIVILEGE");
        Privilege deleteTicketPrivilege
                = createPrivilegeIfNotFound("DELETE_TICKET_PRIVILEGE");

        Privilege readPurchasedTicketPrivilege
                = createPrivilegeIfNotFound("READ_PURCHASED_TICKET_PRIVILEGE");
        Privilege writePurchasedTicketPrivilege
                = createPrivilegeIfNotFound("WRITE_PURCHASED_TICKET_PRIVILEGE");
        Privilege deletePurchasedTicketPrivilege
                = createPrivilegeIfNotFound("DELETE_PURCHASED_TICKET_PRIVILEGE");

        Privilege readUserPrivilege
                = createPrivilegeIfNotFound("READ_USER_PRIVILEGE");
        Privilege writeUserPrivilege
                = createPrivilegeIfNotFound("WRITE_USER_PRIVILEGE");
        Privilege deleteUserPrivilege
                = createPrivilegeIfNotFound("DELETE_USER_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readAircompanyPrivilege, writeAircompanyPrivilege, deleteAircompanyPrivilege,
                readAirplanePrivilege, writeAirplanePrivilege, deleteAirplanePrivilege,
                readAirplaneModelPrivilege, writeAirplaneModelPrivilege, deleteAirplaneModelPrivilege,
                readAirportPrivilege, writeAirportPrivilege, deleteAirportPrivilege,
                readFlightPrivilege, writeFlightPrivilege, deleteFlightPrivilege,
                readPivilegePrivilege, writePrivilegePrivilege, deletePrivilegePrivilege,
                readRolePrivilege, writeRolePrivilege, deleteRolePrivilege,
                readTicketPrivilege, writeTicketPrivilege, deleteTicketPrivilege,
                readPurchasedTicketPrivilege, writePurchasedTicketPrivilege, deletePurchasedTicketPrivilege,
                readUserPrivilege, writeUserPrivilege, deleteUserPrivilege);

        List<Privilege> stuffPrivileges = Arrays.asList(
                readAircompanyPrivilege, writeAircompanyPrivilege,
                readAirplanePrivilege, writeAirplanePrivilege,
                readAirplaneModelPrivilege, writeAirplaneModelPrivilege,
                readAirportPrivilege, writeAirportPrivilege,
                readFlightPrivilege, writeFlightPrivilege,
                readPivilegePrivilege, writePrivilegePrivilege,
                readRolePrivilege, writeRolePrivilege,
                readTicketPrivilege, writeTicketPrivilege,
                readPurchasedTicketPrivilege, writePurchasedTicketPrivilege,
                readUserPrivilege, writeUserPrivilege);

        List<Privilege> userPrivilege = Arrays.asList(
                readAircompanyPrivilege,
                readAirplanePrivilege,
                readAirplaneModelPrivilege,
                readAirportPrivilege,
                readFlightPrivilege);

        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_STUFF", stuffPrivileges);
        createRoleIfNotFound("ROLE_USER", userPrivilege);

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        createAdminIfNotExists(adminRole);




        alreadySetup = true;
    }
    @Transactional
    User createAdminIfNotExists(Role adminRole){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        Optional<User> optionalUser = userRepository.findByLogin("admin");
        if(!optionalUser.isPresent()) {
            try {
                User user = User.builder()
                        .name("Vadim")
                        .login("admin")
                        .password(passwordEncoder.encode("1111"))
                        .birthdate(DATE_FORMAT.parse("2003-10-18"))
                        .email("v.stepuroo@gmail.com")
                        .roles(Collections.singletonList(adminRole))
                        .build();
                return userRepository.save(user);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return optionalUser.get();
    }
    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = Privilege.builder()
                    .name(name)
                    .build();
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, List<Privilege> privileges) {

        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (!optionalRole.isPresent()) {
            Role role = Role.builder()
                    .name(name)
                    .privilegeList(privileges)
                    .build();
            role = roleRepository.save(role);
            return role;
        }
        return optionalRole.get();
    }
}
