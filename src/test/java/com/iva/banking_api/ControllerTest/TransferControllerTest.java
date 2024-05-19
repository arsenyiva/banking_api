package com.iva.banking_api.ControllerTest;

import com.iva.banking_api.controller.TransferController;
import com.iva.banking_api.model.pojo.TransferRequest;
import com.iva.banking_api.service.TransferService;
import com.iva.banking_api.service.UserService;
import com.iva.banking_api.util.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки функционала контроллера TransferController.
 */
@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private UserService userService;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @Test
    public void testTransferMoney_SuccessfulTransfer() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer fakeToken");
        when(jwtTokenUtils.extractUsername(anyString())).thenReturn("testUser");
        when(userService.isUserOwnsId(eq("testUser"), anyLong())).thenReturn(true);

        ResponseEntity<String> successfulResponse = ResponseEntity.ok().body("Transfer successful");
        when(transferService.transferMoney(anyLong(), any(TransferRequest.class)))
                .thenAnswer(invocation -> successfulResponse);

        // Act
        ResponseEntity<?> responseEntity = transferController.transferMoney(123L, request, new TransferRequest());

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Transfer successful", responseEntity.getBody());
        verify(transferService, times(1)).transferMoney(anyLong(), any(TransferRequest.class));
    }

    @Test
    public void testTransferMoney_AccessDenied() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer fakeToken");
        when(jwtTokenUtils.extractUsername(anyString())).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = transferController.transferMoney(123L, request, new TransferRequest());

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Access denied", responseEntity.getBody());
        verify(userService, never()).isUserOwnsId(anyString(), anyLong());
        verify(transferService, never()).transferMoney(anyLong(), any(TransferRequest.class));
    }

}
