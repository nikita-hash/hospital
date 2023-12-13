package com.example.hospital.dto;

import com.example.hospital.validation.UniqueEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class UserChangeDto {

    Long id;

    @NotNull(message = "Заполните поле !")
    @Size(max = 30, message = "Имя должно содержать не более {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ]+", message = "Имя должно содержать только русские символы")
    String name;

    @NotNull(message = "Заполните поле !")
    @Size(max = 30, message = "Имя должно содержать не более {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ]+", message = "Имя должно содержать только русские символы")
    String sur_name;

    @NotNull(message = "Заполните поле !")
    @Size(max = 30, message = "Имя должно содержать не более {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ]+", message = "Имя должно содержать только русские символы")
    String patronymic;

    @NotNull(message = "Заполните поле !")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Неверный формат почты")
    String email;

    @NotNull(message = "Заполните поле !")
    @Size(min = 11, max = 12, message = "Неверный формат телефона !")
    @Pattern(regexp = "(\\+375|80)\\d{9}", message = "Неверный формат телефона !")
    String phone;

    @NotEmpty(message = "Заполните поле пароля !")
    String password;

    @NotNull(message = "Добавьте аватар !")
    MultipartFile image;


}
