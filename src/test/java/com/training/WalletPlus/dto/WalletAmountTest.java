package com.training.WalletPlus.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletAmountTest {

    @Test
    public void testConstructorAndGetters() {
        String username = "testuser";
        double amount = 100.0;

        WalletAmount walletAmount = new WalletAmount(username, amount);

        assertEquals(username, walletAmount.getUsername());
        assertEquals(amount, walletAmount.getAmount());
    }

    @Test
    public void testEqualsAndHashCode() {
        WalletAmount walletAmount1 = new WalletAmount("testuser", 100.0);
        WalletAmount walletAmount2 = new WalletAmount("testuser", 100.0);

        assertEquals(walletAmount1, walletAmount2);
        assertEquals(walletAmount1.hashCode(), walletAmount2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_DifferentValues() {
        WalletAmount walletAmount1 = new WalletAmount("testuser", 100.0);
        WalletAmount walletAmount2 = new WalletAmount("differentuser", 150.0);

        assertNotEquals(walletAmount1, walletAmount2);
        assertNotEquals(walletAmount1.hashCode(), walletAmount2.hashCode());
    }

    @Test
    public void testToString() {
        WalletAmount walletAmount = new WalletAmount("testuser", 100.0);

        String expectedToString = "WalletAmount(username=testuser, amount=100.0)";
        assertEquals(expectedToString, walletAmount.toString());
    }
}
