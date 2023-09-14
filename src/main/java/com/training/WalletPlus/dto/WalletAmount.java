package com.training.WalletPlus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletAmount {
   private String username;
   private double amount;
}
