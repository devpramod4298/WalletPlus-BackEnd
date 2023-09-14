package com.training.WalletPlus.dto;

import com.training.WalletPlus.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TransactionListDTO
{
    private  List<Transaction> transactions;
    private int currentPage;
    private long totalItems;
    private int totalPages;
}
