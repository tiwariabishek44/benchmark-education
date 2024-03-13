package com.benchmark.education.service.admin;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.exception.NoSuchAccountException;
import com.benchmark.education.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


@Service
@Transactional
public class AdminService {

    @Autowired
    private AccountRepository accountRepository;


    public ResponseDto<String> verifyTeacher(int id) {
        Optional<Account> accountOptional = this.accountRepository.findById(id);

        if(accountOptional.isEmpty()){
            throw new NoSuchAccountException("Invalid Teacher Account Provided");
        }
        Account account = accountOptional.get();

        account.setIsVerified(true);
        this.accountRepository.save(account);

        return ResponseDto.Success("",null);
    }
}
