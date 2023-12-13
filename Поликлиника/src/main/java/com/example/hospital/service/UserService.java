package com.example.hospital.service;

import com.example.hospital.dto.UserChangeDto;
import com.example.hospital.dto.UserSingInDto;
import com.example.hospital.model.Role;
import com.example.hospital.model.StatusUser;
import com.example.hospital.model.User;
import com.example.hospital.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Service
public class UserService implements CrudOperation<User,UserChangeDto> , UserDetailsService {

    private UserRepository userRepository;
    private ImageServiece imageService;

    @Autowired
    UserService(UserRepository userRepository, ImageServiece imageServiece){
        this.userRepository=userRepository;
        this.imageService=imageServiece;
    }

    @Override
    public List<User> findAll() {
       return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> update(Long id, UserChangeDto userChangeDto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(userChangeDto.getEmail());
                    user.setName(userChangeDto.getName());
                    user.setSur_name(userChangeDto.getSur_name());
                    user.setPatronymic(userChangeDto.getPatronymic());
                    user.setPhone(userChangeDto.getPhone());
                    user.setPassword(userChangeDto.getPassword());
                    uploadImage(userChangeDto.getImage());
                    Optional.ofNullable(userChangeDto.getImage())
                            .filter(not(MultipartFile::isEmpty))
                            .ifPresent(image -> user.setImage(image.getOriginalFilename()));
                    return user;
                })
                .map(userRepository::save);
    }

    public List<User>findAllByRole(Role role){
        return userRepository.findAllByRole(role);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public double calculateRolePercentage(Role role){
       return userRepository.calculateRolePercentage(role);
    }

    public double calculateStatusPercentage(StatusUser statusUser){
        return userRepository.calculateStatusPercentage(statusUser);
    }

    public Optional<User> blockUser(Long id){
       return userRepository.findById(id)
                .map(user -> {
                    user.setStatusUser(StatusUser.BLOCK_STATUS);
                    return user;
                })
                .map(userRepository::save);

    }

    public Optional<User> unlockUser(Long id){
        return userRepository.findById(id)
                .map(user -> {
                    user.setStatusUser(StatusUser.ACTIVE_STATUS);
                    return user;
                })
                .map(userRepository::save);
    }


    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.uploadImage(image.getOriginalFilename(), image.getInputStream());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new UserSingInDto(
                        user.getId(),
                        user.getPassword(),
                        user.getEmail(),
                        user.getStatusUser(),
                        user.getRole()
                ))
                .orElseThrow(()-> new UsernameNotFoundException("Аккаунт не был найден"));
    }

}
