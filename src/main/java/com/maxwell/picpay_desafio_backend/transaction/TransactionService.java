package com.maxwell.picpay_desafio_backend.transaction;

import com.maxwell.picpay_desafio_backend.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public Transaction create(Transaction transaction){

        // 1- Validar
        validate()

        // 2 - criar a transação
        var newTransaction = transactionRepository.save(transaction);

        // 3 - debitar a carteira
        var wallet = walletRepository.findById(transaction.payer()).get();
        walletRepository.save(wallet.debit(transaction.value()));


        // 4 - chamar serviços externos

        return newTransaction;
    }

    private void validate(Transaction transaction){

    }
}
