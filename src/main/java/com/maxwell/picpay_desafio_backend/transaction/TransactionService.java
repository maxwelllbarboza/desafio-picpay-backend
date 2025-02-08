package com.maxwell.picpay_desafio_backend.transaction;

import com.maxwell.picpay_desafio_backend.exception.InvalidTransactionException;
import com.maxwell.picpay_desafio_backend.wallet.Wallet;
import com.maxwell.picpay_desafio_backend.wallet.WalletRepository;
import com.maxwell.picpay_desafio_backend.wallet.WalletType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public Transaction create(Transaction transaction){

        // 1- Validar
        validate(transaction);

        // 2 - criar a transação
        var newTransaction = transactionRepository.save(transaction);

        // 3 - debitar a carteira
        var wallet = walletRepository.findById(transaction.payer()).get();
        walletRepository.save(wallet.debit(transaction.value()));

        // 4 - chamar serviços externos
        return newTransaction;
    }

    // 1- the payer has a common wallet
    // 2- the payer has enough balance
    // 3 - the payer is not the payee

    private void validate(Transaction transaction){
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                    .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                    .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction)));

    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0 &&
                !payer.id().equals(transaction.payee());
    }
}
