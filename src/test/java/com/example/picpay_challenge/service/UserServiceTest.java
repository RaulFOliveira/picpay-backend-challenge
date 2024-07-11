package com.example.picpay_challenge.service;

import com.example.picpay_challenge.domain.user.User;
import com.example.picpay_challenge.domain.user.UserRepository;
import com.example.picpay_challenge.domain.user.UserRole;
import com.example.picpay_challenge.domain.user.dto.CreateUserDTO;
import com.example.picpay_challenge.domain.user.dto.GetSimpleUserDTO;
import com.example.picpay_challenge.infra.exceptions.DocumentAlreadyExistsException;
import com.example.picpay_challenge.infra.exceptions.DocumentNotValidException;
import com.example.picpay_challenge.infra.exceptions.EmailAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserService.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @MockBean
    private UserRepository repo;

    private User common;
    private User shopkeeper;

    private CreateUserDTO commonUser;
    private CreateUserDTO shopkeeperUser;

    @BeforeEach
    public void setUp() {
        this.commonUser = new CreateUserDTO(
                "John Doe",
                "11012753085",
                "john.doe@example.com",
                "11987654321",
                UserRole.common,
                BigDecimal.valueOf(500)
        );
        this.shopkeeperUser = new CreateUserDTO(
                "John Doe",
                "11012753085123",
                "john.doe1@example.com",
                "11987654321",
                UserRole.shopkeeper,
                BigDecimal.valueOf(1500)
        );
        this.common = new User(commonUser);
        this.shopkeeper = new User(shopkeeperUser);
    }

    @Test
    @DisplayName("Criar usuário com sucesso")
    void create() {
        String document = commonUser.document();
        String email = commonUser.email();

        when(repo.findByDocument(document)).thenReturn(Optional.empty());
        when(repo.findByEmail(email)).thenReturn(Optional.empty());
        when(repo.save(any(User.class))).thenReturn(common);

        GetSimpleUserDTO result = service.create(commonUser);

        verify(repo, times(1)).findByDocument(document);
        verify(repo, times(1)).findByEmail(email);
        verify(repo, times(1)).save(any(User.class));

        assertEquals(common.getId(), result.id());
        assertEquals(common.getFullName(), result.fullName());
        assertEquals(common.getBalance(), result.balance());
    }

    @Test
    @DisplayName("Criar usuário com documento já existente")
    void createDocumentAlreadyExists() {
        String document = commonUser.document();
        String email = commonUser.email();

        when(repo.findByDocument(document)).thenReturn(Optional.of(common));

        var result = assertThrows(DocumentAlreadyExistsException.class, () -> service.create(commonUser));

        verify(repo, times(1)).findByDocument(document);
        verify(repo, times(0)).findByEmail(email);
        verify(repo, times(0)).save(any(User.class));

        assertEquals("Este documento já foi cadastrado.", result.getMessage());
    }

    @Test
    @DisplayName("Criar usuário 'common' com documento inválido")
    void createCommonDocumentNotValid() {
        String document = "11111111111111";
        CreateUserDTO userDTO = new CreateUserDTO(
                commonUser.fullName(),
                document,
                commonUser.email(),
                commonUser.password(),
                UserRole.common,
                commonUser.balance()
        );

            var result = assertThrows(DocumentNotValidException.class, () -> service.create(userDTO));

        verify(repo, times(0)).findByDocument(document);
        verify(repo, times(0)).findByEmail(commonUser.email());
        verify(repo, times(0)).save(any(User.class));

        assertEquals("O CPF de uma pessoa deve ter 11 caracteres.", result.getMessage());
    }

    @Test
    @DisplayName("Criar usuário 'shopkeeper' com documento inválido")
    void createShopkeeperDocumentNotValid() {
        String document = "11111111111";
        CreateUserDTO userDTO = new CreateUserDTO(
                shopkeeperUser.fullName(),
                document,
                shopkeeperUser.email(),
                shopkeeperUser.password(),
                UserRole.shopkeeper,
                shopkeeperUser.balance()
        );

        var result = assertThrows(DocumentNotValidException.class, () -> service.create(userDTO));

        verify(repo, times(0)).findByDocument(document);
        verify(repo, times(0)).findByEmail(shopkeeperUser.email());
        verify(repo, times(0)).save(any(User.class));

        assertEquals("O CNPJ de uma loja deve ter 14 caracteres.", result.getMessage());
    }

    @Test
    @DisplayName("Criar usuário com email ja existente")
    void createEmailAlreadyExists() {
        String document = commonUser.document();
        String email = commonUser.email();

        when(repo.findByDocument(document)).thenReturn(Optional.empty());
        when(repo.findByEmail(email)).thenReturn(Optional.of(common));

        var result = assertThrows(EmailAlreadyExistsException.class, () -> service.create(commonUser));

        verify(repo, times(1)).findByDocument(document);
        verify(repo, times(1)).findByEmail(email);
        verify(repo, times(0)).save(any(User.class));

        assertEquals("Este email já foi cadastrado.", result.getMessage());
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUserById() {
    }
}