package com.training.WalletPlus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferRequestDTO {
    private String reciver;
    private double tranferamount;

}
