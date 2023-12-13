package com.example.hospital.model;



import com.example.hospital.persistency.BasicEntity;
import com.example.hospital.validation.Age;
import com.example.hospital.validation.UniqueEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Entity
@Setter
@Getter
@Table(name = "user_table")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements BasicEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Заполните поле !")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",message = "Неверный формат почты")
    @UniqueEmail(message = "Пользователь с такой почтой уже существует !")
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_user")
    StatusUser statusUser;

    @NotNull(message = "Заполните поле !")
    @Size(max = 30, message = "Имя должно содержать не более {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ]+", message = "Имя должно содержать только русские символы")
    private String name;

    @Size(max = 30, message = "Имя должно содержать не более {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ]+", message = "Имя должно содержать только русские символы")
    @NotNull(message = "Заполните поле !")
    private String sur_name;

    @Size(max = 30, message = "Имя должно содержать не более {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ]+", message = "Имя должно содержать только русские символы")
    @NotNull(message = "Заполните поле !")
    private String patronymic;

    @NotNull(message = "Заполните поле !")
    @Size(min = 11,max = 12,message = "Неверный формат телефона !")
    @Pattern(regexp = "(\\+375|80)\\d{9}",message = "Неверный формат телефона !")
    private String phone;

    @NotNull(message = "Заполните поле !")
    @Age
    private LocalDate date_birth;

    String image;

    @Override
    public Long getId() {
        return  id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    List<Record> records;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<MedicalCard> medicalCards;

}
