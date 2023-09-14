package com.training.WalletPlus.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransferRequestDTOTest {

    @Test
    public void testConstructorAndGetters() {
        String receiver = "receiverUser";
        double transferAmount = 100.0;

        TransferRequestDTO requestDTO = new TransferRequestDTO(receiver, transferAmount);

        assertEquals(receiver, requestDTO.getReciver());
        assertEquals(transferAmount, requestDTO.getTranferamount());
    }

    @Test
    public void testEqualsAndHashCode() {
        TransferRequestDTO dto1 = new TransferRequestDTO("receiverUser", 100.0);
        TransferRequestDTO dto2 = new TransferRequestDTO("receiverUser", 100.0);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_DifferentValues() {
        TransferRequestDTO dto1 = new TransferRequestDTO("receiverUser", 100.0);
        TransferRequestDTO dto2 = new TransferRequestDTO("differentUser", 150.0);

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testToString() {
        TransferRequestDTO requestDTO = new TransferRequestDTO("receiverUser", 100.0);

        String expectedToString = "TransferRequestDTO(reciver=receiverUser, tranferamount=100.0)";
        assertEquals(expectedToString, requestDTO.toString());
    }
}
